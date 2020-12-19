package org.apac.erp.cach.forecast.service;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.User;
import org.apac.erp.cach.forecast.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	PasswordEncoder encoder;

	public List<User> findAllUsers() {
		return userRepo.findAll();
	}

	public User saveNewUser(User user) {
		user.setUserPassword(encoder.encode(user.getUserPassword()));
		return userRepo.save(user);
	}

	public User findUserById(Long userId) {
		return userRepo.findOne(userId);
	}

}
