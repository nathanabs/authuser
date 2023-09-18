package com.ead.authuser.service;

import com.ead.authuser.model.UserModel;

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
}
