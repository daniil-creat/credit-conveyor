package com.example.deal.services.impl;

import com.example.deal.dto.PaymentScheduleElement;
import com.example.deal.entity.Application;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.DocumentService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final ApplicationService applicationService;

    private static final String TEMPLATE_FOR_CLIENT_INFO = "Credit application ${applicationId} from ${creationDate}\n" +
            "Client info:\n" +
            "full name:${firstName} ${lastName} ${middleName}\n" +
            "birthday:${birthDate}\n" +
            "passport:\n" +
            "    issue branch: ${issueBranch}       issue date:${issueDate}\n" +
            "email:${email}\n" +
            "dependent amount:${dependentAmount}\n" +
            "Employment:\n" +
            "     employment status:${employmentStatus}\n" +
            "     employerinn:${employmentInn}\n" +
            "     salary:${salary}\n" +
            "     position:${position}\n" +
            "     work experience current:${workExCur}\n" +
            "     work experience total:${workExTotal}";

    private static final String TEMPLATE_FOR_CREDIT_INFO = "Credit contract ${creditId} from ${creationDate}\n" +
            "Client info:\n" +
            "full name:${firstName} ${lastName} ${middleName}\n" +
            "passport:  issue branch: ${issueBranch}  issue date:${issueDate}\n" +
            "            number:${number}     serial:${serial}\n" +
            "Credit info:\n" +
            "     amount:${amount}\n" +
            "     term:${term}\n" +
            "     monthly payment:${payment}\n" +
            "     rate:${rate}\n" +
            "     psk:${psk}\n" +
            "         services:\n"+
            "               insurance:${insurance}\n"+
            "               salary client:${client}\n";

    private static final String TEMPLATE_FOR_PAYMENT_SCHEDULER = "Payment schedule:\n" +
            "     number:${number}\n" +
            "     date:${date}\n" +
            "     total payment:${totalPayment}\n" +
            "     monthly payment:${payment}\n" +
            "     debt payment:${debt}\n" +
            "     remaining debt:${remaining}\n";

    @Override
    public File generetedDocumentAndGetFileCreditInfo(Long applicationId) throws IOException {
        Application application = applicationService.findById(applicationId);
        Map<String, String> values = new HashMap<>();
        values.put("applicationId", applicationId.toString());
        values.put("creationDate", application.getCreationDate().toString());
        values.put("firstName", application.getClient().getFirstName());
        values.put("lastName", application.getClient().getLastName());
        values.put("middleName", application.getClient().getMiddleName());
        values.put("issueBranch", application.getClient().getPassport().getIssueBranch());
        values.put("issueDate", application.getClient().getPassport().getIssueDate() != null ? application.getClient().getPassport().getIssueDate().toString() : null );
        values.put("number", application.getClient().getPassport().getNumber() != null ? application.getClient().getPassport().getNumber() : null );
        values.put("serial", application.getClient().getPassport().getSeries() != null ? application.getClient().getPassport().getSeries() : null );
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
        FileUtils.writeStringToFile(file, result, String.valueOf(StandardCharsets.UTF_8));
        return file;
    }

    @Override
    public File generetedDocumentAndGetFilePaymentInfo(Long applicationId) throws IOException {
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
        FileUtils.writeStringToFile(file, poolString.toString(), String.valueOf(StandardCharsets.UTF_8));
        return file;
    }

    @Override
    public File generetedDocumentAndGetFileClientInfo(Long applicationId) throws IOException {
        Application application = applicationService.findById(applicationId);
        Map<String, String> values = new HashMap<>();
        values.put("applicationId", applicationId.toString());
        values.put("creationDate", application.getCreationDate().toString());
        values.put("firstName", application.getClient().getFirstName());
        values.put("lastName", application.getClient().getLastName());
        values.put("middleName", application.getClient().getMiddleName());
        values.put("birthDate", application.getClient().getBirthDate().toString());
        values.put("issueBranch", application.getClient().getPassport().getIssueBranch());
        values.put("issueDate", application.getClient().getPassport().getIssueDate() != null ? application.getClient().getPassport().getIssueDate().toString() : null );
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
        FileUtils.writeStringToFile(file, result, String.valueOf(StandardCharsets.UTF_8));
        return file;
    }
}
