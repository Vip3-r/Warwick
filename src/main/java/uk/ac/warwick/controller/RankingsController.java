package uk.ac.warwick.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.warwick.domain.KPIDayRankings;
import uk.ac.warwick.service.KeyPerformanceIndicatorService;

import java.util.List;

@RestController
@AllArgsConstructor
public class RankingsController {

    private final KeyPerformanceIndicatorService service;

    @GetMapping("/rankings")
    public ResponseEntity<List<KPIDayRankings>> rankings() {
        return ResponseEntity.ok(service.getRankings());
    }

}
