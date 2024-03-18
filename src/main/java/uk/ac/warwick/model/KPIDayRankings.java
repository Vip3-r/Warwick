package uk.ac.warwick.model;

import lombok.Data;
import uk.ac.warwick.domain.KPIRanking;

import java.time.LocalDate;

@Data
public class KPIDayRankings {

    private LocalDate date;
    private KPIRanking[] wacc;
    private KPIRanking[] factory_utilization;
    private KPIRanking[] scores;
    private KPIRanking[] employee_engagement;
    private KPIRanking[] interest_coverage;
    private KPIRanking[] marketing_spend_rev;
    private KPIRanking[] e_cars_sales;
    private KPIRanking[] co2_penalty;

}
