package com.example.deal.services.impl;

import com.example.deal.dto.PaymentScheduleElement;
import com.example.deal.entity.Application;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.deal.utils.Templates.*;

@RequiredArgsConstructor
@Service
@Log4j2
public class DocumentServiceImpl implements DocumentService {

    private final ApplicationService applicationService;

    @Override
    public File generetedDocumentAndGetFileCreditInfo(Long applicationId) throws IOException {
        log.info("Service: Document,generetedDocumentAndGetFileCreditInfo method, parameters: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        Map<String, String> values = new HashMap<>();
        values.put("applicationId", applicationId.toString());
        values.put("creationDate", application.getCreationDate().toString());
        values.put("firstName", application.getClient().getFirstName());
        values.put("lastName", application.getClient().getLastName());
        values.put("middleName", application.getClient().getMiddleName());
        values.put("issueBranch", application.getClient().getPassport().getIssueBranch());
        values.put("issueDate", application.getClient().getPassport().getIssueDate() != null ? application.getClient().getPassport().getIssueDate().toString() : null);
        values.put("number", application.getClient().getPassport().getNumber() != null ? application.getClient().getPassport().getNumber() : null);
        values.put("serial", application.getClient().getPassport().getSeries() != null ? application.getClient().getPassport().getSeries() : null);
        values.put("amount", application.getAppliedOffer().getTotalAmount().toString());
        values.put("term", application.getAppliedOffer().getTerm().toString());
        values.put("payment", application.getAppliedOffer().getMonthlyPayment().toString());
        values.put("rate", application.getAppliedOffer().getRate().toString());
        values.put("insurance", application.getAppliedOffer().getIsInsuranceEnabled().toString());
        values.put("client", application.getAppliedOffer().getIsSalaryClient().toString());

        StringSubstitutor sub = new StringSubstitutor(values);
        String result = sub.replace(TEMPLATE_FOR_CREDIT_INFO);
        String path = "deal\\src\\main\\resources\\credit_info.txt";
        File file = new File(path);
        log.info("Start writ to file");
        FileUtils.writeStringToFile(file, result, String.valueOf(StandardCharsets.UTF_8));
        return file;
    }

    @Override
    public File generetedDocumentAndGetFilePaymentInfo(Long applicationId) throws IOException {
        log.info("Service: Document,generetedDocumentAndGetFilePaymentInfo method, parameters: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        List<PaymentScheduleElement> list = application.getCredit().getPaymentSchedule();
        List<String> poolString = new ArrayList<>();
        String path = "deal\\src\\main\\resources\\payment_info.txt";
        File file = new File(path);
        list.forEach(payment -> {
            Map<String, String> values = new HashMap<>();
            values.put("number", payment.getNumber().toString());
            values.put("date", payment.getDate().toString());
            values.put("totalPayment", payment.getTotalPayment().toString());
            values.put("payment", payment.getInterestPayment().toString());
            values.put("debt", payment.getDebtPayment().toString());
            values.put("remaining", payment.getRemainingDebt().toString());

            StringSubstitutor sub = new StringSubstitutor(values);
            String result = sub.replace(TEMPLATE_FOR_PAYMENT_SCHEDULER);
            poolString.add(result);
        });
        log.info("Start write to file");
        FileUtils.writeStringToFile(file, poolString.toString(), String.valueOf(StandardCharsets.UTF_8));
        return file;
    }

    @Override
    public File generetedDocumentAndGetFileClientInfo(Long applicationId) throws IOException {
        log.info("Service: Document,generetedDocumentAndGetFileClientInfo method, parameters: {}", applicationId);
        Application application = applicationService.findById(applicationId);
        Map<String, String> values = new HashMap<>();
        values.put("applicationId", applicationId.toString());
        values.put("creationDate", application.getCreationDate().toString());
        values.put("firstName", application.getClient().getFirstName());
        values.put("lastName", application.getClient().getLastName());
        values.put("middleName", application.getClient().getMiddleName());
        values.put("birthDate", application.getClient().getBirthDate().toString());
        values.put("issueBranch", application.getClient().getPassport().getIssueBranch());
        values.put("issueDate", application.getClient().getPassport().getIssueDate() != null ? application.getClient().getPassport().getIssueDate().toString() : null);
        values.put("email", application.getClient().getEmail());
        values.put("dependentAmount", application.getClient().getDependentAmount() != null ? application.getClient().getDependentAmount().toString() : null);
        values.put("employmentStatus", application.getClient().getEmployment().getEmploymentStatus() != null ? application.getClient().getEmployment().getEmploymentStatus().toString() : null);
        values.put("employmentInn", application.getClient().getEmployment().getEmployerINN());
        values.put("salary", application.getClient().getEmployment().getSalary() != null ? application.getClient().getEmployment().getSalary().toString() : null);
        values.put("position", application.getClient().getEmployment().getPosition() != null ? application.getClient().getEmployment().getPosition().toString() : null);
        values.put("workExCur", application.getClient().getEmployment().getWorkExperienceCurrent() != null ? application.getClient().getEmployment().getWorkExperienceCurrent().toString() : null);
        values.put("workExTotal", application.getClient().getEmployment().getWorkExperienceTotal() != null ? application.getClient().getEmployment().getWorkExperienceTotal().toString() : null);

        StringSubstitutor sub = new StringSubstitutor(values);
        String result = sub.replace(TEMPLATE_FOR_CLIENT_INFO);
        String path = "deal\\src\\main\\resources\\client_info.txt";
        File file = new File(path);
        log.info("Start write to file");
        FileUtils.writeStringToFile(file, result, String.valueOf(StandardCharsets.UTF_8));
        return file;
    }
}
