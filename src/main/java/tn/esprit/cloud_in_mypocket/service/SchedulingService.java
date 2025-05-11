package tn.esprit.cloud_in_mypocket.service;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.entity.LawyerAvailability;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.ConsultationRepository;
import tn.esprit.cloud_in_mypocket.repository.LawyerAvailabilityRepository;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;
import tn.esprit.cloud_in_mypocket.util.TimeWindow;
import tn.esprit.cloud_in_mypocket.util.TimeSlot;
import tn.esprit.cloud_in_mypocket.util.TimeSlotCalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SchedulingService {
  @Autowired 
  private LawyerAvailabilityRepository availRepo;
  
  @Autowired 
  private ConsultationRepository consultRepo;
  
  @Autowired 
  private UserRepository userRepo; // to load lawyers

  // 1) For a given lawyer + date, produce free slots
  public List<TimeSlot> getFreeSlots(Long lawyerId, LocalDate date) {
    User lawyer = userRepo.findById(lawyerId)
                  .orElseThrow(() -> new RuntimeException("Lawyer not found"));
    DayOfWeek dow = date.getDayOfWeek();

    // recurring + one‑off
    List<LawyerAvailability> rec = availRepo.findByLawyerAndDayOfWeek(lawyer, dow);
    List<LawyerAvailability> oneoffs = availRepo.findByLawyerAndSpecificDate(lawyer, date);

    // Merge both lists into windows…
    List<TimeWindow> windows = Stream.concat(rec.stream(), oneoffs.stream())
        .map(av -> new TimeWindow(av.getStartTime(), av.getEndTime()))
        .collect(Collectors.toList());

    // Subtract consultations already booked that day
    List<Consultation> booked = consultRepo
      .findAllByLawyerAndSlotStartBetween(lawyer,
          date.atStartOfDay(), date.atTime(LocalTime.MAX));

    return TimeSlotCalculator.subtract(windows, booked, 
            av -> av.getSlotStart().toLocalTime(),
            c -> TimeWindow.of(c.getSlotStart(), c.getDureeMinutes()));
  }

  // Get all lawyers available on a specific date
  public List<User> getAvailableLawyers(LocalDate date) {
    // Get all lawyers
    List<User> allLawyers = userRepo.findByRole("LAWYER");
    DayOfWeek dow = date.getDayOfWeek();
    
    // Filter lawyers who have availability on this date or day of week
    return allLawyers.stream()
        .filter(lawyer -> {
          // Check if lawyer has recurring availability on this day of week
          List<LawyerAvailability> recAvailability = availRepo.findByLawyerAndDayOfWeek(lawyer, dow);
          
          // Check if lawyer has specific availability for this date
          List<LawyerAvailability> specificAvailability = availRepo.findByLawyerAndSpecificDate(lawyer, date);
          
          // Lawyer is available if they have any availability records for this date/day
          return !recAvailability.isEmpty() || !specificAvailability.isEmpty();
        })
        .collect(Collectors.toList());
  }

  // 2) Book a slot
  public Consultation bookSlot(Long lawyerId, Long clientId,
      LocalDateTime start, int duration)  
  {
    User lawyer = userRepo.findById(lawyerId)
                  .orElseThrow(() -> new RuntimeException("Lawyer not found"));
    User client = userRepo.findById(clientId)
                  .orElseThrow(() -> new RuntimeException("Client not found"));

    // Check slot is free
    if (consultRepo.existsByLawyerAndSlotStartAndStatusNot(lawyer, start, "CANCELLED"))
      throw new RuntimeException("Slot taken");

    Consultation c = new Consultation();
    c.setLawyer(lawyer);
    c.setClient(client);
    c.setSlotStart(start);
    c.setDureeMinutes(duration);
    c.setStatus("PLANIFIEE");
    c.setSujet("Consultation"); // Default subject
    return consultRepo.save(c);
  }
}
