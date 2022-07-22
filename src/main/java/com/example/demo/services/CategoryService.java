package com.example.demo.services;

import com.example.demo.models.dtos.CategorySeedDto;
import com.example.demo.models.entities.Category;

import java.util.List;

public interface CategoryService {
    void seedCategories(CategorySeedDto[] categorySeedDtos);

    List<Category> getRandomCategories();
}
