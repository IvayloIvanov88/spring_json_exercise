package com.example.demo.services;

import com.example.demo.models.dtos.ProductInRangeDto;
import com.example.demo.models.dtos.ProductSeedDto;

import java.util.List;

public interface ProductService {

    void seedProduct(ProductSeedDto[] productSeedDtos);

    List<ProductInRangeDto> getAllProductInRanges();
}
