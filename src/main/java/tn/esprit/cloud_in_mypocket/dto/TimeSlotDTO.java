package tn.esprit.cloud_in_mypocket.dto;

import tn.esprit.cloud_in_mypocket.util.TimeSlot;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlotDTO {
    private String startTime;
    private String endTime;
    private boolean available;
    
    // For serialization
    public TimeSlotDTO() {
    }
    
    public TimeSlotDTO(TimeSlot timeSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.startTime = timeSlot.getStartTime().format(formatter);
        this.endTime = timeSlot.getEndTime().format(formatter);
        this.available = timeSlot.isAvailable();
    }

    // Getters and Setters
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
