package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserDto.UserView.RegistrationPost;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.ead.authuser.constants.AppConstants.Controller.AUTHENTICATION;
import static com.ead.authuser.constants.AppConstants.Data.UTC;
import static com.ead.authuser.enums.UserStatus.ACTIVE;
import static com.ead.authuser.enums.UserType.STUDENT;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(AUTHENTICATION)
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(RegistrationPost.class)
                                                   @JsonView(RegistrationPost.class) UserDto userDto){

        if (userService.existByUsername(userDto.getUsername())){
            return ResponseEntity
                    .status(CONFLICT)
                    .body("Error: Username is already taken");
        }
        if (userService.existByEmail(userDto.getEmail())){
            return ResponseEntity
                    .status(CONFLICT)
                    .body("Error: Email is already taken");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(ACTIVE);
        userModel.setUserType(STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of(UTC)));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(UTC)));

        userService.save(userModel);

        return ResponseEntity
                .status(CREATED)
                .body(userModel);
    }
}
