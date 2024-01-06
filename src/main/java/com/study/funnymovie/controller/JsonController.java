package com.study.funnymovie.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.study.funnymovie.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonController {

	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/users")
	public Object findUser() {
		Object users = userService.findUser();
		return users;
	}
}
