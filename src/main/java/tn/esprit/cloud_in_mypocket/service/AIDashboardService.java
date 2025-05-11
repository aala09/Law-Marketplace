package tn.esprit.cloud_in_mypocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.cloud_in_mypocket.dto.IaDto.*;
import tn.esprit.cloud_in_mypocket.entity.User;
import tn.esprit.cloud_in_mypocket.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIDashboardService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${flask.api.url}")
    private String flaskApiUrl;

    public AnalysisDashboardDTO getDashboardData() {
        List<User> users = userRepository.findAll();

        List<UserAnalysisDTO> userAnalyses = users.stream()
                .map(this::analyzeUserSafely)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new AnalysisDashboardDTO(
                userAnalyses,
                calculateDashboardStats(userAnalyses),
                generateRecommendationSummary(userAnalyses)
        );
    }

    private UserAnalysisDTO analyzeUserSafely(User user) {
        try {
            AnalysisResultDTO flaskData = fetchUserAnalysis(user.getId());
            if (flaskData == null) {
                System.err.printf("⚠️ No analysis data received for user %d%n", user.getId());
                return null;
            }

            UserFeaturesDTO features = flaskData.getFeatures();
            BehaviorAnalysisDTO behaviorAnalysis = flaskData.getBehaviorAnalysis();

            if (features == null || behaviorAnalysis == null) {
                System.err.printf("⚠️ Incomplete analysis data for user %d%n", user.getId());
                return null;
            }

            return new UserAnalysisDTO(
                    user.getId(),
                    user.getNom() + " " + user.getPrenom(),
                    user.getEmail(),
                    features.getTotalSpent(),
                    behaviorAnalysis.getLoyaltyScore(),
                    behaviorAnalysis.getSpendingCategory(),
                    flaskData.getRecommendations()
            );
        } catch (Exception e) {
            System.err.printf("❌ Error analyzing user %d: %s%n", user.getId(), e.getMessage());
            return null;
        }
    }

    public AnalysisResultDTO fetchUserAnalysis(Long userId) {
        String url = flaskApiUrl + "/api/users/" + userId + "/analysis";
        return restTemplate.getForObject(url, AnalysisResultDTO.class);
    }

    private DashboardStatsDTO calculateDashboardStats(List<UserAnalysisDTO> userAnalyses) {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalUsers(userAnalyses.size());
        stats.setTotalRevenue(userAnalyses.stream()
                .mapToDouble(UserAnalysisDTO::getTotalSpent)
                .sum());

        stats.setSpendingCategoryDistribution(
                userAnalyses.stream()
                        .filter(u -> u.getSpendingCategory() != null)
                        .collect(Collectors.groupingBy(
                                UserAnalysisDTO::getSpendingCategory,
                                Collectors.reducing(0, e -> 1, Integer::sum)
                        ))
        );

        stats.setRecommendationTypeDistribution(
                userAnalyses.stream()
                        .flatMap(u -> u.getRecommendations().stream())
                        .collect(Collectors.groupingBy(
                                RecommendationDTO::getType,
                                Collectors.reducing(0, e -> 1, Integer::sum)
                        ))
        );

        stats.setAverageLoyaltyScore(
                userAnalyses.stream()
                        .mapToInt(UserAnalysisDTO::getLoyaltyScore)
                        .average()
                        .orElse(0.0)
        );

        return stats;
    }

    private List<RecommendationSummaryDTO> generateRecommendationSummary(List<UserAnalysisDTO> userAnalyses) {
        return userAnalyses.stream()
                .flatMap(u -> u.getRecommendations().stream())
                .collect(Collectors.groupingBy(
                        rec -> rec.getType() + "_" + rec.getPriority(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    String[] keyParts = entry.getKey().split("_", 2);
                    String type = keyParts[0];
                    String priority = keyParts.length > 1 ? keyParts[1] : "unknown";

                    RecommendationSummaryDTO summary = new RecommendationSummaryDTO();
                    summary.setType(type);
                    summary.setPriority(priority);
                    summary.setCount(entry.getValue().intValue());
                    return summary;
                })
                .collect(Collectors.toList());
    }
}
