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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDto getBalance(String cardNumber) {
        User user = userRepository.findByCardNumber(cardNumber);
        AccountDto accountDto = new AccountDto();
        if (user != null) {
            if (user.getAccount() == null) {
                accountDto = setBalance(user);
            } else {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                accountDto = modelMapper.map(user.getAccount(), AccountDto.class);

            }
        }


        return accountDto;
    }
    @Override
    public AccountDto deposit(String cardNumber, Double amount) throws Exception {
        User user = userRepository.findByCardNumber(cardNumber);
        if (user == null) {
            throw new UsernameNotFoundException("");
        }
        Account account = null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        if (user.getAccount() == null) {
            AccountDto accountDto = setBalance(user);
            accountDto.setBalance(amount);
            account = modelMapper.map(accountDto, Account.class);
        } else {
            checkBalance(user.getAccount().getBalance() + amount);
            user.getAccount().setBalance(user.getAccount().getBalance() + amount);
            account = modelMapper.map(user.getAccount(), Account.class);
        }
        return getAccountDto(cardNumber, account);
    }

    private AccountDto getAccountDto(String cardNumber, Account account) throws Exception {
        accountRepository.save(account);
        User user = userRepository.findByCardNumber(cardNumber);
        checkBalance(user.getAccount().getBalance());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AccountDto accountDto = modelMapper.map(user.getAccount(), AccountDto.class);
        return accountDto;
    }

    private AccountDto setBalance(User user) {
        AccountDto accountDto = new AccountDto();
        accountDto.setBalance(0d);
        accountDto.setUser(user);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Account account = modelMapper.map(accountDto, Account.class);
        accountRepository.save(account);
        return accountDto;

    }

    @Override
    public AccountDto withdraw(String cardNumber, Double amount) throws Exception {
        User user = userRepository.findByCardNumber(cardNumber);
        if (user == null) {
            throw new UsernameNotFoundException("");
        }
        if (user.getAccount() == null)
            throw new UsernameNotFoundException("");

        checkBalance(user.getAccount().getBalance() - amount);
        user.getAccount().setBalance(user.getAccount().getBalance() - amount);
        return getAccountDto(cardNumber, user.getAccount());
    }

    private void checkBalance(Double amount) throws Exception {
        if (amount < 0)
            throw new Exception("Account balance is not enough");

    }
}
