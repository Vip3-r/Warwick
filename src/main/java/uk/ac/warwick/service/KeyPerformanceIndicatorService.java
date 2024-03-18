package uk.ac.warwick.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.ac.warwick.domain.KPIDayRankingAcademicDTO;
import uk.ac.warwick.domain.KPIDayRankingDTO;
import uk.ac.warwick.domain.KPIDayRankingStaffDTO;
import uk.ac.warwick.model.KPIDayRankings;
import uk.ac.warwick.security.AuthorityGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class KeyPerformanceIndicatorService {

    private final Map<LocalDate, KPIDayRankings> rankings = Collections.synchronizedMap(new HashMap<>());

    @Value("classpath:data/*.json")
    Resource[] resources;

    @PostConstruct
    private void loadData() {
        synchronized (rankings) {
            var files = Arrays.stream(resources).map(resource -> {
                try {
                    return resource.getFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull).toList().toArray(File[]::new);

            mapRankings(unmarshallJsonDataFromFile(files));
        }
    }

    protected List<KPIDayRankings> unmarshallJsonDataFromFile(File... files) {
        var mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.getDefault());

        return Arrays.stream(files).map(file -> {
            try {
                var rankings = mapper.readValue(new FileInputStream(file), KPIDayRankings.class);
                rankings.setDate(LocalDate.parse(file.getName().substring(0, file.getName().length() - 5), formatter));
                return rankings;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).toList();
    }

    protected void mapRankings(List<KPIDayRankings> rankings) {
        rankings.forEach(ranking -> this.rankings.put(ranking.getDate(), ranking));
    }

    public synchronized List<KPIDayRankingDTO> getRankings(Collection<AuthorityGroup> authorities) {
        final boolean isStaff = authorities.contains(AuthorityGroup.STAFF);
        final boolean isAcademic = authorities.contains(AuthorityGroup.ACADEMIC);

        return this.rankings.values().stream().map(ranking -> {
            KPIDayRankingDTO dto;
            if (isStaff) {
                dto = new KPIDayRankingStaffDTO();
                dto.setDate(ranking.getDate());
                dto.setWacc(ranking.getWacc());
                dto.setScores(ranking.getScores());
                var staff = ((KPIDayRankingStaffDTO) dto);
                staff.setFactory_utilization(ranking.getFactory_utilization());
                staff.setInterest_coverage(ranking.getInterest_coverage());
                staff.setMarketing_spend_rev(ranking.getMarketing_spend_rev());
                staff.setE_cars_sales(ranking.getE_cars_sales());
                staff.setCo2_penalty(ranking.getCo2_penalty());
                staff.setEmployee_engagement(ranking.getEmployee_engagement());
            } else if (isAcademic) {
                dto = new KPIDayRankingAcademicDTO();
                dto.setDate(ranking.getDate());
                dto.setWacc(ranking.getWacc());
                dto.setScores(ranking.getScores());
                var academic = ((KPIDayRankingAcademicDTO) dto);
                academic.setFactory_utilization(ranking.getFactory_utilization());
                academic.setInterest_coverage(ranking.getInterest_coverage());
                academic.setMarketing_spend_rev(ranking.getMarketing_spend_rev());
                academic.setE_cars_sales(ranking.getE_cars_sales());
                academic.setCo2_penalty(ranking.getCo2_penalty());
            } else {
                dto = new KPIDayRankingDTO();
                dto.setDate(ranking.getDate());
                dto.setWacc(ranking.getWacc());
                dto.setScores(ranking.getScores());
            }
            return dto;
        }).toList();
    }
}
