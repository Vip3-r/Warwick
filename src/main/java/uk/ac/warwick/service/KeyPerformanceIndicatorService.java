package uk.ac.warwick.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import uk.ac.warwick.domain.KPIDayRankings;

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

    public synchronized List<KPIDayRankings> getRankings() {
        return this.rankings.values().stream().toList();
    }
}
