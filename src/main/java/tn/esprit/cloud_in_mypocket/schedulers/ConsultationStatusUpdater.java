package tn.esprit.cloud_in_mypocket.schedulers;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.service.ConsultationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsultationStatusUpdater {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationStatusUpdater.class);

    @Autowired
    private ConsultationService consultationService;

    /**
     * This method updates the status of consultations that have a scheduled time in the past.
     * It runs every hour. (Adjust the cron expression or fixedDelay as needed.)
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void updateConsultationStatuses() {
        logger.info("Updating consultation statuses...");
        consultationService.updateExpiredConsultationStatuses();
        logger.info("Consultation status update complete.");
    }
}
