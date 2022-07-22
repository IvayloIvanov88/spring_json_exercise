package com.example.demo.config;

import com.example.demo.utils.FileIOUtil;
import com.example.demo.utils.ValidationUtil;
import com.example.demo.utils.impls.FileIOUtilImpl;
import com.example.demo.utils.impls.ValidationUtilImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class ApplicationBeenConfiguration {

    @Bean
    public Gson gson(){
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ValidationUtil validationUtil(){
        return new ValidationUtilImpl();
    }

    @Bean
    public Random random(){
        return new Random();
    }

    @Bean
    public FileIOUtil fileIoUtil(){
        return new FileIOUtilImpl();
    }
}
