package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.dto.TimeSlotDTO;
import tn.esprit.cloud_in_mypocket.entity.User; // Add this import
import tn.esprit.cloud_in_mypocket.service.SchedulingService;
import tn.esprit.cloud_in_mypocket.util.TimeSlot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
  private static final Logger logger = LoggerFactory.getLogger(AvailabilityController.class);
  
  @Autowired 
  private SchedulingService schedulingService;

  @GetMapping("/lawyers/{lawyerId}")
  public ResponseEntity<?> getSlots(
      @PathVariable Long lawyerId,
      @RequestParam @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
  {
    try {
      logger.info("Fetching slots for lawyer ID: {} on date: {}", lawyerId, date);
      
      List<TimeSlot> freeSlots = schedulingService.getFreeSlots(lawyerId, date);
      List<TimeSlotDTO> slots = freeSlots.stream()
          .map(TimeSlotDTO::new)
          .collect(Collectors.toList());
      
      logger.info("Found {} free slots", slots.size());
      return ResponseEntity.ok(slots);
    } catch (RuntimeException e) {
      logger.error("Error fetching availability for lawyer ID {}: {}", lawyerId, e.getMessage(), e);
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }

  @GetMapping("/date")
  public ResponseEntity<?> getAvailableLawyers(
      @RequestParam @DateTimeFormat(iso=ISO.DATE) LocalDate date) 
  {
    try {
      logger.info("Fetching available lawyers for date: {}", date);
      
      List<User> availableLawyers = schedulingService.getAvailableLawyers(date);
      
      logger.info("Found {} available lawyers", availableLawyers.size());
      return ResponseEntity.ok(availableLawyers);
    } catch (RuntimeException e) {
      logger.error("Error fetching available lawyers for date {}: {}", date, e.getMessage(), e);
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }
}
