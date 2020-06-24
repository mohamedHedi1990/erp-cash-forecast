package org.apac.erp.cach.forecast.controller;

import java.util.List;

import org.apac.erp.cach.forecast.persistence.entities.User;
import org.apac.erp.cach.forecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@CrossOrigin
	@GetMapping()
	public List<User> findAllUsers() {
		return userService.findAllUsers();
	}

	@CrossOrigin
	@PostMapping()
	public User saveNewUser(@RequestBody User user) {
		return userService.saveNewUser(user);
	}

	@CrossOrigin
	@GetMapping("by-user-id/{userId}")
	public User findUserById(@PathVariable("userId") Long userId) {
		return userService.findUserById(userId);
	}
}
