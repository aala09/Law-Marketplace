package tn.esprit.cloud_in_mypocket.dto;

import java.time.LocalDateTime;

public class BookingRequest {
    private Long lawyerId;
    private Long clientId;
    private LocalDateTime slotStart;
    private Integer duration;

    // Default constructor for deserialization
    public BookingRequest() {
    }

    public BookingRequest(Long lawyerId, Long clientId, LocalDateTime slotStart, Integer duration) {
        this.lawyerId = lawyerId;
        this.clientId = clientId;
        this.slotStart = slotStart;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Long lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(LocalDateTime slotStart) {
        this.slotStart = slotStart;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
