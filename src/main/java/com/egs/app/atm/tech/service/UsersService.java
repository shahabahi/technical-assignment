package com.egs.app.atm.tech.service;

import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto user) throws Exception;

     UserDto getUserByCardNumber(String cardNumber);

    UserDto updateUserDetails(UserDto userDetails) ;

}
