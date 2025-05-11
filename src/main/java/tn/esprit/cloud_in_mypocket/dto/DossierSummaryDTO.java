package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.entity.Dossier;
import java.time.LocalDate;

public class DossierSummaryDTO {
    private Long id;
    private String reference;
    private String description;
    private LocalDate dateCreation;
    
    // Minimal client info
    private Long clientId;
    private String clientName;
    
    // Minimal lawyer info
    private Long lawyerId;
    private String lawyerName;

    public DossierSummaryDTO() {
    }

    public DossierSummaryDTO(Dossier dossier) {
        this.id = dossier.getId();
        this.reference = dossier.getReference();
        this.description = dossier.getDescription();
        this.dateCreation = dossier.getDateCreation();
        
        if (dossier.getClient() != null) {
            this.clientId = dossier.getClient().getId();
            this.clientName = dossier.getClient().getNom() + " " + dossier.getClient().getPrenom();
        }
        
        if (dossier.getLawyer() != null) {
            this.lawyerId = dossier.getLawyer().getId();
            this.lawyerName = dossier.getLawyer().getNom() + " " + dossier.getLawyer().getPrenom();
        }
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public void setLawyerName(String lawyerName) {
        this.lawyerName = lawyerName;
    }
}
