package tn.esprit.cloud_in_mypocket.repository;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long>, JpaSpecificationExecutor<Consultation> {
    List<Consultation> findByDossier(Dossier dossier);
    
    // The following method exists and is fine
    List<Consultation> findBySlotStartBetween(LocalDateTime start, LocalDateTime end);
    
    List<Consultation> findByStatus(String status);
    
    // Keep these methods that use slotStart
    List<Consultation> findBySlotStartAfter(LocalDateTime dateTime);
    List<Consultation> findBySlotStartBefore(LocalDateTime dateTime);
    List<Consultation> findBySlotStartBeforeAndStatus(LocalDateTime dateTime, String status);

    // Keep this query
    @Query("SELECT c FROM Consultation c WHERE c.slotStart >= ?1 AND c.slotStart < ?2")
    List<Consultation> findConsultationsForDay(LocalDateTime startOfDay, LocalDateTime endOfDay);
    
    // Methods for booking functionality
    boolean existsByLawyerAndSlotStartAndStatusNot(User lawyer, LocalDateTime slotStart, String cancelled);
    
    List<Consultation> findAllByLawyerAndSlotStartBetween(User lawyer, LocalDateTime start, LocalDateTime end);
}
