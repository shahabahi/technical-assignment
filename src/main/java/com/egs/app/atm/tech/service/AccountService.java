package com.egs.app.atm.tech.service;


import com.egs.app.atm.tech.persistence.dto.AccountDto;

public interface AccountService {
    AccountDto getRemain(String cardNumber);
}
