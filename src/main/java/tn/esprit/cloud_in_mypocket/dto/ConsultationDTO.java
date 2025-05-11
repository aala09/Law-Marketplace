package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import java.time.LocalDateTime;

public class ConsultationDTO {
    private Long id;
    private String sujet;
    private String description;
    private LocalDateTime dateHeure;
    private Integer dureeMinutes;
    private String status;
    private String notes;
    private DossierSummaryDTO dossier;

    public ConsultationDTO() {
    }

    public ConsultationDTO(Consultation consultation) {
        this.id = consultation.getId();
        this.sujet = consultation.getSujet();
        this.description = consultation.getDescription();
        this.dateHeure = consultation.getDateHeure();
        this.dureeMinutes = consultation.getDureeMinutes();
        this.status = consultation.getStatus();
        this.notes = consultation.getNotes();
        
        if (consultation.getDossier() != null) {
            this.dossier = new DossierSummaryDTO(consultation.getDossier());
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DossierSummaryDTO getDossier() {
        return dossier;
    }

    public void setDossier(DossierSummaryDTO dossier) {
        this.dossier = dossier;
    }
}
