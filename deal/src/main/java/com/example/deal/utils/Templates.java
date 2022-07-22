package com.example.deal.utils;

public class Templates {
    public static final String TEMPLATE_FOR_CLIENT_INFO = "Credit application ${applicationId} from ${creationDate}\n" +
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

    public static final String TEMPLATE_FOR_CREDIT_INFO = "Credit contract ${creditId} from ${creationDate}\n" +
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

    public static final String TEMPLATE_FOR_PAYMENT_SCHEDULER = "Payment schedule:\n" +
            "     number:${number}\n" +
            "     date:${date}\n" +
            "     total payment:${totalPayment}\n" +
            "     monthly payment:${payment}\n" +
            "     debt payment:${debt}\n" +
            "     remaining debt:${remaining}\n";

}
