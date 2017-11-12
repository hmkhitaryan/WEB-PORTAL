package com.egs.account.service.user;

import com.egs.account.exception.EmailExistsException;
import com.egs.account.model.User;
import com.egs.account.model.VerificationToken;
import com.egs.account.repository.role.RoleRepository;
import com.egs.account.repository.token.VerificationTokenRepository;
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

	@Autowired
	private UserRepository repository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		user.setDateRegistered(new Date());
		userRepository.save(user);
		LOGGER.info("user with username {} successfully saved", user.getUsername());
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Find all users.
	 *
	 * @return list of user
	 */
	public List<User> findAllUsers() {
		return userRepository.findAllUsers();
	}

	/**
	 * Find user by id.
	 *
	 * @return user found
	 */
	public User findById(Long Id) {
		return userRepository.findById(Id);
	}

	/**
	 * Update user info.
	 *
	 * @param user user Whose info will be updated
	 */
	public void updateUser(User user) {
		final User entity = userRepository.findById(user.getId());
		if (entity != null) {
			updateUserInfo(entity, user);
			userRepository.save(entity);
			LOGGER.info("user with id {} successfully updated", user.getId());
		}
	}

	/**
	 * Update user info.
	 *
	 * @param u1 user whose info will be updated
	 * @param u2 whose info will be used
	 */
	private void updateUserInfo(User u1, User u2) {
		u1.setFirstName(u2.getFirstName());
		u1.setLastName(u2.getLastName());
		u1.setEmail(u2.getEmail());
		u1.setSkypeID(u2.getSkypeID());
		u1.setDateRegistered(new Date());
		u1.setPassword(bCryptPasswordEncoder.encode(u2.getPassword()));
	}

	/**
	 * Delete user by id.
	 *
	 * @param id by which user will be deleted
	 */
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
		LOGGER.info("user with id {} successfully deleted", id);
	}

	@Override
	public User registerNewUserAccount(User user)
			throws EmailExistsException {

		if (emailExist(user.getEmail())) {
			throw new EmailExistsException("There is an account with that email adress: "
					+ user.getEmail());
		}

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		user.setDateRegistered(new Date());

		return repository.save(user);
	}

	private boolean emailExist(String email) {
		User user = repository.findByUsername(email);

		return user != null;
	}

	@Override
	public User getUser(String verificationToken) {
		User user = tokenRepository.findByToken(verificationToken).getUser();
		return user;
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		repository.save(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}
}
