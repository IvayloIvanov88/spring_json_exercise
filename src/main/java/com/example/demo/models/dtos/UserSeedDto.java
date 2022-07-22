package com.example.demo.models.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class UserSeedDto {

    @Expose
    private String firstName;
    @Expose
    @Length(min = 3,message = "Last name is too short")
    private String lastName;
    @Expose
    @Min(value = 0,message = "Age must be positive")
    @Max(value = 130,message = "Age must below 130")
    private int age;

    public UserSeedDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
