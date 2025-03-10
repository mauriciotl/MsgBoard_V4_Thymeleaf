package com.mau.msgboard_v4_thymeleaf.app.dataaccess;

import com.mau.msgboard_v4_thymeleaf.app.model.User;

import java.util.List;

public interface UserRepository {

    // Create
    User createUser(User user);

    // Read
    User findByUsername(String username);   //Note. The one tha uses spring-boot security

    User findById(int userId);
    List<User> findAll();

    // Update
    boolean updateUser(User user);

    // Delete
    boolean deleteUser(int userId);
    boolean deleteUser(User user);
}