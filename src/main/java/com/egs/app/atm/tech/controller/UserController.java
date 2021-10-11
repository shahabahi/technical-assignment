package com.egs.app.atm.tech.controller;

import com.egs.app.atm.tech.persistence.dto.UserDto;
import com.egs.app.atm.tech.persistence.model.request.UserDetailsRequestModel;
import com.egs.app.atm.tech.persistence.model.response.ResponseApi;
import com.egs.app.atm.tech.persistence.model.response.UserRest;
import com.egs.app.atm.tech.service.UsersService;
import org.hibernate.annotations.Parameter;
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
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UsersService userService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/status/check")
    public String status() {
        return "working";
    }

    @PostMapping(path = "/create",
            consumes =
                    {
                            MediaType.APPLICATION_XML_VALUE,
                            MediaType.APPLICATION_JSON_VALUE
                    }, produces =
            {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<ResponseApi> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails)  {

        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            UserDto userDto = modelMapper.map(userDetails, UserDto.class);
            if(userService.getUserByCardNumber(userDto.getCardNumber())!=null)
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            UserDto createdUser = userService.createUser(userDto);
            UserRest userRest = modelMapper.map(createdUser, UserRest.class);
            return new ResponseEntity(userRest, HttpStatus.CREATED);

        } catch (Exception ex) {
            logger.error(ex.getCause().toString());
            return new ResponseEntity<>(new ResponseApi((byte) -9, "error in system"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/getUser/{cardNumber}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
            })
    public ResponseEntity<UserRest> getUserByCardNumber(@PathVariable("cardNumber") String cardNumber) {
        UserDto userDto = userService.getUserByCardNumber(cardNumber);
        UserRest returnValue = new ModelMapper().map(userDto, UserRest.class);
        return new ResponseEntity(returnValue, HttpStatus.OK);
    }
}
