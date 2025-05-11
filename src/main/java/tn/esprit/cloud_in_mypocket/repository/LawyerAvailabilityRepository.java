package tn.esprit.cloud_in_mypocket.repository;

import tn.esprit.cloud_in_mypocket.entity.LawyerAvailability;
import tn.esprit.cloud_in_mypocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LawyerAvailabilityRepository extends JpaRepository<LawyerAvailability, Long> {

    List<LawyerAvailability> findByLawyerAndDayOfWeek(User lawyer, DayOfWeek dow);
    List<LawyerAvailability> findByLawyerAndSpecificDate(User lawyer, LocalDate date);
}
