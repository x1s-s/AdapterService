package by.x1ss.adapterservice;

import by.x1ss.adapterservice.configuration.LinksToOtherService;
import by.x1ss.adapterservice.controller.AdapterController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
class AdapterServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LinksToOtherService links;

    private MockRestServiceServer mockServer;

    @Autowired
    private AdapterController adapterController;

    @BeforeEach
    public void createMock() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators
                        .withStatus(HttpStatus.OK)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
    }


    @Test
    void integrationTestJuridical() throws Exception {
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<Response>\n" +
                                "\t<clientIdentifier>1234567890</clientIdentifier>\n" +
                                "\t<accrualAmount>2.5</accrualAmount>\n" +
                                "\t<amountPay>2.0</amountPay>\n" +
                                "\t<resolutionNumber>1234.0</resolutionNumber>\n" +
                                "\t<resolutionDate>2023-02-02</resolutionDate>\n" +
                                "\t<administrativeCode>123</administrativeCode>\n" +
                                "</Response>"));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "http://localhost:8080/adapter/answer/juridical/1234567890"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.xpath("Response/clientIdentifier").string("1234567890"))
                .andExpect(MockMvcResultMatchers.xpath("Response/accrualAmount").number(2.5))
                .andExpect(MockMvcResultMatchers.xpath("Response/amountPay").number(2.0))
                .andExpect(MockMvcResultMatchers.xpath("Response/resolutionNumber").number(1234.0))
                .andExpect(MockMvcResultMatchers.xpath("Response/resolutionDate").string("2023-02-02"))
                .andExpect(MockMvcResultMatchers.xpath("Response/administrativeCode").string("123"));
        mockServer.verify();

    }

    @Test
    void integrationTestPhysical() throws Exception{
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<Response>\n" +
                                "\t<clientIdentifier>А111АА00</clientIdentifier>\n" +
                                "\t<accrualAmount>5.5</accrualAmount>\n" +
                                "\t<amountPay>5.0</amountPay>\n" +
                                "\t<resolutionNumber>56789.0</resolutionNumber>\n" +
                                "\t<resolutionDate>2021-01-01</resolutionDate>\n" +
                                "\t<administrativeCode>456</administrativeCode>\n" +
                                "</Response>"));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "http://localhost:8080/adapter/answer/physical").content("<inn>А111АА00</inn>"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.xpath("Response/clientIdentifier").string("А111АА00"))
                .andExpect(MockMvcResultMatchers.xpath("Response/accrualAmount").number(5.5))
                .andExpect(MockMvcResultMatchers.xpath("Response/amountPay").number(5.0))
                .andExpect(MockMvcResultMatchers.xpath("Response/resolutionNumber").number(56789.0))
                .andExpect(MockMvcResultMatchers.xpath("Response/resolutionDate").string("2021-01-01"))
                .andExpect(MockMvcResultMatchers.xpath("Response/administrativeCode").string("456"));

        mockServer.verify();
    }

    @Test
    void testValidationJuridical() {
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
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
        assertThrows(ConstraintViolationException.class, () -> adapterController.getJuridicalAnswer("123456789"));
        assertThrows(ConstraintViolationException.class, () -> adapterController.getJuridicalAnswer("12345678009"));
        adapterController.getJuridicalAnswer("1234567890");
        mockServer.verify();
    }

    @Test
    void testValidationPhysical(){
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer()+"[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
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
        assertThrows(ConstraintViolationException.class, () -> adapterController.getPhysicalAnswer("А111АА00000"));
        assertThrows(ConstraintViolationException.class, () -> adapterController.getPhysicalAnswer("ААА00000"));
        adapterController.getPhysicalAnswer("А111АА00");
        mockServer.verify();
    }

    @Test
    void contextLoads() {
    }

}
