package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.dto.DossierDTO;
import tn.esprit.cloud_in_mypocket.dto.DossierSummaryDTO;
import tn.esprit.cloud_in_mypocket.dto.search.DossierSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.service.DossierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dossiers")
@CrossOrigin(origins = "http://localhost:4200")
public class DossierController {

    @Autowired
    private DossierService dossierService;

    // Create a new dossier
    @PostMapping
    public ResponseEntity<DossierDTO> createDossier(@RequestBody Dossier dossier) {
        try {
            Dossier createdDossier = dossierService.createDossier(dossier);
            return new ResponseEntity<>(new DossierDTO(createdDossier), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all dossiers - use summary DTO for list view
    @GetMapping
    public ResponseEntity<List<DossierSummaryDTO>> getAllDossiers() {
        List<DossierSummaryDTO> dossierDTOs = dossierService.getAllDossiers()
                .stream()
                .map(DossierSummaryDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dossierDTOs, HttpStatus.OK);
    }

    // Advanced search endpoint with pagination
    @PostMapping("/search")
    public ResponseEntity<Page<DossierSummaryDTO>> searchDossiers(
            @RequestBody DossierSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<DossierSummaryDTO> result = dossierService.searchDossiers(criteria, pageable)
                .map(DossierSummaryDTO::new);
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    // Simple search without pagination
    @PostMapping("/filter")
    public ResponseEntity<?> filterDossiers(
            @RequestBody DossierSearchCriteria criteria) {
        
        try {
            System.out.println("Filter criteria received: " + criteria);
            
            List<Dossier> dossiers = dossierService.searchDossiers(criteria);
            
            if (dossiers.isEmpty()) {
                System.out.println("No dossiers found matching criteria");
                return ResponseEntity.ok()
                    .header("X-Result-Count", "0")
                    .body(new HashMap<String, Object>() {{
                        put("message", "No dossiers found matching criteria");
                        put("data", Collections.emptyList());
                    }});
            }
            
            List<DossierSummaryDTO> dossierDTOs = dossiers.stream()
                .map(DossierSummaryDTO::new)
                .collect(Collectors.toList());
            
            System.out.println("Found " + dossierDTOs.size() + " dossiers");
            return ResponseEntity.ok()
                .header("X-Result-Count", String.valueOf(dossierDTOs.size()))
                .body(dossierDTOs);
            
        } catch (Exception e) {
            System.err.println("Error in filter dossiers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HashMap<String, String>() {{
                    put("error", "Error processing filter request");
                    put("message", e.getMessage());
                }});
        }
    }

    // Get dossier by ID - use full DTO for detail view
    @GetMapping("/{id}")
    public ResponseEntity<DossierDTO> getDossierById(@PathVariable Long id) {
        return dossierService.getDossierById(id)
                .map(dossier -> new ResponseEntity<>(new DossierDTO(dossier), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get dossiers by client ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DossierSummaryDTO>> getDossiersByClient(@PathVariable Long clientId) {
        List<DossierSummaryDTO> dossierDTOs = dossierService.getDossiersByClient(clientId)
                .stream()
                .map(DossierSummaryDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dossierDTOs, HttpStatus.OK);
    }

    // Get dossiers by lawyer ID
    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<DossierSummaryDTO>> getDossiersByLawyer(@PathVariable Long lawyerId) {
        List<DossierSummaryDTO> dossierDTOs = dossierService.getDossiersByLawyer(lawyerId)
                .stream()
                .map(DossierSummaryDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dossierDTOs, HttpStatus.OK);
    }

    // Get dossiers by reference
    @GetMapping("/reference/{reference}")
    public ResponseEntity<List<DossierSummaryDTO>> getDossiersByReference(@PathVariable String reference) {
        List<DossierSummaryDTO> dossierDTOs = dossierService.getDossiersByReference(reference)
                .stream()
                .map(DossierSummaryDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dossierDTOs, HttpStatus.OK);
    }

    // Update dossier
    @PutMapping("/{id}")
    public ResponseEntity<DossierDTO> updateDossier(@PathVariable Long id, @RequestBody Dossier dossier) {
        try {
            Dossier updatedDossier = dossierService.updateDossier(id, dossier);
            return new ResponseEntity<>(new DossierDTO(updatedDossier), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete dossier
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable Long id) {
        try {
            dossierService.deleteDossier(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Enhanced endpoint to upload a file to a dossier with optional custom document name
    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long id, 
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "customDocumentName", required = false) String customDocumentName) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to upload empty file");
            }
            
            Dossier updatedDossier = dossierService.updateDossierFile(id, file, customDocumentName);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "File uploaded successfully");
            response.put("fileName", updatedDossier.getFileName());
            response.put("fileSize", updatedDossier.getFileSize());
            response.put("fileType", updatedDossier.getFileType());
            
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        }
    }
    
    // Add a similar endpoint for add-file to support custom document names
    @PostMapping("/{id}/add-file")
    public ResponseEntity<?> addFileIfNotExists(
            @PathVariable Long id, 
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "customDocumentName", required = false) String customDocumentName) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to upload empty file");
            }
            
            try {
                Dossier updatedDossier = dossierService.addDossierFileIfNotExists(id, file, customDocumentName);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "File added successfully");
                response.put("fileName", updatedDossier.getFileName());
                response.put("fileSize", updatedDossier.getFileSize());
                response.put("fileType", updatedDossier.getFileType());
                
                return ResponseEntity.ok().body(response);
            } catch (IllegalStateException e) {
                // This is expected if file already exists - return a specific status code for this case
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("File not added: " + e.getMessage());
            }
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        }
    }
    
    // Endpoint to get the file from a dossier
    @GetMapping("/{id}/file")
    public ResponseEntity<?> getFile(@PathVariable Long id) {
        return dossierService.getDossierById(id)
            .map(dossier -> {
                if (dossier.getFileData() == null || dossier.getFileName() == null) {
                    return ResponseEntity.notFound().build();
                }
                
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dossier.getFileName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, dossier.getFileType())
                    .body(dossier.getFileData());
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to check if a dossier has a file attached
    @GetMapping("/{id}/hasFile")
    public ResponseEntity<?> checkDossierHasFile(@PathVariable Long id) {
        return dossierService.getDossierById(id)
            .map(dossier -> {
                Map<String, Boolean> response = new HashMap<>();
                response.put("hasFile", dossier.hasFile());
                return ResponseEntity.ok(response);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new dossier with a file in a single operation
     */
    @PostMapping("/create-with-file")
    public ResponseEntity<?> createDossierWithFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String dossierName,
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "customFileName", required = false) String customFileName,
            @RequestParam(value = "clientId", required = false) Long clientId,
            @RequestParam(value = "lawyerId", required = false) Long lawyerId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to upload empty file");
            }
            
            // Create a dossier with the given name
            Dossier dossier = new Dossier();
            dossier.setDescription(dossierName);
            
            // Use provided reference or generate a unique one
            if (reference != null && !reference.trim().isEmpty()) {
                dossier.setReference(reference);
            } else {
                dossier.setReference("REF-" + System.currentTimeMillis()); // Generate a unique reference
            }
            
            dossier.setDateCreation(LocalDate.now());
            
            // Save the dossier to get an ID
            Dossier savedDossier = dossierService.createDossier(dossier);
            
            // Now attach the file to the dossier
            Dossier updatedDossier = dossierService.updateDossierFile(savedDossier.getId(), file, customFileName);
            
            return ResponseEntity.ok().body(new DossierDTO(updatedDossier));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("File upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Dossier creation failed: " + e.getMessage());
        }
    }
}
