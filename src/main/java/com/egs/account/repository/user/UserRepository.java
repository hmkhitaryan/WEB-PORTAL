package com.egs.account.repository.user;

import com.egs.account.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username = ?")
    User findByUsername(String username);
}
