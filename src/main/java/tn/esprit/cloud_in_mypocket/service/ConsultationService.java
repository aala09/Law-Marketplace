package tn.esprit.cloud_in_mypocket.service;

import tn.esprit.cloud_in_mypocket.dto.search.ConsultationSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.repository.ConsultationRepository;
import tn.esprit.cloud_in_mypocket.repository.DossierRepository;
import tn.esprit.cloud_in_mypocket.specification.ConsultationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DossierRepository dossierRepository;

    // Create
    public Consultation createConsultation(Consultation consultation) {
        validateConsultation(consultation);
        return consultationRepository.save(consultation);
    }

    // Read all consultations
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public Optional<Consultation> getConsultationById(Long id) {
        return consultationRepository.findById(id);
    }

    public List<Consultation> getConsultationsByDossier(Long dossierId) {
        Optional<Dossier> dossier = dossierRepository.findById(dossierId);
        return dossier.map(consultationRepository::findByDossier).orElse(List.of());
    }

    public List<Consultation> getConsultationsByStatus(String status) {
        return consultationRepository.findByStatus(status);
    }

    public List<Consultation> getUpcomingConsultations() {
        // Updated to use slotStart instead of dateHeure
        return consultationRepository.findBySlotStartAfter(LocalDateTime.now());
    }

    public List<Consultation> getPastConsultations() {
        // Updated to use slotStart instead of dateHeure
        return consultationRepository.findBySlotStartBefore(LocalDateTime.now());
    }

    // New method: Get consultations scheduled for today
    public List<Consultation> getConsultationsForToday() {
        LocalDate today = LocalDate.now();
        // Start of today
        LocalDateTime startOfDay = today.atStartOfDay();
        // End of today: use the maximum time of the day
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        // It is assumed that your repository has an appropriate method (see below)
        return consultationRepository.findConsultationsForDay(startOfDay, endOfDay);
    }

    // Modified method to get consultations for a specific date range
    public List<Consultation> getConsultationsForDateRange(LocalDateTime start, LocalDateTime end) {
        System.out.println("Searching for consultations between " + start + " and " + end);
        
        // Use slotStart field for all queries
        List<Consultation> consultations = consultationRepository.findBySlotStartBetween(start, end);
        System.out.println("Found " + consultations.size() + " consultations by slotStart");
        
        // We can also try using the query method if the above didn't yield results
        if (consultations.isEmpty()) {
            try {
                List<Consultation> queryConsultations = consultationRepository.findConsultationsForDay(start, end);
                System.out.println("Found " + queryConsultations.size() + " consultations using custom query");
                consultations.addAll(queryConsultations);
            } catch (Exception e) {
                System.out.println("Failed to execute custom query: " + e.getMessage());
            }
        }
        
        return consultations;
    }

    // Advanced search with pagination
    public Page<Consultation> searchConsultations(ConsultationSearchCriteria criteria, Pageable pageable) {
        Specification<Consultation> spec = ConsultationSpecification.buildSpecification(criteria);
        return consultationRepository.findAll(spec, pageable);
    }

    public List<Consultation> searchConsultations(ConsultationSearchCriteria criteria) {
        Specification<Consultation> spec = ConsultationSpecification.buildSpecification(criteria);
        return consultationRepository.findAll(spec);
    }

    // Update
    public Consultation updateConsultation(Long id, Consultation consultationDetails) {
        return consultationRepository.findById(id)
                .map(existingConsultation -> {
                    existingConsultation.setSujet(consultationDetails.getSujet());
                    existingConsultation.setDescription(consultationDetails.getDescription());
                    existingConsultation.setSlotStart(consultationDetails.getSlotStart());
                    existingConsultation.setDureeMinutes(consultationDetails.getDureeMinutes());
                    existingConsultation.setStatus(consultationDetails.getStatus());
                    existingConsultation.setNotes(consultationDetails.getNotes());

                    if (consultationDetails.getDossier() != null) {
                        existingConsultation.setDossier(consultationDetails.getDossier());
                    }

                    return consultationRepository.save(existingConsultation);
                })
                .orElseThrow(() -> new RuntimeException("Consultation not found with id: " + id));
    }

    // Delete
    public void deleteConsultation(Long id) {
        if (!consultationRepository.existsById(id)) {
            throw new RuntimeException("Consultation not found with id: " + id);
        }
        consultationRepository.deleteById(id);
    }

    // Helper method to validate the consultation before saving
    private void validateConsultation(Consultation consultation) {
        if (consultation.getSujet() == null || consultation.getSujet().trim().isEmpty()) {
            throw new IllegalArgumentException("Consultation subject cannot be empty");
        }

        if (consultation.getSlotStart() == null) {
            throw new IllegalArgumentException("Consultation date and time cannot be null");
        }

        if (consultation.getDureeMinutes() == null || consultation.getDureeMinutes() <= 0) {
            throw new IllegalArgumentException("Consultation duration must be greater than 0");
        }

        // Dossier is optional (no validation error if null)
    }

    // Method to update consultations that should now be marked as TERMINÉE
    public void updateExpiredConsultationStatuses() {
        // Updated to use slotStart instead of dateHeure
        List<Consultation> expiredConsultations = consultationRepository.findBySlotStartBeforeAndStatus(LocalDateTime.now(), "PLANIFIÉE");
        if (expiredConsultations.isEmpty()) {
            return;
        }

        // Update the status for each expired consultation
        for (Consultation consultation : expiredConsultations) {
            consultation.setStatus("TERMINÉE"); // or your appropriate status label
            consultationRepository.save(consultation);
        }
    }
}
