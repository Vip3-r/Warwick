package uk.ac.warwick.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.warwick.domain.KPIDayRankingDTO;
import uk.ac.warwick.security.AuthorityGroup;
import uk.ac.warwick.service.KeyPerformanceIndicatorService;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
public class RankingsController {

    private final KeyPerformanceIndicatorService service;

    @GetMapping("/rankings")
    public ResponseEntity<List<KPIDayRankingDTO>> rankings(
            Authentication authentication
    ) {
        return ResponseEntity.ok(service.getRankings((Collection<AuthorityGroup>) authentication.getAuthorities()));
    }

}
