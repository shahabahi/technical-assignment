package com.egs.app.atm.tech.controller;

import com.egs.app.atm.tech.persistence.dto.AccountDto;
import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.Account;
import com.egs.app.atm.tech.persistence.model.request.AccountRequestModel;
import com.egs.app.atm.tech.persistence.model.request.UserDetailsRequestModel;
import com.egs.app.atm.tech.persistence.model.response.AccountRes;
import com.egs.app.atm.tech.persistence.model.response.ResponseApi;
import com.egs.app.atm.tech.persistence.model.response.UserRest;
import com.egs.app.atm.tech.persistence.repository.AccountRepository;
import com.egs.app.atm.tech.service.AccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @PostMapping(path = "/deposit",
            consumes =
                    {
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE
                    }, produces =
            {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<ResponseApi> deposit(@Valid @RequestBody AccountRequestModel accountRequestModel)  {

        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            AccountDto accountDto = accountService.deposit(accountRequestModel.getCardNumber(),accountRequestModel.getAmount());
            AccountRes accountRes = modelMapper.map(accountDto, AccountRes.class);
            return new ResponseEntity(accountRes, HttpStatus.CREATED);

        } catch (Exception ex) {
            logger.error(ex.getCause().toString());
            return new ResponseEntity<>(new ResponseApi((byte) -9, "error in system"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/withdraw",
            consumes =
                    {
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE
                    }, produces =
            {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<ResponseApi> withdraw(@Valid @RequestBody AccountRequestModel accountRequestModel)  {

        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            AccountDto accountDto = accountService.withdraw(accountRequestModel.getCardNumber(),accountRequestModel.getAmount());
            AccountRes accountRes = modelMapper.map(accountDto, AccountRes.class);
            return new ResponseEntity(accountRes, HttpStatus.CREATED);

        } catch (Exception ex) {
            logger.error(ex.getCause().toString());
            return new ResponseEntity<>(new ResponseApi((byte) -9, "error in system"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
