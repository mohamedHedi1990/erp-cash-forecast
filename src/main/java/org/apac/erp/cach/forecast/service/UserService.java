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

		if(user.getUserId() != null) {
			User oldUser = userRepo.findOne(user.getUserId());
			// utilisateur crée sans mot de passe
			if(oldUser.getUserPassword()== null) {
				user.setUserPassword(encoder.encode(user.getUserPassword()));
			} else { // contexte de modifier le mot de passe
				if(!user.getUserPassword().equals(oldUser.getUserPassword())) {
					user.setUserPassword(encoder.encode(user.getUserPassword()));
				}
			}

		} else {
			user.setUserPassword(encoder.encode(user.getUserPassword()));
		}

		user = this.userRepo.save(user);

		return user;
	}

	public User findUserById(Long userId) {
		return userRepo.findOne(userId);
	}

	public void deleteById(Long userId) {
		this.userRepo.delete(userId);
	}
}
