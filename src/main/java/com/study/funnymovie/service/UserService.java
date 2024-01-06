package com.study.funnymovie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.funnymovie.dao.UserDao;
import com.study.funnymovie.domain.user.User;

import jakarta.servlet.http.HttpSession;

@Service
@Transactional
public class UserService {

	@Autowired
	UserDao userDao;
	
	public Object findUser () {
		return userDao.findUsers();
	}

	public User login(String id, String pass) {
		// TODO Auto-generated method stub
		User user = userDao.login(id,pass);

		return user;
	}
}
