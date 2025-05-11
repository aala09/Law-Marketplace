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
public class AnalysisDashboardDTO {
    private List<UserAnalysisDTO> userAnalyses;
    private DashboardStatsDTO stats;
    private List<RecommendationSummaryDTO> recommendationSummary;
} 