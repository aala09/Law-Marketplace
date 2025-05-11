package tn.esprit.cloud_in_mypocket.dto.search;

import java.time.LocalDateTime;

public class ConsultationSearchCriteria {
    private String sujet;
    private String description;
    private Long dossierId;
    private LocalDateTime dateHeureStart;
    private LocalDateTime dateHeureEnd;
    private String status;
    private Integer dureeMinutesMin;
    private Integer dureeMinutesMax;
    
    public ConsultationSearchCriteria() {
    }
    
    // Getters and Setters
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
    
    public Long getDossierId() {
        return dossierId;
    }
    
    public void setDossierId(Long dossierId) {
        this.dossierId = dossierId;
    }
    
    public LocalDateTime getDateHeureStart() {
        return dateHeureStart;
    }
    
    public void setDateHeureStart(LocalDateTime dateHeureStart) {
        this.dateHeureStart = dateHeureStart;
    }
    
    public LocalDateTime getDateHeureEnd() {
        return dateHeureEnd;
    }
    
    public void setDateHeureEnd(LocalDateTime dateHeureEnd) {
        this.dateHeureEnd = dateHeureEnd;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getDureeMinutesMin() {
        return dureeMinutesMin;
    }
    
    public void setDureeMinutesMin(Integer dureeMinutesMin) {
        this.dureeMinutesMin = dureeMinutesMin;
    }
    
    public Integer getDureeMinutesMax() {
        return dureeMinutesMax;
    }
    
    public void setDureeMinutesMax(Integer dureeMinutesMax) {
        this.dureeMinutesMax = dureeMinutesMax;
    }
}
