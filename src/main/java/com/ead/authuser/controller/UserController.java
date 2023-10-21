package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUser(
            SpecificationTemplate.UserSpec spec,
            @PageableDefault(
            page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> userModelPage = service.findAll(pageable, spec);
        return ResponseEntity.ok(userModelPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> userOptional = service.findOne(id);

        return userOptional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id){
        Optional<UserModel> userOptional = service.findOne(id);

        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        service.delete(userOptional.get());
        return ResponseEntity.ok("User deleted successfuly");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        Optional<UserModel> userOptional = service.findOne(id);

        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var response = service.updateUser(userOptional.get(), userDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "id") UUID id,
                                                 @RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<UserModel> userOptional = service.findOne(id);

        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!userOptional.get().getPassword().equals(userDto.getOldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password ");
        }

        service.updatePassword(userOptional.get(), userDto);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "id") UUID id,
                                              @RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<UserModel> userOptional = service.findOne(id);

        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var response = service.updateImage(userOptional.get(), userDto.getImageUrl());
        return ResponseEntity.ok(response);
    }
}
