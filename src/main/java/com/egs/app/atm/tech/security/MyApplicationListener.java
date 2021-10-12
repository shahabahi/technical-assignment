package com.egs.app.atm.tech.security;

import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.User;
import com.egs.app.atm.tech.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private UsersService userService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event)  {
        String cardNumber =(String) event.getAuthentication().getPrincipal();
       // Object credentials = event.getAuthentication().getCredentials();
        UserDto userDto = userService.getUserByCardNumber(cardNumber);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDto, User.class);

        if (user != null) {
            if (user.isEnabled() && user.isEnabled()) {
                if (user.getFailedAttempt() < UsersService.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    throw  new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!user.isEnabled()) {
                if (userService.unlockWhenTimeExpired(user)) {
                    throw  new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }
    }
}
