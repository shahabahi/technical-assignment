package com.egs.app.atm.tech.persistence.repository;


import com.egs.app.atm.tech.persistence.dto.AccountDto;
import com.egs.app.atm.tech.persistence.model.Account;
import com.egs.app.atm.tech.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{
    AccountDto findAccountByUserId(Long userId);
}
