package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sujet;
    private String description;
    
    // --- Direct relationships to users ---
    @ManyToOne
    @JoinColumn(name = "lawyer_id", nullable = false)
    private User lawyer;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;
    
    // --- Booking slot ---
    private LocalDateTime slotStart;     // renamed from dateHeure
    private Integer dureeMinutes;
    private String status; // "PLANIFIÉE", "TERMINÉE", "ANNULÉE", etc.
    private String notes;

    // (Optional) keep dossier link - now optional
    @ManyToOne
    @JoinColumn(name = "dossier_id")
    private Dossier dossier;

    // Constructors
    public Consultation() {
    }

    public Consultation(String sujet, String description, User lawyer, User client, 
                        LocalDateTime slotStart, Integer dureeMinutes, 
                        String status, String notes, Dossier dossier) {
        this.sujet = sujet;
        this.description = description;
        this.lawyer = lawyer;
        this.client = client;
        this.slotStart = slotStart;
        this.dureeMinutes = dureeMinutes;
        this.status = status;
        this.notes = notes;
        this.dossier = dossier; // This is optional
    }

    // Original constructor for backward compatibility
    public Consultation(String sujet, String description, LocalDateTime dateHeure, Integer dureeMinutes, 
                        String status, String notes, Dossier dossier) {
        this.sujet = sujet;
        this.description = description;
        this.slotStart = dateHeure;
        this.dureeMinutes = dureeMinutes;
        this.status = status;
        this.notes = notes;
        this.dossier = dossier;
        
        // Extract lawyer and client from dossier if available
        if (dossier != null) {
            this.lawyer = dossier.getLawyer();
            this.client = dossier.getClient();
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

    // Changed getter/setter from dateHeure to slotStart
    public LocalDateTime getDateHeure() {
        return slotStart; // For backward compatibility
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.slotStart = dateHeure; // For backward compatibility
    }
    
    // New getter/setter for slotStart
    public LocalDateTime getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(LocalDateTime slotStart) {
        this.slotStart = slotStart;
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

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public User getLawyer() {
        return lawyer;
    }

    public void setLawyer(User lawyer) {
        this.lawyer = lawyer;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}
