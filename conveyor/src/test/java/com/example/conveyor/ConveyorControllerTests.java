package com.example.conveyor;

import com.example.conveyor.services.serviceImpl.ScoringServiceImpl;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConveyorControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ScoringServiceImpl scoringService;

    JSONObject jsonLoanApplication = new JSONObject();
    JSONObject jsonScoringData = new JSONObject();
    JSONObject employment = new JSONObject();
    LocalDate date = LocalDate.now();
    String loanOffers = null;
    String credit = null;

    @BeforeEach
    void setup() {
        jsonLoanApplication.put("amount", 200000);
        jsonLoanApplication.put("term", 6);
        jsonLoanApplication.put("firstName", "Daniil");
        jsonLoanApplication.put("lastName", "Kalimov");
        jsonLoanApplication.put("middleName", "Valeryevich");
        jsonLoanApplication.put("email", "dkal@mail.com");
        jsonLoanApplication.put("birthday", "1996-03-08");
        jsonLoanApplication.put("passportSeries", "4010");
        jsonLoanApplication.put("passportNumber", "524201");

        employment.put("employmentStatus", "WORKER");
        employment.put("employerINN", "312434134");
        employment.put("position", "WORKER");
        employment.put("workExperienceTotal", "2");
        employment.put("workExperienceCurrent", "6");
        employment.put("salary", "100000");

        jsonScoringData.put("amount", 200000);
        jsonScoringData.put("term", 6);
        jsonScoringData.put("firstName", "Daniil");
        jsonScoringData.put("lastName", "Kalimov");
        jsonScoringData.put("middleName", "Valeryevich");
        jsonScoringData.put("gender", "MAIL");
        jsonScoringData.put("birthdate", "1996-03-08");
        jsonScoringData.put("passportSeries", "4010");
        jsonScoringData.put("passportNumber", "524201");
        jsonScoringData.put("passportIssueDate", "1996-03-08");
        jsonScoringData.put("passportIssueBranch", "100-100");
        jsonScoringData.put("maritalStatus", "SINGLE");
        jsonScoringData.put("dependentAmount", 1);
        jsonScoringData.put("employment", employment);
        jsonScoringData.put("account", "121241121241341412");
        jsonScoringData.put("isInsuranceEnabled", false);
        jsonScoringData.put("isSalaryClient", false);

        loanOffers = "[\n" +
                "  {\n" +
                "    \"totalAmount\": 207120.0,\n" +
                "    \"isSalaryClient\": false,\n" +
                "    \"monthlyPayment\": 34520.0,\n" +
                "    \"rate\": 12,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"term\": 6,\n" +
                "    \"applicationId\": 1,\n" +
                "    \"isInsuranceEnabled\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"totalAmount\": 206520.0,\n" +
                "    \"isSalaryClient\": true,\n" +
                "    \"monthlyPayment\": 34420.0,\n" +
                "    \"rate\": 11,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"term\": 6,\n" +
                "    \"applicationId\": 2,\n" +
                "    \"isInsuranceEnabled\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"totalAmount\": 206346.6,\n" +
                "    \"isSalaryClient\": false,\n" +
                "    \"monthlyPayment\": 34220.0,\n" +
                "    \"rate\": 9,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"term\": 6,\n" +
                "    \"applicationId\": 3,\n" +
                "    \"isInsuranceEnabled\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"totalAmount\": 205743.6,\n" +
                "    \"isSalaryClient\": true,\n" +
                "    \"monthlyPayment\": 34120.0,\n" +
                "    \"rate\": 8,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"term\": 6,\n" +
                "    \"applicationId\": 4,\n" +
                "    \"isInsuranceEnabled\": true\n" +
                "  }\n" +
                "]";

        credit = "{\n" +
                "  \"amount\": 207120.0,\n" +
                "  \"isSalaryClient\": false,\n" +
                "  \"monthlyPayment\": 34520.0,\n" +
                "  \"rate\": 12,\n" +
                "  \"paymentSchedule\": [\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(1) + "\",\n" +
                "      \"interestPayment\": 2071.2,\n" +
                "      \"number\": 1,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 32448.8,\n" +
                "      \"remainingDebt\": 172600.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(2) + "\",\n" +
                "      \"interestPayment\": 1726.0,\n" +
                "      \"number\": 2,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 32794.0,\n" +
                "      \"remainingDebt\": 138080.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(3) + "\",\n" +
                "      \"interestPayment\": 1380.8,\n" +
                "      \"number\": 3,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 33139.2,\n" +
                "      \"remainingDebt\": 103560.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(4) + "\",\n" +
                "      \"interestPayment\": 1035.6,\n" +
                "      \"number\": 4,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 33484.4,\n" +
                "      \"remainingDebt\": 69040.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(5) + "\",\n" +
                "      \"interestPayment\": 690.4,\n" +
                "      \"number\": 5,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 33829.6,\n" +
                "      \"remainingDebt\": 34520.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"date\": \"" + date.plusMonths(6) + "\",\n" +
                "      \"interestPayment\": 345.2,\n" +
                "      \"number\": 6,\n" +
                "      \"totalPayment\": 34520.0,\n" +
                "      \"debtPayment\": 34174.8,\n" +
                "      \"remainingDebt\": 0.0\n" +
                "    }\n" +
                "  ],\n" +
                "  \"psk\": 3.56,\n" +
                "  \"term\": 6,\n" +
                "  \"isInsuranceEnabled\": false\n" +
                "}";

    }

    @Test
    public void getDifferentOffersOfLoanConditions() throws Exception {
        String jsonLoanApplicationString = jsonLoanApplication.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonLoanApplicationString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.error").isEmpty())
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObjectResponce = (JSONObject) jsonParser.parse(responce);
        assertThat(jsonObjectResponce.get("result").toString()).isEqualTo(loanOffers.replaceAll("\\s+", ""));
    }

    @Test
    public void getDifferentOffersOfLoanConditionsWithExceptionValidate() throws Exception {
        jsonLoanApplication.put("amount", 90000);
        String jsonLoanApplicationString = jsonLoanApplication.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonLoanApplicationString))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        assertThat(responce).isEqualTo("Error validation, field amount, amount must be 100000 and more");
    }

    @Test
    public void getCreditTest() throws Exception {
        String jsonScoringDataString = jsonScoringData.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.error").isEmpty())
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObjectResponce = (JSONObject) jsonParser.parse(responce);
        assertThat(jsonObjectResponce.get("result").toString()).isEqualTo(credit.replaceAll("\\s+", ""));
    }

    @Test
    public void getCreditExceptionTest() throws Exception {
        jsonScoringData.put("birthdate", "2010-03-08");
        String jsonScoringDataString = jsonScoringData.toString();
        this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Error validate age"));
    }

    @Test
    public void getCreditExceptionValidationNameTest() throws Exception {
        jsonScoringData.put("firstName", "D");
        String jsonScoringDataString = jsonScoringData.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        assertThat(responce).isEqualTo("Error validation, field firstName, must be from 2 to 30 characters");

    }

    @Test
    public void getCreditExceptionScoringTest() throws Exception {
        jsonScoringData.put("term", 1000000000);
        jsonScoringData.put("amount", 1000000000);
        String jsonScoringDataString = jsonScoringData.toString();
        this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Error scoring"));
    }
}
