package com.egs.account.service.user;

import com.egs.account.model.User;
import com.egs.account.repository.role.RoleRepository;
import com.egs.account.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        user.setDateRegistered(new Date());
        userRepository.save(user);
        LOGGER.info("user with username {} successfully saved", user.getUsername());
    }

    @Override
    public User findByUsername(String username) { return userRepository.findByUsername(username); }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public User findById(Long Id) {
        return userRepository.findById(Id);
    }

    public void updateUser(User user) {
        User entity = userRepository.findById(user.getId());
        if (entity != null) {
            entity.setFirstName(user.getFirstName());
            entity.setLastName(user.getLastName());
            entity.setEmail(user.getEmail());
            entity.setSkypeID(user.getSkypeID());
            entity.setDateRegistered(new Date());
            entity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(entity);
            LOGGER.info("user with id {} successfully updated", user.getId());
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        LOGGER.info("user with id {} successfully deleted", id);
    }
}
