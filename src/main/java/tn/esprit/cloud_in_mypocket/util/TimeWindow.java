package tn.esprit.cloud_in_mypocket.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;

public class TimeWindow {
    private LocalTime start;
    private LocalTime end;

    public TimeWindow(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public int getDurationMinutes() {
        return (int) Duration.between(start, end).toMinutes();
    }

    // Static factory method to create a TimeWindow from a LocalDateTime and duration
    public static TimeWindow of(LocalDateTime dateTime, int durationMinutes) {
        LocalTime start = dateTime.toLocalTime();
        LocalTime end = start.plusMinutes(durationMinutes);
        return new TimeWindow(start, end);
    }

    // Check if this window overlaps with another
    public boolean overlaps(TimeWindow other) {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }

    // Check if this window contains a specific time
    public boolean contains(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }
}
