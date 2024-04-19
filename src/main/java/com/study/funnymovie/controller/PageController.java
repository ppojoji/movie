package com.study.funnymovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.study.funnymovie.domain.movie.rating.MovieRatingService;

@Controller
public class PageController {
	
	@Autowired
	MovieRatingService movieRatingService;
	/**
	 * 메인 페이지
	 * @return
	 */
	@GetMapping("/")
	public String mainPage(Model model) {
		List ratings = movieRatingService.movieList();
		model.addAttribute("ratings",ratings);
		return "index";
	}
	@GetMapping("/testpage")
	public Object pageTest() {
		
		return "test";
	}
	@GetMapping("/mypage")
	public String myPage() {
		return "myPage";
	}
}
