package tn.esprit.cloud_in_mypocket.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class TimeSlotCalculator {
    
    // Default slot duration in minutes
    private static final int DEFAULT_SLOT_MINUTES = 30;

    /**
     * Subtracts booked times from available windows and returns list of available slots
     * 
     * @param availableWindows List of available time windows
     * @param bookedItems List of booked items
     * @param startTimeExtractor Function to extract start time from booked item
     * @param windowExtractor Function to convert booked item to a TimeWindow
     * @param <T> Type of booked item
     * @return List of available time slots
     */
    public static <T> List<TimeSlot> subtract(
            List<TimeWindow> availableWindows, 
            List<T> bookedItems,
            Function<T, LocalTime> startTimeExtractor,
            Function<T, TimeWindow> windowExtractor) {
        
        return subtract(availableWindows, bookedItems, startTimeExtractor, windowExtractor, DEFAULT_SLOT_MINUTES);
    }

    /**
     * Subtracts booked times from available windows and returns list of available slots with custom slot duration
     */
    public static <T> List<TimeSlot> subtract(
            List<TimeWindow> availableWindows, 
            List<T> bookedItems,
            Function<T, LocalTime> startTimeExtractor,
            Function<T, TimeWindow> windowExtractor,
            int slotDurationMinutes) {
        
        List<TimeSlot> availableSlots = new ArrayList<>();
        
        // Sort the available windows by start time
        availableWindows.sort(Comparator.comparing(TimeWindow::getStart));
        
        // For each available window, find the slots
        for (TimeWindow window : availableWindows) {
            LocalTime current = window.getStart();
            LocalTime end = window.getEnd();
            
            while (current.plusMinutes(slotDurationMinutes).compareTo(end) <= 0) {
                final LocalTime slotStart = current;
                final LocalTime slotEnd = current.plusMinutes(slotDurationMinutes);
                
                // Check if this slot is booked
                boolean isBooked = bookedItems.stream()
                    .anyMatch(item -> {
                        TimeWindow bookedWindow = windowExtractor.apply(item);
                        return timeOverlaps(slotStart, slotEnd, bookedWindow.getStart(), bookedWindow.getEnd());
                    });
                
                if (!isBooked) {
                    availableSlots.add(new TimeSlot(slotStart, slotEnd, true));
                }
                
                current = current.plusMinutes(slotDurationMinutes);
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Checks if two time ranges overlap
     */
    private static boolean timeOverlaps(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !end1.isBefore(start2) && !start1.isAfter(end2);
    }
}
