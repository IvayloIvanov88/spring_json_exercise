package com.example.demo.services.impl;

import com.example.demo.models.dtos.ProductInRangeDto;
import com.example.demo.models.dtos.ProductSeedDto;
import com.example.demo.models.entities.Product;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;
import com.example.demo.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final Random random;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService, Random random, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.random = random;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProduct(ProductSeedDto[] productSeedDtos) {

        for (ProductSeedDto productSeedDto : productSeedDtos) {
            if (this.isProductExist(productSeedDto.getName(), productSeedDto.getPrice())) {
                System.out.printf("There is product with same name %s and same price: %.2f%n",
                        productSeedDto.getName(), productSeedDto.getPrice());
                continue;
            } else {
                Arrays.stream(productSeedDtos).forEach(productDto -> {
                    if (this.validationUtil.isValid(productDto)) {
                        Product product = this.modelMapper.map(productDto, Product.class);

                        product.setSeller(this.userService.getRandomUser());

                        if (this.random.nextInt(2) == 1)
                            product.setBuyer(this.userService.getRandomUser());

                        product.setCategories(new HashSet<>(this.categoryService.getRandomCategories()));
                        this.productRepository.saveAndFlush(product);
                    } else {
                        this.validationUtil.violations(productDto).stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }
                });
            }
        }
    }

    @Override
    public List<ProductInRangeDto> getAllProductInRanges() {
        return this.productRepository
                .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500), BigDecimal.valueOf(1000))
                .stream()
                .map(p -> {
                    ProductInRangeDto productInRangeDto = this.modelMapper.map(p, ProductInRangeDto.class);
                    productInRangeDto
                            .setSeller(String.format("%s %s",
                                    p.getSeller().getFirstName(), p.getSeller().getLastName()));
                    return productInRangeDto;
                }).collect(Collectors.toList());
    }

    private boolean isProductExist(String name, BigDecimal price) {
        List<Product> products = this.productRepository.findByNameAndPrice(name, price).orElse(null);
        return products != null;
    }
}

