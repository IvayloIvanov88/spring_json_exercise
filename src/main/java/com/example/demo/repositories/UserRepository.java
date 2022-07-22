package com.example.demo.repositories;

import com.example.demo.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<List<User>> findFirstByFirstNameAndLastNameAndAge(String firstName, String lastName, int age);


    @Query("select u from User u where u.sold.size >=1 order by u.lastName, u.firstName")
    List<User> findBySoldOrderByLastNameAscFirstNameAsc();

}
