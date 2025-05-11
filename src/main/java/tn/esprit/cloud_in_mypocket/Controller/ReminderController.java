package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.schedulers.ConsultationReminderScheduler;
import tn.esprit.cloud_in_mypocket.schedulers.ConsultationStatusUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "http://localhost:4200")
public class ReminderController {

    @Autowired
    private ConsultationReminderScheduler reminderScheduler;

    @Autowired
    private ConsultationStatusUpdater statusUpdater;

    // Endpoint to manually trigger the reminder scheduler for testing
    @GetMapping("/trigger")
    public String triggerReminders() {
        reminderScheduler.sendDailyConsultationReminders();
        return "Consultation reminder task triggered manually.";
    }


}
