package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.dto.BookingRequest;
import tn.esprit.cloud_in_mypocket.dto.ConsultationDTO;
import tn.esprit.cloud_in_mypocket.dto.search.ConsultationSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.service.ConsultationService;
import tn.esprit.cloud_in_mypocket.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private SchedulingService schedulingService;

    // Simple ping endpoint for API connection testing
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping(@RequestParam(required = false) String date) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("timestamp", LocalDateTime.now().toString());
        if (date != null) {
            response.put("receivedDate", date);
        }
        return ResponseEntity.ok(response);
    }

    // Create a new consultation
    @PostMapping
    public ResponseEntity<ConsultationDTO> createConsultation(@RequestBody Consultation consultation) {
        try {
            Consultation createdConsultation = consultationService.createConsultation(consultation);
            return new ResponseEntity<>(new ConsultationDTO(createdConsultation), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Log the error for debugging
            System.err.println("Error creating consultation: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Log any unexpected errors
            System.err.println("Unexpected error in createConsultation: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all consultations
    @GetMapping
    public ResponseEntity<List<ConsultationDTO>> getAllConsultations() {
        try {
            List<ConsultationDTO> consultationDTOs = consultationService.getAllConsultations()
                    .stream()
                    .map(ConsultationDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error retrieving all consultations: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Advanced search endpoint with pagination
    @PostMapping("/search")
    public ResponseEntity<?> searchConsultations(
            @RequestBody ConsultationSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateHeure") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        try {
            System.out.println("Search criteria received: " + criteria);

            Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ?
                    Sort.Direction.ASC : Sort.Direction.DESC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<ConsultationDTO> result = consultationService.searchConsultations(criteria, pageable)
                    .map(ConsultationDTO::new);

            if (result.isEmpty()) {
                System.out.println("No consultations found matching criteria");
                return ResponseEntity.ok()
                    .header("X-Result-Count", "0")
                    .body(new HashMap<String, Object>() {{
                        put("message", "No consultations found matching criteria");
                        put("data", result);
                        put("totalElements", 0);
                        put("totalPages", 0);
                    }});
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error in search consultations: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HashMap<String, String>() {{
                    put("error", "Error processing search request");
                    put("message", e.getMessage());
                }});
        }
    }

    // Simple search without pagination
    @PostMapping("/filter")
    public ResponseEntity<?> filterConsultations(
            @RequestBody ConsultationSearchCriteria criteria) {

        try {
            System.out.println("Filter criteria received: " + criteria);

            List<Consultation> consultations = consultationService.searchConsultations(criteria);

            if (consultations.isEmpty()) {
                System.out.println("No consultations found matching criteria");
                return ResponseEntity.ok()
                    .header("X-Result-Count", "0")
                    .body(new HashMap<String, Object>() {{
                        put("message", "No consultations found matching criteria");
                        put("data", Collections.emptyList());
                    }});
            }

            List<ConsultationDTO> consultationDTOs = consultations.stream()
                .map(ConsultationDTO::new)
                .collect(Collectors.toList());

            System.out.println("Found " + consultationDTOs.size() + " consultations");
            return ResponseEntity.ok()
                .header("X-Result-Count", String.valueOf(consultationDTOs.size()))
                .body(consultationDTOs);

        } catch (Exception e) {
            System.err.println("Error in filter consultations: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HashMap<String, String>() {{
                    put("error", "Error processing filter request");
                    put("message", e.getMessage());
                }});
        }
    }

    // Enhanced endpoint to get consultations for a specific date
    @GetMapping("/by-date")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsForDate(@RequestParam String date) {
        try {
            System.out.println("Received request for consultations on date: " + date);
            
            // Parse the date manually with detailed error logging
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(date);
            } catch (Exception e) {
                System.err.println("Error parsing date: " + date + ". Error: " + e.getMessage());
                e.printStackTrace();
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
            }
            
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
            
            System.out.println("Fetching consultations from " + startOfDay + " to " + endOfDay);
            
            // Try to find consultations using both slotStart and dateHeure fields
            List<Consultation> consultations = consultationService.getConsultationsForDateRange(startOfDay, endOfDay);
            
            // Debugging: print each consultation found
            System.out.println("Found " + consultations.size() + " consultations");
            consultations.forEach(c -> System.out.println("  ID: " + c.getId() + 
                                                         ", Subject: " + c.getSujet() + 
                                                         ", Start: " + c.getSlotStart() + 
                                                         ", Status: " + c.getStatus()));
            
            List<ConsultationDTO> consultationDTOs = consultations.stream()
                    .map(ConsultationDTO::new)
                    .collect(Collectors.toList());
                    
            return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching consultations: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get consultation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDTO> getConsultationById(@PathVariable Long id) {
        return consultationService.getConsultationById(id)
                .map(consultation -> new ResponseEntity<>(new ConsultationDTO(consultation), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get consultations by dossier ID
    @GetMapping("/dossier/{dossierId}")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByDossier(@PathVariable Long dossierId) {
        List<ConsultationDTO> consultationDTOs = consultationService.getConsultationsByDossier(dossierId)
                .stream()
                .map(ConsultationDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
    }

    // Get consultations by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ConsultationDTO>> getConsultationsByStatus(@PathVariable String status) {
        List<ConsultationDTO> consultationDTOs = consultationService.getConsultationsByStatus(status)
                .stream()
                .map(ConsultationDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
    }

    // Get upcoming consultations
    @GetMapping("/upcoming")
    public ResponseEntity<List<ConsultationDTO>> getUpcomingConsultations() {
        List<ConsultationDTO> consultationDTOs = consultationService.getUpcomingConsultations()
                .stream()
                .map(ConsultationDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
    }

    // Get past consultations
    @GetMapping("/past")
    public ResponseEntity<List<ConsultationDTO>> getPastConsultations() {
        List<ConsultationDTO> consultationDTOs = consultationService.getPastConsultations()
                .stream()
                .map(ConsultationDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(consultationDTOs, HttpStatus.OK);
    }

    // Update consultation
    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDTO> updateConsultation(@PathVariable Long id, @RequestBody Consultation consultation) {
        try {
            Consultation updatedConsultation = consultationService.updateConsultation(id, consultation);
            return new ResponseEntity<>(new ConsultationDTO(updatedConsultation), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete consultation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long id) {
        try {
            consultationService.deleteConsultation(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/book")
    public ResponseEntity<ConsultationDTO> book(@RequestBody BookingRequest req) {
        try {
            Consultation c = schedulingService.bookSlot(
                req.getLawyerId(), req.getClientId(),
                req.getSlotStart(), req.getDuration());
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(new ConsultationDTO(c));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
