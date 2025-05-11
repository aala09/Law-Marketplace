package tn.esprit.cloud_in_mypocket.repository;

import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long>, JpaSpecificationExecutor<Dossier> {
    List<Dossier> findByClient(User client);
    List<Dossier> findByLawyer(User lawyer);
    List<Dossier> findByReference(String reference);
    
    // File-related query methods
    List<Dossier> findByFileNameContainingIgnoreCase(String fileName);
    List<Dossier> findByFileTypeContainingIgnoreCase(String fileType);
    List<Dossier> findByFileSizeGreaterThan(Long size);
    List<Dossier> findByFileSizeLessThan(Long size);
    
    // Check if dossier has attached file
    List<Dossier> findByFileNameIsNotNull();
    List<Dossier> findByFileNameIsNull();
    
    // Find dossiers by client and has file attached
    List<Dossier> findByClientAndFileNameIsNotNull(User client);
    
    // Find dossiers by lawyer and has file attached
    List<Dossier> findByLawyerAndFileNameIsNotNull(User lawyer);
    
    // Additional file-related query methods
    @Query("SELECT d FROM Dossier d WHERE d.fileName IS NOT NULL")
    List<Dossier> findAllWithFiles();
    
    @Query("SELECT d FROM Dossier d WHERE d.fileType = ?1")
    List<Dossier> findByFileType(String fileType);
    
    @Query("SELECT COUNT(d) FROM Dossier d WHERE d.fileName IS NOT NULL")
    Long countDossiersWithFiles();
    
    @Query("SELECT SUM(d.fileSize) FROM Dossier d WHERE d.fileSize IS NOT NULL")
    Long getTotalFileStorageUsed();
}
