package tn.esprit.cloud_in_mypocket.dto.search;

import java.time.LocalDate;

public class DossierSearchCriteria {
    private String reference;
    private String description;
    private Long clientId;
    private Long lawyerId;
    private LocalDate dateCreationStart;
    private LocalDate dateCreationEnd;
    private String status;
    
    public DossierSearchCriteria() {
    }
    
    // Getters and Setters
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
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Long getLawyerId() {
        return lawyerId;
    }
    
    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }
    
    public LocalDate getDateCreationStart() {
        return dateCreationStart;
    }
    
    public void setDateCreationStart(LocalDate dateCreationStart) {
        this.dateCreationStart = dateCreationStart;
    }
    
    public LocalDate getDateCreationEnd() {
        return dateCreationEnd;
    }
    
    public void setDateCreationEnd(LocalDate dateCreationEnd) {
        this.dateCreationEnd = dateCreationEnd;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
