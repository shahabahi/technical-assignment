package com.egs.app.atm.tech.service.impl;

import com.egs.app.atm.tech.persistence.dto.AccountDto;
import com.egs.app.atm.tech.persistence.model.Account;
import com.egs.app.atm.tech.persistence.model.User;
import com.egs.app.atm.tech.persistence.repository.AccountRepository;
import com.egs.app.atm.tech.persistence.repository.UserRepository;
import com.egs.app.atm.tech.service.AccountService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDto getRemain(String cardNumber) {
        User user = userRepository.findByCardNumber(cardNumber);
        AccountDto accountDto = new AccountDto();
        if (user != null) {
            if (user.getAccount() == null) {
                accountDto.setRemain(0d);
                accountDto.setUserId(user.getId());
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                Account account = modelMapper.map(accountDto, Account.class);
                accountRepository.save(account);
            } else {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                accountDto = modelMapper.map(user.getAccount(), AccountDto.class);

            }
        }


        return accountDto;
    }
}
