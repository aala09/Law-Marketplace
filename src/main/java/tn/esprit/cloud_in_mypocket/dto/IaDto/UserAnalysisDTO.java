package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnalysisDTO {
    private Long userId;
    private String userName;
    private String email;
    private double totalSpent;
    private int paymentCount;
    private String spendingCategory;
    private int loyaltyScore;
    private List<RecommendationDTO> recommendations;

    public UserAnalysisDTO(Long userId, String userName, String email, double totalSpent, int loyaltyScore, String spendingCategory, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.totalSpent = totalSpent;
        this.loyaltyScore = loyaltyScore;
        this.spendingCategory = spendingCategory;
        this.recommendations = recommendations;
    }
}