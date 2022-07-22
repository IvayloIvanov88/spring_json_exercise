package com.example.demo.services.impl;

import com.example.demo.models.dtos.CategorySeedDto;
import com.example.demo.models.entities.Category;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.CategoryService;
import com.example.demo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Random random) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedCategories(CategorySeedDto[] categorySeedDtos) {
        for (CategorySeedDto categorySeedDto : categorySeedDtos) {
            if (this.isCategoryExist(categorySeedDto.getName())) {
                System.out.printf("There is category with same name: %s%n", categorySeedDto.getName());
                continue;
            } else {
                Arrays.stream(categorySeedDtos).forEach(categoryDto -> {
                    if (this.validationUtil.isValid(categoryDto)) {
                        Category category = this.modelMapper.map(categoryDto, Category.class);
                        this.categoryRepository.saveAndFlush(category);
                    } else {
                        this.validationUtil.violations(categoryDto).stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
            }
        }

    }

    @Override
    public List<Category> getRandomCategories() {

        List<Category> result = new ArrayList<>();
        int randomCounter = this.random.nextInt(3) + 1;
        for (int i = 0; i < randomCounter; i++) {
            long randomId = this.random.nextInt((int) this.categoryRepository.count()) + 1;
            result.add(this.categoryRepository.getOne(randomId));
        }
        return result;
    }

    private boolean isCategoryExist(String categoryName) {
        List<Category> categories = this.categoryRepository.findByName(categoryName).orElse(null);
        return categories != null;
    }
}
