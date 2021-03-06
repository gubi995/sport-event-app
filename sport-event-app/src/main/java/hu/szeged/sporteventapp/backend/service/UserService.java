package hu.szeged.sporteventapp.backend.service;

import java.util.HashSet;
import java.util.Set;

import hu.szeged.sporteventapp.backend.data.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.szeged.sporteventapp.backend.data.entity.User;
import hu.szeged.sporteventapp.backend.repositories.UserRepository;
import hu.szeged.sporteventapp.common.exception.AlreadyExistsException;
import hu.szeged.sporteventapp.security.SecurityUtils;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Set<User> findAll() {
		return new HashSet<>(userRepository.findAll());
	}

	public User getCurrentUser() {
		return userRepository.findByUsername(SecurityUtils.getUsername());
	}

	boolean usernameIsExist(String username) {
		return userRepository.countByUsername(username) > 0 ? true : false;
	}

	boolean emailIsExist(String email) {
		return userRepository.countByEmail(email) > 0 ? true : false;
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public int countAllByRole(Role role){
		return userRepository.countAllByRole(role);
	}

	@Transactional
	public User save(User user) throws AlreadyExistsException {
		if (usernameIsExist(user.getUsername())) {
			throw new AlreadyExistsException("Username already exists");
		} else if (emailIsExist(user.getEmail())) {
			throw new AlreadyExistsException("Email already exists");
		} else {
			String hashedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(hashedPassword);
			return userRepository.save(user);
		}
	}

	@Transactional
	public int updateUserPassword(String id, String password) {
		return userRepository.updateUserPassword(id, password);
	}

	@Transactional
	public int updateUserAdditionalData(String id, int age, String realName, String mobileNumber) {
		return userRepository.updateUserAdditionalData(id, age, realName, mobileNumber);
	}

	@Transactional
	public int updateUserImageData(String id, String pictureName) {
		return userRepository.updateUserImageData(id, pictureName);
	}

	@Transactional
	public void delete(User user) {
		userRepository.delete(user);
	}
}
