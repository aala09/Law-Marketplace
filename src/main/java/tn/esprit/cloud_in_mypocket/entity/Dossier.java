package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dossiers")
public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String description;
    private LocalDate dateCreation;

    // Enhanced file field annotations with better column definitions
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;   // The binary content of the file

    @Column(name = "file_name", length = 255)
    private String fileName;   // Original file name

    @Column(name = "file_type", length = 100)
    private String fileType;   // MIME type (e.g., application/pdf, image/png)

    @Column(name = "file_size")
    private Long fileSize;     // Size in bytes

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;  // References User with CLIENT role

    @ManyToOne
    @JoinColumn(name = "lawyer_id")
    private User lawyer;  // References User with LAWYER role

    @OneToOne(mappedBy = "dossier", cascade = CascadeType.ALL)
    private Contrat contrat;

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<Proposition> propositions = new ArrayList<>();

    @OneToMany(mappedBy = "dossier", cascade = CascadeType.ALL)
    private List<Consultation> consultations = new ArrayList<>();

    // Constructors
    public Dossier() {
        this.dateCreation = LocalDate.now();
    }

    public Dossier(String reference, String description, User client, User lawyer) {
        this.reference = reference;
        this.description = description;
        this.dateCreation = LocalDate.now();
        this.client = client;
        this.lawyer = lawyer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        // Improved validation to check if role is CLIENT
        if (client != null && client.getRole() != null && !client.getRole().equals("CLIENT")) {
            throw new IllegalArgumentException("User must have CLIENT role to be assigned as client");
        }
        this.client = client;
    }

    public User getLawyer() {
        return lawyer;
    }

    public void setLawyer(User lawyer) {
        // Improved validation to check if role is LAWYER
        if (lawyer != null && lawyer.getRole() != null && !lawyer.getRole().equals("LAWYER")) {
            throw new IllegalArgumentException("User must have LAWYER role to be assigned as lawyer");
        }
        this.lawyer = lawyer;
    }

    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    // Enhanced getter for fileData with null check
    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    // Add helper method to check if file exists
    public boolean hasFile() {
        return fileData != null && fileName != null && fileSize != null;
    }
    
    // Add helper method to get file size in human-readable format
    public String getReadableFileSize() {
        if (fileSize == null) return "0 B";
        
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
        return String.format("%.1f %s", fileSize / Math.pow(1024, digitGroups), 
                units[digitGroups]);
    }
}
