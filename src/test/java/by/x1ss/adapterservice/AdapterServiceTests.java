package by.x1ss.adapterservice;

import by.x1ss.adapterservice.domain.logic.interactionWithSMEV.service.configuration.propertiesConfig.LinksToOtherService;
import by.x1ss.adapterservice.domain.object.Response;
import by.x1ss.adapterservice.domain.object.ResponseList;
import by.x1ss.adapterservice.web.controller.AdapterController;
import by.x1ss.adapterservice.web.dto.ResponseListDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
class AdapterServiceTests {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LinksToOtherService links;

    private MockRestServiceServer mockServer;

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private AdapterController adapterController;

    @BeforeEach
    public void createMock() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    void integrationPositiveTestJuridicalXML() throws Exception {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("1234567890")
                        .accrualAmount(2.5)
                        .amountPay(2)
                        .resolutionNumber(1234)
                        .resolutionDate(LocalDate.of(2023, 2, 2))
                        .administrativeCode("123")
                        .isJuridical(true)
                        .build()
        );
        responses.add(
                Response.builder()
                        .clientIdentifier("1234567890")
                        .accrualAmount(4.5)
                        .amountPay(4)
                        .resolutionNumber(4)
                        .resolutionDate(LocalDate.of(2023, 4, 4))
                        .administrativeCode("4")
                        .isJuridical(true)
                        .build()
        );
        responseList.setResponses(responses);
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(xmlMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/juridical")
                        .content("1234567890")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(xmlMapper.writeValueAsString(new ResponseListDTO(responseList))));
        mockServer.verify();
    }

    @Test
    void integrationPositiveTestJuridicalJSON() throws Exception {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("1234567890")
                        .accrualAmount(2.5)
                        .amountPay(2)
                        .resolutionNumber(1234)
                        .resolutionDate(LocalDate.of(2023, 2, 2))
                        .administrativeCode("123")
                        .isJuridical(true)
                        .build()
        );
        responses.add(
                Response.builder()
                        .clientIdentifier("1234567890")
                        .accrualAmount(4.5)
                        .amountPay(4)
                        .resolutionNumber(4)
                        .resolutionDate(LocalDate.of(2023, 4, 4))
                        .administrativeCode("4")
                        .isJuridical(true)
                        .build()
        );
        responseList.setResponses(responses);
        System.out.println(objectMapper.writeValueAsString(responseList));
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/juridical")
                        .content("1234567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ResponseListDTO(responseList))));
        mockServer.verify();
    }

    @Test
    void integrationPositiveTestPhysicalXML() throws Exception {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("А111АА00")
                        .accrualAmount(5.5)
                        .amountPay(5)
                        .resolutionNumber(56789)
                        .resolutionDate(LocalDate.of(2021, 1, 1))
                        .administrativeCode("456")
                        .isJuridical(false)
                        .build()
        );
        responseList.setResponses(responses);
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_XML)
                        .body(xmlMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/physical")
                        .content("А111АА00")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().xml(xmlMapper.writeValueAsString(new ResponseListDTO(responseList))));
    }

    @Test
    void integrationPositiveTestPhysicalJSON() throws Exception {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("А111АА00")
                        .accrualAmount(5.5)
                        .amountPay(5)
                        .resolutionNumber(56789)
                        .resolutionDate(LocalDate.of(2021, 1, 1))
                        .administrativeCode("456")
                        .isJuridical(false)
                        .build()
        );
        responseList.setResponses(responses);
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/physical")
                        .content("А111АА00")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ResponseListDTO(responseList))));
    }

    @Test
    void integrationNegativeTestServiceUnavailable() throws Exception {
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/physical")
                        .content("А111АА00")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    void integrationNegativeTestNoContent() throws Exception {
        mockServer.expect(once(), requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NO_CONTENT));
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/adapter/answer/physical")
                        .content("А111АА00")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testValidationJuridical() throws JsonProcessingException {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("1234567890")
                        .accrualAmount(2.5)
                        .amountPay(2)
                        .resolutionNumber(1234)
                        .resolutionDate(LocalDate.of(2023, 2, 2))
                        .administrativeCode("123")
                        .isJuridical(true)
                        .build()
        );
        responseList.setResponses(responses);
        mockServer.expect(ExpectedCount.times(3),requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK));
        assertThrows(ConstraintViolationException.class, () -> adapterController.getJuridicalAnswer("123456789"));
        assertThrows(ConstraintViolationException.class, () -> adapterController.getJuridicalAnswer("12345678009"));
        adapterController.getJuridicalAnswer("1234567890");
        mockServer.verify();
    }

    @Test
    void testValidationPhysical() throws JsonProcessingException {
        ResponseList responseList = new ResponseList();
        List<Response> responses = new ArrayList<>();
        responses.add(
                Response.builder()
                        .clientIdentifier("А111АА00")
                        .accrualAmount(5.5)
                        .amountPay(5)
                        .resolutionNumber(56789)
                        .resolutionDate(LocalDate.of(2021, 1, 1))
                        .administrativeCode("456")
                        .isJuridical(false)
                        .build()
        );
        responseList.setResponses(responses);
        mockServer.expect(ExpectedCount.times(3),requestTo(links.getStatus()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":\"UP\"}"));
        mockServer.expect(once(), requestTo(links.getRequest()))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.ACCEPTED));
        mockServer.expect(once(), requestTo(matchesPattern(links.getGetAnswer() + ".*")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(responseList)));
        mockServer.expect(once(), requestTo(matchesPattern(links.getConfirm() + ".*")))
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
