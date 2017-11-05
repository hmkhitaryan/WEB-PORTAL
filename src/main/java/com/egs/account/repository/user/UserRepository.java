package com.egs.account.repository.user;

import com.egs.account.model.User;

import java.util.List;

public interface UserRepository  {
    User findByUsername(String username);

    User findById(Long id);

    User save(User user);

    void deleteById(Long id);

    List<User> findAllUsers();
}
