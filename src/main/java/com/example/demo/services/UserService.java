package com.example.demo.services;

import com.example.demo.models.dtos.UserSeedDto;
import com.example.demo.models.dtos.UsersSoldProductsDto;
import com.example.demo.models.entities.User;

import java.util.List;

public interface UserService {

    List<UsersSoldProductsDto> getAllUsersSoldProducts();

    void seedUsers(UserSeedDto[] userSeedDtos);

    User getRandomUser();
}
