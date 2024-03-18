package uk.ac.warwick.domain;

import lombok.Data;

@Data
public class KPIDayRankingStaffDTO extends KPIDayRankingAcademicDTO {
    private KPIRanking[] employee_engagement;
}
