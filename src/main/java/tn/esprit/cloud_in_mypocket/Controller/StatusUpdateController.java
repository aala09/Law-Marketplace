package tn.esprit.cloud_in_mypocket.Controller;

import tn.esprit.cloud_in_mypocket.schedulers.ConsultationStatusUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status-update")
@CrossOrigin(origins = "http://localhost:4200")
public class StatusUpdateController {

    @Autowired
    private ConsultationStatusUpdater statusUpdater;

    // Endpoint to manually trigger the status update for consultations
    @GetMapping("/trigger")
    public String triggerStatusUpdate() {
        statusUpdater.updateConsultationStatuses();
        return "Consultation status update task triggered manually.";
    }
}
