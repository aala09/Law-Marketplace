package tn.esprit.cloud_in_mypocket.service;

import tn.esprit.cloud_in_mypocket.dto.search.DossierSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.DossierRepository;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import tn.esprit.cloud_in_mypocket.specification.DossierSpecification;
import tn.esprit.cloud_in_mypocket.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DossierService {

    @Autowired
    private DossierRepository dossierRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Create
    public Dossier createDossier(Dossier dossier) {
        validateDossier(dossier);
        return dossierRepository.save(dossier);
    }

    // Read
    public List<Dossier> getAllDossiers() {
        return dossierRepository.findAll();
    }

    public Optional<Dossier> getDossierById(Long id) {
        return dossierRepository.findById(id);
    }

    public List<Dossier> getDossiersByClient(Long clientId) {
        Optional<User> client = userRepository.findById(clientId);
        return client.map(dossierRepository::findByClient).orElse(List.of());
    }

    public List<Dossier> getDossiersByLawyer(Long lawyerId) {
        Optional<User> lawyer = userRepository.findById(lawyerId);
        return lawyer.map(dossierRepository::findByLawyer).orElse(List.of());
    }

    public List<Dossier> getDossiersByReference(String reference) {
        return dossierRepository.findByReference(reference);
    }
    
    // Advanced search
    public Page<Dossier> searchDossiers(DossierSearchCriteria criteria, Pageable pageable) {
        Specification<Dossier> spec = DossierSpecification.buildSpecification(criteria);
        return dossierRepository.findAll(spec, pageable);
    }
    
    public List<Dossier> searchDossiers(DossierSearchCriteria criteria) {
        Specification<Dossier> spec = DossierSpecification.buildSpecification(criteria);
        return dossierRepository.findAll(spec);
    }

    // Update
    public Dossier updateDossier(Long id, Dossier dossierDetails) {
        return dossierRepository.findById(id)
                .map(existingDossier -> {
                    existingDossier.setReference(dossierDetails.getReference());
                    existingDossier.setDescription(dossierDetails.getDescription());
                    
                    if (dossierDetails.getClient() != null) {
                        existingDossier.setClient(dossierDetails.getClient());
                    }
                    
                    if (dossierDetails.getLawyer() != null) {
                        existingDossier.setLawyer(dossierDetails.getLawyer());
                    }
                    
                    // Preserve file data if not updated in this request
                    if (dossierDetails.getFileData() != null) {
                        existingDossier.setFileData(dossierDetails.getFileData());
                        existingDossier.setFileName(dossierDetails.getFileName());
                        existingDossier.setFileType(dossierDetails.getFileType());
                        existingDossier.setFileSize(dossierDetails.getFileSize());
                    }
                    
                    return dossierRepository.save(existingDossier);
                })
                .orElseThrow(() -> new RuntimeException("Dossier not found with id: " + id));
    }
    
    // Method to add a file to a dossier with custom name, but only if it doesn't already have a file
    public Dossier addDossierFileIfNotExists(Long id, MultipartFile file, String customDocumentName) throws IOException {
        if (!FileUtils.isValidFile(file)) {
            throw new IllegalArgumentException("Invalid file: must be under 10MB and a supported file type");
        }
        
        return dossierRepository.findById(id)
                .map(existingDossier -> {
                    // Check if the dossier already has a file
                    if (existingDossier.hasFile()) {
                        throw new IllegalStateException("Dossier already has a file. Use updateDossierFile method to replace it.");
                    }
                    
                    try {
                        existingDossier.setFileData(FileUtils.getFileBytes(file));
                        
                        // Use custom document name if provided, otherwise use original filename
                        if (customDocumentName != null && !customDocumentName.trim().isEmpty()) {
                            // Get file extension from original filename
                            String originalExtension = "";
                            String originalFilename = file.getOriginalFilename();
                            if (originalFilename != null && originalFilename.contains(".")) {
                                originalExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                            }
                            
                            // Create a final copy of the custom name and extension for use in lambda
                            final String finalCustomName;
                            if (!customDocumentName.contains(".")) {
                                finalCustomName = customDocumentName + originalExtension;
                            } else {
                                finalCustomName = customDocumentName;
                            }
                            
                            existingDossier.setFileName(finalCustomName);
                        } else {
                            existingDossier.setFileName(file.getOriginalFilename());
                        }
                        
                        existingDossier.setFileType(file.getContentType());
                        existingDossier.setFileSize(file.getSize());
                        
                        System.out.println("Saving new file: " + existingDossier.getFileName() + 
                            ", size: " + file.getSize() + 
                            ", type: " + file.getContentType());
                            
                        return dossierRepository.save(existingDossier);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to add file to dossier: " + e.getMessage(), e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Dossier not found with id: " + id));
    }
    
    // Original addDossierFileIfNotExists method for backward compatibility
    public Dossier addDossierFileIfNotExists(Long id, MultipartFile file) throws IOException {
        return addDossierFileIfNotExists(id, file, null);
    }
    
    // Updated method to support custom document name
    public Dossier updateDossierFile(Long id, MultipartFile file, String customDocumentName) throws IOException {
        if (!FileUtils.isValidFile(file)) {
            throw new IllegalArgumentException("Invalid file: must be under 10MB and a supported file type");
        }
        
        return dossierRepository.findById(id)
                .map(existingDossier -> {
                    try {
                        existingDossier.setFileData(FileUtils.getFileBytes(file));
                        
                        // Use custom document name if provided, otherwise use original filename
                        if (customDocumentName != null && !customDocumentName.trim().isEmpty()) {
                            // Get file extension from original filename
                            String originalExtension = "";
                            String originalFilename = file.getOriginalFilename();
                            if (originalFilename != null && originalFilename.contains(".")) {
                                originalExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                            }
                            
                            // Create a final copy of the custom name and extension for use in lambda
                            final String finalCustomName;
                            if (!customDocumentName.contains(".")) {
                                finalCustomName = customDocumentName + originalExtension;
                            } else {
                                finalCustomName = customDocumentName;
                            }
                            
                            existingDossier.setFileName(finalCustomName);
                        } else {
                            existingDossier.setFileName(file.getOriginalFilename());
                        }
                        
                        existingDossier.setFileType(file.getContentType());
                        existingDossier.setFileSize(file.getSize());
                        
                        System.out.println("Saving file: " + existingDossier.getFileName() + 
                            ", size: " + file.getSize() + 
                            ", type: " + file.getContentType());
                            
                        return dossierRepository.save(existingDossier);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to update dossier file: " + e.getMessage(), e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Dossier not found with id: " + id));
    }
    
    // Original updateDossierFile method for backward compatibility
    public Dossier updateDossierFile(Long id, MultipartFile file) throws IOException {
        return updateDossierFile(id, file, null);
    }
    
    // Method to remove a file from a dossier
    public Dossier removeDossierFile(Long id) {
        return dossierRepository.findById(id)
                .map(existingDossier -> {
                    existingDossier.setFileData(null);
                    existingDossier.setFileName(null);
                    existingDossier.setFileType(null);
                    existingDossier.setFileSize(null);
                    return dossierRepository.save(existingDossier);
                })
                .orElseThrow(() -> new RuntimeException("Dossier not found with id: " + id));
    }

    // Delete
    public void deleteDossier(Long id) {
        if (!dossierRepository.existsById(id)) {
            throw new RuntimeException("Dossier not found with id: " + id);
        }
        dossierRepository.deleteById(id);
    }
    
    // Helper method
    private void validateDossier(Dossier dossier) {
        if (dossier.getReference() == null || dossier.getReference().trim().isEmpty()) {
            throw new IllegalArgumentException("Dossier reference cannot be empty");
        }
        
        // Additional validations can be added here
    }
}
