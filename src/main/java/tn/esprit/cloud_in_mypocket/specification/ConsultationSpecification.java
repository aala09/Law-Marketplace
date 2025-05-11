package tn.esprit.cloud_in_mypocket.specification;

import tn.esprit.cloud_in_mypocket.dto.search.ConsultationSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

import java.util.ArrayList;
import java.util.List;

public class ConsultationSpecification {
    
    public static Specification<Consultation> buildSpecification(ConsultationSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            
            if (criteria.getSujet() != null && !criteria.getSujet().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("sujet")), 
                    "%" + criteria.getSujet().toLowerCase() + "%"
                ));
            }
            
            if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }
            
            if (criteria.getDossierId() != null) {
                Join<Consultation, Dossier> dossierJoin = root.join("dossier");
                predicates.add(criteriaBuilder.equal(dossierJoin.get("id"), criteria.getDossierId()));
            }
            
            if (criteria.getDateHeureStart() != null) {
                // Updated to use slotStart instead of dateHeure
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("slotStart"), criteria.getDateHeureStart()
                ));
            }
            
            if (criteria.getDateHeureEnd() != null) {
                // Updated to use slotStart instead of dateHeure
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("slotStart"), criteria.getDateHeureEnd()
                ));
            }
            
            if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                    root.get("status"), criteria.getStatus()
                ));
            }
            
            if (criteria.getDureeMinutesMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dureeMinutes"), criteria.getDureeMinutesMin()
                ));
            }
            
            if (criteria.getDureeMinutesMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("dureeMinutes"), criteria.getDureeMinutesMax()
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
