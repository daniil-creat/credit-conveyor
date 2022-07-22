package com.example.dossier.utils;

public class MessageTemplate {

    public static final String CREDIT_AGREED = "<p>Credit agreed</p>";
    public static final String CREDIT_DENIED = "<p>Credit denied</p>";

    public static String getTemplateForFinishedRegistration(Long applicationId) {
        return "<p>Hello! You loan application №" + applicationId + " approved.</p>" +
                "<p>to continue registration, follow the link:</p>" +
                "<a href=\"http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8082/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
    }

    public static String getTemplateForCreateDocuments(Long applicationId) {
        return "<p>Hello! you have passed all the checks by application №" + applicationId + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
    }

    public static String getTamplateForEmailWithDocuments(Long applicationId) {
        return "<p>Hello! you have passed all the checks by application №" + applicationId + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
    }

    public static String getTemplateForEmailWithCode(String code) {
        return "<p>your code for signing documents" + code + ".</p>" +
                "<p>to continue, follow the link:</p>" +
                "<a href=\"http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT\">" +
                "http://localhost:8085/swagger-ui/index.html#/deal-controller/calculateCreditUsingPUT</a>";
    }
}
