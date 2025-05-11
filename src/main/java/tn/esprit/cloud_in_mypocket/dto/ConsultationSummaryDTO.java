package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import java.time.LocalDateTime;

public class ConsultationSummaryDTO {
    private Long id;
    private String sujet;
    private LocalDateTime dateHeure;
    private Integer dureeMinutes;
    private String status;

    public ConsultationSummaryDTO() {
    }

    public ConsultationSummaryDTO(Consultation consultation) {
        this.id = consultation.getId();
        this.sujet = consultation.getSujet();
        this.dateHeure = consultation.getDateHeure();
        this.dureeMinutes = consultation.getDureeMinutes();
        this.status = consultation.getStatus();
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
}
