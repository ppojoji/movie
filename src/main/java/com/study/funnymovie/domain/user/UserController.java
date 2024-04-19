package com.study.funnymovie.domain.user;
/**
 * 사용자의 정보를 조회/수정하는 역할
 * 
 * 1. 기본정보 - 닉네임, 비번 수정, 아이콘 설정
 * 2. 커뮤니티 - 리뷰조회, 쪽지기능
 * 3. 
 * @author ppojo
 *
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.study.funnymovie.Util;
import com.study.funnymovie.domain.review.Review;
import com.study.funnymovie.domain.review.ReviewService;
import com.study.funnymovie.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	ReviewService reviewService;
	
	
	@GetMapping("/userInfo")
	public User userInfo(
			HttpSession session) {
		User sessionUser =  Util.getUser(session);
		sessionUser.getUser_seq(); // userSEq
		return userService.userInfo(sessionUser.getUser_seq());
	}
	
	@GetMapping("/userReview")
	public Object userReview(
			HttpSession session) {
		User sessionUser =  Util.getUser(session);
		
		Object what = reviewService.findReviewByUser(sessionUser.getUser_seq());
		return what;
	}
}
