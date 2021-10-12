package com.egs.app.atm.tech.service;


import com.egs.app.atm.tech.persistence.dto.AccountDto;

public interface AccountService {
    AccountDto getBalance(String cardNumber);
    AccountDto deposit(String cardNumber,Double amount) throws Exception;
    AccountDto withdraw(String cardNumber,Double amount) throws Exception;

}
