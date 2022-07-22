package com.example.demo.controlers;

import com.example.demo.constants.GlobalConstant;
import com.example.demo.models.dtos.*;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;
import com.example.demo.utils.FileIOUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class AppController implements CommandLineRunner {
    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;

    private final ProductService productService;
    private final FileIOUtil fileIOUtil;

    @Autowired
    public AppController(Gson gson, CategoryService categoryService, UserService userService, ProductService productService, FileIOUtil fileIOUtil) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.fileIOUtil = fileIOUtil;
    }


    @Override
    public void run(String... args) throws Exception {

        this.seedCategories();
        this.seedUsers();
        this.seedProducts();
        this.writeProductsInRange();
        this.writeUsersSoldProducts();
    }

    private void writeUsersSoldProducts() throws IOException {
        List<UsersSoldProductsDto> userSoldProductsDto = this.userService.getAllUsersSoldProducts();

        String json = this.gson.toJson(userSoldProductsDto);
        this.fileIOUtil.write(json, GlobalConstant.EX_2_OUTPUT_PATH);
    }

    private void writeProductsInRange() throws IOException {
        List<ProductInRangeDto> dtos = this.productService.getAllProductInRanges();

        String json = this.gson.toJson(dtos);
        this.fileIOUtil.write(json, GlobalConstant.EX_1_OUTPUT_PATH);
    }

    private void seedProducts() throws FileNotFoundException {
        ProductSeedDto[] productSeedDtos = this.gson.fromJson(new FileReader(GlobalConstant.PRODUCTS_FILE_PATH),
                ProductSeedDto[].class);
        this.productService.seedProduct(productSeedDtos);
    }

    private void seedUsers() throws FileNotFoundException {
        UserSeedDto[] userSeedDtos = this.gson.fromJson(new FileReader(GlobalConstant.USERS_FILE_PATH),
                UserSeedDto[].class);
        this.userService.seedUsers(userSeedDtos);
    }

    private void seedCategories() throws FileNotFoundException {
        CategorySeedDto[] categorySeedDtos = this.gson.fromJson(new FileReader(GlobalConstant.CATEGORIES_FILE_PATH),
                CategorySeedDto[].class);
        this.categoryService.seedCategories(categorySeedDtos);

    }
}
