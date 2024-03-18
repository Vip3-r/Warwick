package uk.ac.warwick.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import uk.ac.warwick.WarwickApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    classes = { WarwickApplication.class },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class RankingsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void responds200WithData() {
        var responseEntity = restTemplate.getForEntity("/rankings", List.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

}