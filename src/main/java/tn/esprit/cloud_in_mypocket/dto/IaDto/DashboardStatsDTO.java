package tn.esprit.cloud_in_mypocket.dto.IaDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private int totalUsers;
    private double totalRevenue;
    private Map<String, Integer> spendingCategoryDistribution;
    private Map<String, Integer> recommendationTypeDistribution;
    private double averageLoyaltyScore;
} 