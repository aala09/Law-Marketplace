package tn.esprit.cloud_in_mypocket.schedulers;

import tn.esprit.cloud_in_mypocket.entity.Consultation;
import tn.esprit.cloud_in_mypocket.service.ConsultationService;
import tn.esprit.cloud_in_mypocket.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ConsultationReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationReminderScheduler.class);

    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private EmailService emailService;

    // Scheduled to run every day at 7 AM (server time)
    @Scheduled(cron = "0 0 7 * * ?")
    public void sendDailyConsultationReminders() {
        logger.info("Executing consultation reminder scheduler...");
        List<Consultation> consultationsToday = consultationService.getConsultationsForToday();
        if (consultationsToday.isEmpty()) {
            logger.info("No consultations scheduled for today.");
            return;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Consultation consultation : consultationsToday) {
            // For demonstration, we'll use static email addresses.
            // In a real-world implementation, retrieve the emails from the related Client and Lawyer entities.
            String clientEmail = "hosnyferjeny@gmail.com";   // Replace with consultation.getDossier().getClient().getEmail() if available.
            String lawyerEmail = "hosnyferjeny@gmail.com";     // Replace with consultation.getDossier().getLawyer().getEmail() if available.

            String subject = "Reminder: Consultation Scheduled Today";
            String text = "Dear User,\n\nThis is a reminder that your consultation on '"
                    + consultation.getSujet() + "' is scheduled for "
                    + consultation.getDateHeure().toLocalTime().format(timeFormatter) + ".\n\nThank you.";

            try {
                //emailService.sendEmail(clientEmail, subject, text);
                //emailService.sendEmail(lawyerEmail, subject, text);
                logger.info("Reminder sent for consultation id: " + consultation.getId());
            } catch (Exception e) {
                logger.error("Failed to send reminder for consultation id: " + consultation.getId(), e);
            }
        }
    }
}
