package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFeaturesDTO {
    private int paymentCount;
    private double totalSpent;
    private double avgPayment;
    private int subscriptionCount;
    private String lastPaymentDate;
    private String preferredPaymentMethod;
    private boolean isYearlySubscriber;
} 