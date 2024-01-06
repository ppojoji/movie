package com.study.funnymovie.domain.user.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.funnymovie.Util;
import com.study.funnymovie.Value;
import com.study.funnymovie.domain.user.User;
import com.study.funnymovie.service.UserService;

import jakarta.servlet.http.HttpSession;
/**

session 사용 thymeleaf
thymeleaf 에서  if-else

 * @author ppojo
 *
 */
@Controller
public class LoginController {
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	@ResponseBody
	public Object login(@RequestParam String id, @RequestParam String pass, HttpSession session) {
		User sessionUser =  Util.getUser(session);
		System.out.println("[sessionUser]" + sessionUser);
		System.out.println("[LOGIN]"+id + "pass" + pass);
		User user= userService.login(id, pass);
		System.out.println("[login] " + user);
		session.setAttribute(Value.KEY_LOGIN_USER,user);
		
//		if (user != null) {
//			res.put("success", true);
//		} else {
//			res.put("success", false);
//		}
		Map res = new HashMap<>();
		res.put("success",  user != null);
		res.put("user", user);
		
//		if (user == null) {
//			throw new AppException
//		}
		return res;
		// return list 
	}
	
	@PostMapping("/logout")
	@ResponseBody
	public Object logout(HttpSession session) {
		session.invalidate();
		return true;
	}
	// "/logout"
}
