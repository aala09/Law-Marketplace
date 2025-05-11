package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAIDTO {
    private Long paymentId;
    private Long userId;
    private Long packId;
    private Double montant;
    private String methode;
    private LocalDateTime datePaiement;
    private String status;
    private Boolean isYearly;
} 