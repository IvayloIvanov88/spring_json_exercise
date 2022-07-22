package com.example.demo.services.impl;

import com.example.demo.models.dtos.UserSeedDto;
import com.example.demo.models.dtos.UsersSoldProductsDto;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Random random) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public List<UsersSoldProductsDto> getAllUsersSoldProducts() {
        return this.userRepository
                .findBySoldOrderByLastNameAscFirstNameAsc()
                .stream()
                .map(u -> {
                    UsersSoldProductsDto userSoldProductsDto = this.modelMapper.map(u, UsersSoldProductsDto.class);

                    return userSoldProductsDto;

                }).collect(Collectors.toList());
    }

    @Override
    public void seedUsers(UserSeedDto[] userSeedDtos) {
        for (UserSeedDto userSeedDto : userSeedDtos) {
            if (this.isUserExist(userSeedDto.getFirstName(), userSeedDto.getLastName(), userSeedDto.getAge())) {
                System.out.printf("There is user with same name and same age: %s %s age %d%n",
                        userSeedDto.getFirstName(), userSeedDto.getLastName(), userSeedDto.getAge());
                continue;
            } else {
                Arrays.stream(userSeedDtos).forEach(userDto -> {
                    if (this.validationUtil.isValid(userDto)) {
                        User user = this.modelMapper.map(userDto, User.class);
                        this.userRepository.saveAndFlush(user);
                    } else {
                        this.validationUtil.violations(userDto).stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
            }
        }
    }

    @Override
    public User getRandomUser() {
        long randomId = this.random.nextInt((int) this.userRepository.count()) + 1L;
        return userRepository.getOne(randomId);
    }

    private boolean isUserExist(String firstName, String lastName, int age) {
        List<User> users = this.userRepository.findFirstByFirstNameAndLastNameAndAge(firstName, lastName, age).orElse(null);
        return users != null;
    }
}
