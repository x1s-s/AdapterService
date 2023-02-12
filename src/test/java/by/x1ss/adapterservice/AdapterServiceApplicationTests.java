package by.x1ss.adapterservice;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdapterServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private LinksToOtherService links;


    private MockRestServiceServer mockServer;

    @BeforeEach
    public void createMock() {
        mockServer = MockRestServiceServer.createServer(new RestTemplate());
        mockServer.expect(requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators
                        .withStatus(HttpStatus.OK)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .body("PROCESSING"));

    }




    @Test
    void integrationTestJuridical() throws Exception {
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .body(
                                "{" +
                                        "\"clientIdentifier\": \"1234567890\",\n" +
                                        "\"accrualAmount\": -2.054162789E9,\n" +
                                        "\"amountPay\": -2.054162789E9,\n" +
                                        "\"resolutionNumber\": -2.054162789E9,\n" +
                                        "\"resolutionDate\": \"+169104628-12-09\",\n" +
                                        "\"administrativeCode\": \"-2.054162789E9\"\n" +
                                        "}"));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "http://localhost:8080/adapter/answer/juridical/1234567890"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientIdentifier").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accrualAmount").value(-2.054162789E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountPay").value(-2.054162789E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resolutionNumber").value(-2.054162789E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resolutionDate").value("+169104628-12-09"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.administrativeCode").value("-2.054162789E9"));
    }

    @Test
    void integrationTestPhysical() throws Exception{
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .body(
                                "{" +
                                        "\"clientIdentifier\": \"А111АА00\",\n" +
                                        "\"accrualAmount\": 2.064621441E9,\n" +
                                        "\"amountPay\": 2.064621441E9,\n" +
                                        "\"resolutionNumber\": 2.064621441E9,\n" +
                                        "\"resolutionDate\": \"+169104628-12-09\",\n" +
                                        "\"administrativeCode\": \"2.064621441E9\"\n" +
                                        "}"));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "http://localhost:8080/adapter/answer/physical/А111АА00"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientIdentifier").value("А111АА00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accrualAmount").value(2.064621441E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountPay").value(2.064621441E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resolutionNumber").value(2.064621441E9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resolutionDate").value("+169104628-12-09"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.administrativeCode").value("2.064621441E9"));

    }

    @Test
    void contextLoads() {
    }

}
