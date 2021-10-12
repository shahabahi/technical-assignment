package com.egs.app.atm.tech.service.impl;

import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.Role;
import com.egs.app.atm.tech.persistence.model.User;
import com.egs.app.atm.tech.persistence.repository.UserRepository;
import com.egs.app.atm.tech.service.UsersService;
import com.egs.app.atm.tech.service.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    @Autowired
    Utils utils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UsersService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, user.getCardNumber());
    }
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getCardNumber(), user.getPassword(), authorities);
    }
@Override
    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }
    @Override
    public void lock(User user) {
        user.setEnabled(false);
        user.setLockTime(new Date());

        userRepository.save(user);
    }
@Override
    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setEnabled(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);

            userRepository.save(user);

            return true;
        }

        return false;
    }
    @Override
    public UserDto createUser(UserDto userDetails) throws Exception {
        userDetails.setCardNumber(utils.generateUserId());
        userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDetails, User.class);
        userDetails.setCardNumber(UUID.randomUUID().toString());
        user.setRoles(new HashSet<>(Arrays.asList(Role.builder().role("USER").build())));
        userRepository.save(user);
        UserDto returnValue = modelMapper.map(user, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserByCardNumber(String cardNumber) {
        User users = userRepository.findByCardNumber(cardNumber);
        if (users == null) throw new UsernameNotFoundException(cardNumber);
        UserDto userDto = new ModelMapper().map(users, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto updateUserDetails(UserDto userDetails) {
        User users = userRepository.findByCardNumber(userDetails.getCardNumber());
        if(users==null|| !users.getCardNumber().equalsIgnoreCase(userDetails.getCardNumber()))
            throw new UsernameNotFoundException("");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDetails, User.class);
        userRepository.save(user);
        UserDto returnValue = modelMapper.map(user, UserDto.class);
        return returnValue;
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach((role) -> {
            roles.add(new SimpleGrantedAuthority(role.getRole()));
        });

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    @Override
    public UserDetails loadUserByUsername(String cardNumber) throws UsernameNotFoundException {
        User users = userRepository.findByCardNumber(cardNumber);
        if (users == null) throw new UsernameNotFoundException(cardNumber);
        List<GrantedAuthority> authorities = getUserAuthority(users.getRoles());
        return buildUserForAuthentication(users, authorities);
    }
}
