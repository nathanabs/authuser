package com.ead.authuser.service;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findOne(UUID id);

    void delete(UserModel userModel);

    void save(UserModel userModel);

    boolean existByUsername(String username);
    boolean existByEmail(String email);

    UserModel updateUser(UserModel userModel, UserDto userDto);

    void updatePassword(UserModel userModel, UserDto userDto);

    UserModel updateImage(UserModel userModel, String imageUrl);

    Page<UserModel> findAll(Pageable pageable, SpecificationTemplate.UserSpec spec);
}
