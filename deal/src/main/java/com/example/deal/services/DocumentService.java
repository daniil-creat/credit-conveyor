package com.example.deal.services;

import java.io.File;
import java.io.IOException;

public interface DocumentService {
    File generetedDocumentAndGetFileClientInfo(Long applicationId) throws IOException;

    File generetedDocumentAndGetFileCreditInfo(Long applicationId) throws IOException;

    File generetedDocumentAndGetFilePaymentInfo(Long applicationId) throws IOException;
}
