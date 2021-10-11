package com.egs.app.atm.tech.controller;

import com.egs.app.atm.tech.persistence.dto.AccountDto;
import com.egs.app.atm.tech.persistence.model.Account;
import com.egs.app.atm.tech.persistence.model.response.AccountRes;
import com.egs.app.atm.tech.persistence.repository.AccountRepository;
import com.egs.app.atm.tech.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @GetMapping(value="/getRemain/{cardNumber}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            })
    public ResponseEntity<AccountRes> getUserByCardNumber(@PathVariable("cardNumber") String cardNumber) {
        AccountDto account = accountService.getRemain(cardNumber);
        AccountRes returnValue = new ModelMapper().map(account, AccountRes.class);
        return new ResponseEntity(returnValue, HttpStatus.OK);
    }

}
