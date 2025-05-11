package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contrats")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private String description;
    private LocalDate dateSignature;
    private Double montant;
    private String status; // "BROUILLON", "SIGNÉ", "TERMINÉ", etc.

    @OneToOne
    @JoinColumn(name = "dossier_id")
    private Dossier dossier;

    // Constructors
    public Contrat() {
    }

    public Contrat(String reference, String description, LocalDate dateSignature, Double montant, String status, Dossier dossier) {
        this.reference = reference;
        this.description = description;
        this.dateSignature = dateSignature;
        this.montant = montant;
        this.status = status;
        this.dossier = dossier;
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

    public LocalDate getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(LocalDate dateSignature) {
        this.dateSignature = dateSignature;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }
}
