package tn.esprit.cloud_in_mypocket.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.cloud_in_mypocket.dto.IaDto.*;

import tn.esprit.cloud_in_mypocket.service.AIDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AIDashboardController {
    private final AIDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<AnalysisDashboardDTO> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @GetMapping("/users/{userId}/analysis")
    public ResponseEntity<AnalysisResultDTO> getUserAnalysis(@PathVariable Long userId) {
        AnalysisResultDTO analysis = dashboardService.fetchUserAnalysis(userId);
        if (analysis == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(analysis);
    }
} 