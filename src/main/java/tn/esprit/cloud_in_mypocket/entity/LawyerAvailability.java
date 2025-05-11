package tn.esprit.cloud_in_mypocket.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "lawyer_availability")
public class LawyerAvailability {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lawyer_id", nullable = false)
    private User lawyer;     // your User entity, where role = "LAWYER"

    // Either a recurring weekly slot…
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;   // MONDAY…SUNDAY

    // …or a one-off date slot
    private LocalDate specificDate;  // nullable if recurring

    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors
    public LawyerAvailability() {
    }

    public LawyerAvailability(User lawyer, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.lawyer = lawyer;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LawyerAvailability(User lawyer, LocalDate specificDate, LocalTime startTime, LocalTime endTime) {
        this.lawyer = lawyer;
        this.specificDate = specificDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getLawyer() {
        return lawyer;
    }

    public void setLawyer(User lawyer) {
        this.lawyer = lawyer;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getSpecificDate() {
        return specificDate;
    }

    public void setSpecificDate(LocalDate specificDate) {
        this.specificDate = specificDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
