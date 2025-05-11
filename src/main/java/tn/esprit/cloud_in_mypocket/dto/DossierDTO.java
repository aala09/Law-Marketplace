package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.entity.Proposition;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DossierDTO {
    private Long id;
    private String reference;
    private String description;
    private LocalDate dateCreation;
    
    // Client info
    private Long clientId;
    private String clientNom;
    private String clientPrenom;
    
    // Lawyer info
    private Long lawyerId;
    private String lawyerNom;
    private String lawyerPrenom;
    
    // Contrat info if available
    private Long contratId;
    private String contratReference;
    
    // Lists with summarized objects to avoid circular references
    private List<ConsultationSummaryDTO> consultations = new ArrayList<>();
    private List<PropositionSummaryDTO> propositions = new ArrayList<>();

    public DossierDTO() {
    }

    public DossierDTO(Dossier dossier) {
        this.id = dossier.getId();
        this.reference = dossier.getReference();
        this.description = dossier.getDescription();
        this.dateCreation = dossier.getDateCreation();
        
        if (dossier.getClient() != null) {
            this.clientId = dossier.getClient().getId();
            this.clientNom = dossier.getClient().getNom();
            this.clientPrenom = dossier.getClient().getPrenom();
        }
        
        if (dossier.getLawyer() != null) {
            this.lawyerId = dossier.getLawyer().getId();
            this.lawyerNom = dossier.getLawyer().getNom();
            this.lawyerPrenom = dossier.getLawyer().getPrenom();
        }
        
        if (dossier.getContrat() != null) {
            this.contratId = dossier.getContrat().getId();
            this.contratReference = dossier.getContrat().getReference();
        }
        
        // Convert consultations to simplified DTOs that don't include dossier details
        if (dossier.getConsultations() != null) {
            this.consultations = dossier.getConsultations().stream()
                .map(ConsultationSummaryDTO::new)
                .collect(Collectors.toList());
        }
        
        // Convert propositions to simplified DTOs that don't include dossier details
        if (dossier.getPropositions() != null) {
            this.propositions = dossier.getPropositions().stream()
                .map(PropositionSummaryDTO::new)
                .collect(Collectors.toList());
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

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getClientPrenom() {
        return clientPrenom;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerNom() {
        return lawyerNom;
    }

    public void setLawyerNom(String lawyerNom) {
        this.lawyerNom = lawyerNom;
    }

    public String getLawyerPrenom() {
        return lawyerPrenom;
    }

    public void setLawyerPrenom(String lawyerPrenom) {
        this.lawyerPrenom = lawyerPrenom;
    }

    public Long getContratId() {
        return contratId;
    }

    public void setContratId(Long contratId) {
        this.contratId = contratId;
    }

    public String getContratReference() {
        return contratReference;
    }

    public void setContratReference(String contratReference) {
        this.contratReference = contratReference;
    }

    public List<ConsultationSummaryDTO> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<ConsultationSummaryDTO> consultations) {
        this.consultations = consultations;
    }

    public List<PropositionSummaryDTO> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<PropositionSummaryDTO> propositions) {
        this.propositions = propositions;
    }
}
