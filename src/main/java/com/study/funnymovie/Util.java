package com.study.funnymovie;


import com.study.funnymovie.domain.user.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Util {
	public static User getUser(HttpSession session) {
		return (User)session.getAttribute(Value.KEY_LOGIN_USER);
	}
	public static String getSession(HttpServletRequest req, String key) {
		String v = (String) req.getSession().getAttribute(key);
		return v;
	}
	
	public static String getSession(HttpServletRequest req, String key, String defaultValue) {
		String v = (String) req.getSession().getAttribute(key);
		if (v == null) {
			return (String)defaultValue;
		} else {			
			return v;
		}
	}
}
