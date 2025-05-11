package tn.esprit.cloud_in_mypocket.dto.IaDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BehaviorAnalysisDTO {
    @JsonProperty("loyalty_score")
    private int loyaltyScore;

    @JsonProperty("payment_consistency")
    private String paymentConsistency;

    @JsonProperty("preferred_subscription_type")
    private String preferredSubscriptionType;

    @JsonProperty("spending_category")
    private String spendingCategory;
} 