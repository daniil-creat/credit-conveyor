package com.example.conveyor;

import com.example.conveyor.service.impl.CreditServiceImpl;
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
    private CreditServiceImpl scoringService;

    JSONObject jsonLoanApplication = new JSONObject();
    JSONObject jsonScoringData = new JSONObject();
    JSONObject employment = new JSONObject();
    LocalDate date = LocalDate.now();
    String loanOffers = null;
    String credit = null;
    String creditEmpty = null;

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
        jsonScoringData.put("gender", "MALE");
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
                "    \"applicationId\": 1,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"totalAmount\": 207120.00,\n" +
                "    \"term\": 6,\n" +
                "    \"monthlyPayment\": 34520.00,\n" +
                "    \"rate\": 12,\n" +
                "    \"isInsuranceEnabled\": false,\n" +
                "    \"isSalaryClient\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"applicationId\": 2,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"totalAmount\": 206520.00,\n" +
                "    \"term\": 6,\n" +
                "    \"monthlyPayment\": 34420.00,\n" +
                "    \"rate\": 11,\n" +
                "    \"isInsuranceEnabled\": false,\n" +
                "    \"isSalaryClient\": true\n" +
                "  },\n" +
                "  {\n" +
                "    \"applicationId\": 3,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"totalAmount\": 206346.60,\n" +
                "    \"term\": 6,\n" +
                "    \"monthlyPayment\": 34391.10,\n" +
                "    \"rate\": 9,\n" +
                "    \"isInsuranceEnabled\": true,\n" +
                "    \"isSalaryClient\": false\n" +
                "  },\n" +
                "  {\n" +
                "    \"applicationId\": 4,\n" +
                "    \"requestedAmount\": 200000,\n" +
                "    \"totalAmount\": 205743.60,\n" +
                "    \"term\": 6,\n" +
                "    \"monthlyPayment\": 34290.60,\n" +
                "    \"rate\": 8,\n" +
                "    \"isInsuranceEnabled\": true,\n" +
                "    \"isSalaryClient\": true\n" +
                "  }\n" +
                "]";

        credit = "{\n" +
                "  \"amount\": 207120.00,\n" +
                "  \"term\": 6,\n" +
                "  \"monthlyPayment\": 34520.00,\n" +
                "  \"rate\": 12,\n" +
                "  \"psk\": 3.56,\n" +
                "  \"isInsuranceEnabled\": false,\n" +
                "  \"isSalaryClient\": false,\n" +
                "  \"paymentSchedule\": [\n" +
                "    {\n" +
                "      \"number\": 1,\n" +
                "      \"date\": \"" + date.plusMonths(1) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 2071.20,\n" +
                "      \"debtPayment\": 32448.80,\n" +
                "      \"remainingDebt\": 172600.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 2,\n" +
                "      \"date\": \"" + date.plusMonths(2) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 1726.00,\n" +
                "      \"debtPayment\": 32794.00,\n" +
                "      \"remainingDebt\": 138080.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 3,\n" +
                "      \"date\": \"" + date.plusMonths(3) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 1380.80,\n" +
                "      \"debtPayment\": 33139.20,\n" +
                "      \"remainingDebt\": 103560.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 4,\n" +
                "      \"date\": \"" + date.plusMonths(4) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 1035.60,\n" +
                "      \"debtPayment\": 33484.40,\n" +
                "      \"remainingDebt\": 69040.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 5,\n" +
                "      \"date\": \"" + date.plusMonths(5) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 690.40,\n" +
                "      \"debtPayment\": 33829.60,\n" +
                "      \"remainingDebt\": 34520.00\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 6,\n" +
                "      \"date\": \"" + date.plusMonths(6) + "\",\n" +
                "      \"totalPayment\": 34520.00,\n" +
                "      \"interestPayment\": 345.20,\n" +
                "      \"debtPayment\": 34174.80,\n" +
                "      \"remainingDebt\": 0.00\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        creditEmpty = "{\n" +
                "  \"amount\": 0,\n" +
                "  \"term\": 0,\n" +
                "  \"monthlyPayment\": 0,\n" +
                "  \"rate\": 0,\n" +
                "  \"psk\": 0,\n" +
                "  \"isInsuranceEnabled\": false,\n" +
                "  \"isSalaryClient\": false,\n" +
                "  \"paymentSchedule\": []\n" +
                "}";

    }

    @Test
    public void getDifferentOffersOfLoanConditions() throws Exception {
        String jsonLoanApplicationString = jsonLoanApplication.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/offers")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonLoanApplicationString))
                .andExpect(status().isOk())
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        JSONParser jsonParser = new JSONParser();
        assertThat(responce).isEqualTo(loanOffers.replaceAll("\\s+", ""));
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
                .andReturn();

        String responce = mvcResult.getResponse().getContentAsString();
        assertThat(responce).isEqualTo(credit.replaceAll("\\s+", ""));
    }

    @Test
    public void getCreditExceptionTest() throws Exception {
        jsonScoringData.put("birthdate", "2010-03-08");
        String jsonScoringDataString = jsonScoringData.toString();
        this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getCreditExceptionValidationNameTest() throws Exception {
        jsonScoringData.put("firstName", "D");
        String jsonScoringDataString = jsonScoringData.toString();
        MvcResult mvcResult = this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().is4xxClientError())
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
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void getCreditEmptyTest() throws Exception {
        jsonScoringData.put("gender", "FEMALE");
        String jsonScoringDataString = jsonScoringData.toString();
        this.mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonScoringDataString))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("credit denied"));
    }
}
