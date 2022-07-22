package com.example.deal.services;

import com.example.deal.dto.CreditDTO;
import com.example.deal.entity.Credit;

public interface CreditService {

    Credit saveCredit(CreditDTO credit);
}
