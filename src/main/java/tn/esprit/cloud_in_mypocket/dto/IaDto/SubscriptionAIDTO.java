package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionAIDTO {
    private Long subscriptionId;
    private Long userId;
    private Long packId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
} 