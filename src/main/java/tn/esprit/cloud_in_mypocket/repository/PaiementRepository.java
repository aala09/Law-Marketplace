package tn.esprit.cloud_in_mypocket.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.cloud_in_mypocket.entity.Paiement;
import tn.esprit.cloud_in_mypocket.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    //List<Paiement> findByDatePaiementBetween(LocalDate startDate, LocalDate endDate);
    List<Paiement> findByUtilisateurIdAndDatePaiementBetween(Long utilisateurId, LocalDate startDate, LocalDate endDate);
    List<Paiement> findByUtilisateurOrderByDatePaiementDesc(User user);
    @Query("SELECT p FROM Paiement p WHERE p.utilisateur.id = :userId ORDER BY p.datePaiement DESC")
    List<Paiement> findPaymentsByUserIdOrderedByDate(Long userId);

    @Query("SELECT p FROM Paiement p WHERE p.status = 'COMPLETED' AND FUNCTION('DATE_ADD', p.datePaiement, p.packAbonnement.duree, 'DAY') >= CURRENT_DATE")
    List<Paiement> findActiveSubscriptions();
    @Query("SELECT p.packAbonnement.id FROM Paiement p GROUP BY p.packAbonnement.id ORDER BY COUNT(p.id) DESC")
    List<Long> findMostPurchasedPackId(Pageable pageable);

    List<Paiement> findByPackAbonnementId(Long packId);
}
