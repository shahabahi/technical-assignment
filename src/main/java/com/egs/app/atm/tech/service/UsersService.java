package com.egs.app.atm.tech.service;

import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    int MAX_FAILED_ATTEMPTS = 3;
    long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    void increaseFailedAttempts(User user);

    void lock(User user);

    void resetFailedAttempts(String cardNumber);

    boolean unlockWhenTimeExpired(User user);


    UserDto createUser(UserDto user) throws Exception;

    UserDto getUserByCardNumber(String cardNumber);

    UserDto updateUserDetails(UserDto userDetails);

}
