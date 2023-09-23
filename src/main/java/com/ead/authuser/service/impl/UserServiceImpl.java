package com.ead.authuser.service.impl;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserModel> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserModel> findOne(UUID id) {
        return repository.findById(id);
    }

    @Override
    public void delete(UserModel userModel) {
        repository.delete(userModel);
    }

    @Override
    public void save(UserModel userModel) {
        repository.save(userModel);
    }

    @Override
    public boolean existByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserModel updateUser(UserModel userModel, UserDto userDto) {
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(userModel);
    }

    @Override
    public void updatePassword(UserModel userModel, UserDto userDto) {
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        repository.save(userModel);
    }

    @Override
    public UserModel updateImage(UserModel userModel, String imageUrl) {
        userModel.setImageUrl(imageUrl);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return repository.save(userModel);
    }
}
