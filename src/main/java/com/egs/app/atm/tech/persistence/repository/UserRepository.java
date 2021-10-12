package com.egs.app.atm.tech.persistence.repository;

import com.egs.app.atm.tech.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long> {


    User findByCardNumber(String cardNumber);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.cardNumber = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String cardNumber);
}
