package com.study.funnymovie.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.funnymovie.domain.user.User;

@Repository
public class UserDao {

	@Autowired
	SqlSession session;
	
	public Object findUsers() {
		return session.selectList("UserMapper.findUsers");
		
	}

	public User login(String id, String pass) {
		Map map = new HashMap<>();
		map.put("id", id);
		map.put("pass", pass);
		
		User user = session.selectOne("UserMapper.login",map);
		return user;
	}
}
