package uk.ac.warwick.domain;

import lombok.Data;

@Data
public class KPIDayRankingAcademicDTO extends KPIDayRankingDTO {
    private KPIRanking[] factory_utilization;
    private KPIRanking[] interest_coverage;
    private KPIRanking[] marketing_spend_rev;
    private KPIRanking[] e_cars_sales;
    private KPIRanking[] co2_penalty;
}
