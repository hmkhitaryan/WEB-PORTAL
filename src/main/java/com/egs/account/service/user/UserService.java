package com.egs.account.service.user;

import com.egs.account.model.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    void saveUser(User user);

    void updateUser(User user);

    User findByUsername(String username);

    void deleteUserById(Long id);

    List<User> findAllUsers();
}
