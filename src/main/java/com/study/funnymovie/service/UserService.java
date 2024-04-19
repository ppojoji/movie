package com.study.funnymovie.service;

import javax.management.RuntimeErrorException;

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

	public User userInfo(Integer userSeq) {
		return userDao.userInfo(userSeq);
		 
	}

	public User doJoin(String id, String email, String pwd) {
		User user = new User();
		user.setUser_id(id); // "", "...2$$%#", 
		user.setUser_pass(pwd); // "", "111", "..."
		user.setUser_email(email);;
		
		User existing = userDao.findByUserId(id);
		if(existing != null) {
			throw new RuntimeException("dup user id");
		}
		
		userDao.doJoin(user);
		return user;
	}
	/**
	 * 주어진 id가 이미 사용 중인지... 
	 * @param id
	 * @return 사용중이면 true, 사용하지 않으면 false
	 */
	public Boolean existingId(String id) {
		User user = userDao.findByUserId(id);
		if(user != null) {
			return true;
		}else {
			return false;
		}
	}
}
