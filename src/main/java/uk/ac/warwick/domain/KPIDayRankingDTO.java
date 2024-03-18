package uk.ac.warwick.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class KPIDayRankingDTO {
    private LocalDate date;
    private KPIRanking[] wacc;
    private KPIRanking[] scores;
}
