package tn.esprit.cloud_in_mypocket.dto.IaDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalysisResultDTO {
    @JsonProperty("analysis_date")
    private String analysisDate;

    @JsonProperty("behavior_analysis")
    private BehaviorAnalysisDTO behaviorAnalysis;

    private UserFeaturesDTO features;

    private List<RecommendationDTO> recommendations;

    @JsonProperty("user_id")
    private Long userId;
} 