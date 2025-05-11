package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "propositions")
public class Proposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private LocalDate dateCreation;
    private Double montantPropose;
    private String status; // "EN_ATTENTE", "ACCEPTÉ", "REFUSÉ", etc.

    @ManyToOne
    @JoinColumn(name = "dossier_id")
    private Dossier dossier;

    // Constructors
    public Proposition() {
        this.dateCreation = LocalDate.now();
    }

    public Proposition(String titre, String description, Double montantPropose, String status, Dossier dossier) {
        this.titre = titre;
        this.description = description;
        this.dateCreation = LocalDate.now();
        this.montantPropose = montantPropose;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public Dossier getDossier() {
        return dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }
}
