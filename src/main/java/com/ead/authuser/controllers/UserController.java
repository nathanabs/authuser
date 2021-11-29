package com.ead.authuser.controllers;

import com.ead.authuser.constants.AppConstants;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserDto.UserView.ImagePut;
import com.ead.authuser.dtos.UserDto.UserView.PasswordPut;
import com.ead.authuser.dtos.UserDto.UserView.UserPut;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ead.authuser.constants.AppConstants.Controller.USERS;
import static org.springframework.http.HttpStatus.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUser(){
        return ResponseEntity
                .status(OK)
                .body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()){
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body("User not found");
        }
        return ResponseEntity
                .status(OK)
                .body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()){
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body("User not found");
        }

        userService.delete(userModelOptional.get());

        return ResponseEntity
                .status(OK)
                .body("User deleted successfully.");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserPut.class)
                                             @JsonView(UserPut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()){
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body("User not found");
        }

        var userModel = userModelOptional.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(AppConstants.Data.UTC)));

        userService.save(userModel);

        return ResponseEntity
                .status(OK)
                .body(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(PasswordPut.class)
                                                 @JsonView(PasswordPut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()){
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body("User not found");
        }

        var userModel = userModelOptional.get();

        if (!userDto.getOldPassword().equals(userModel.getPassword())){
            return ResponseEntity
                    .status(CONFLICT)
                        .body("Error: mismatched old password!");
        }

        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(AppConstants.Data.UTC)));

        userService.save(userModel);

        return ResponseEntity
                .status(OK)
                .body("Password updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(ImagePut.class)
                                              @JsonView(ImagePut.class) UserDto userDto){

        Optional<UserModel> userModelOptional = userService.findById(userId);

        if(userModelOptional.isEmpty()){
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body("User not found");
        }

        var userModel = userModelOptional.get();

        userModel.setImgeUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of(AppConstants.Data.UTC)));

        userService.save(userModel);

        return ResponseEntity
                .status(OK)
                .body(userModel);
    }
}
