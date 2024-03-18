package uk.ac.warwick.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import uk.ac.warwick.domain.KPIDayRankings;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyPerformanceIndicatorServiceTest {

    private KeyPerformanceIndicatorService keyPerformanceIndicatorService;

    @BeforeEach
    void setUp() {
        this.keyPerformanceIndicatorService = new KeyPerformanceIndicatorService();
    }

    @Test
    void unmarshallsJsonResourcesIntoObjects() throws IOException {
        var testData = new DefaultResourceLoader().getResource("classpath:data/2023-06-19.json");
        var rankings = keyPerformanceIndicatorService.unmarshallJsonDataFromFile(testData.getFile());
        assertEquals(1, rankings.size());
    }

    @Test
    void mapsRankingObjectsIntoMemory() {
        var rankings = new ArrayList<KPIDayRankings>();
        var testRanking = new KPIDayRankings();
        testRanking.setDate(LocalDate.now());
        rankings.add(testRanking);
        keyPerformanceIndicatorService.mapRankings(rankings);
        assertEquals(1, keyPerformanceIndicatorService.getRankings().size());
    }
}