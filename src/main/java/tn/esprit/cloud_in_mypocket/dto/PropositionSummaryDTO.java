package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.entity.Proposition;
import java.time.LocalDate;

public class PropositionSummaryDTO {
    private Long id;
    private String titre;
    private LocalDate dateCreation;
    private Double montantPropose;
    private String status;

    public PropositionSummaryDTO() {
    }

    public PropositionSummaryDTO(Proposition proposition) {
        this.id = proposition.getId();
        this.titre = proposition.getTitre();
        this.dateCreation = proposition.getDateCreation();
        this.montantPropose = proposition.getMontantPropose();
        this.status = proposition.getStatus();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getMontantPropose() {
        return montantPropose;
    }

    public void setMontantPropose(Double montantPropose) {
        this.montantPropose = montantPropose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
