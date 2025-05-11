package tn.esprit.cloud_in_mypocket.specification;

import tn.esprit.cloud_in_mypocket.dto.search.DossierSearchCriteria;
import tn.esprit.cloud_in_mypocket.entity.Dossier;
import tn.esprit.cloud_in_mypocket.entity.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

import java.util.ArrayList;
import java.util.List;

public class DossierSpecification {
    
    public static Specification<Dossier> buildSpecification(DossierSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            
            if (criteria.getReference() != null && !criteria.getReference().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("reference")), 
                    "%" + criteria.getReference().toLowerCase() + "%"
                ));
            }
            
            if (criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }
            
            if (criteria.getClientId() != null) {
                Join<Dossier, User> clientJoin = root.join("client");
                predicates.add(criteriaBuilder.equal(clientJoin.get("id"), criteria.getClientId()));
            }
            
            if (criteria.getLawyerId() != null) {
                Join<Dossier, User> lawyerJoin = root.join("lawyer");
                predicates.add(criteriaBuilder.equal(lawyerJoin.get("id"), criteria.getLawyerId()));
            }
            
            if (criteria.getDateCreationStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dateCreation"), criteria.getDateCreationStart()
                ));
            }
            
            if (criteria.getDateCreationEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("dateCreation"), criteria.getDateCreationEnd()
                ));
            }
            
            if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
                // If dossier has a status field, otherwise you might need to join with contrat
                // and check its status
                Join<Dossier, Object> contratJoin = root.join("contrat");
                predicates.add(criteriaBuilder.equal(
                    contratJoin.get("status"), criteria.getStatus()
                ));
            }
            
            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
