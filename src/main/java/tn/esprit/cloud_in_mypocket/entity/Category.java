package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private List<User> lawyers = new ArrayList<>();  // Changed from Lawyer to User

    // Constructors
    public Category() {}

    public Category(String label) {
        this.label = label;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<User> getLawyers() {
        return lawyers;
    }

    public void setLawyers(List<User> lawyers) {
        this.lawyers = lawyers;
    }
}
