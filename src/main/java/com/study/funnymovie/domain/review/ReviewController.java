package com.study.funnymovie.domain.review;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.funnymovie.Util;
import com.study.funnymovie.domain.movie.MovieService;
import com.study.funnymovie.domain.user.User;

import jakarta.servlet.http.HttpSession;

@RestController
public class ReviewController {
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	MovieService movieService; 
	
	/**
	 * 주어진 리뷰를 수정함
	 */
	@PutMapping("/review/{reviewSeq}")
	public Object updateReview(
			@PathVariable Integer reviewSeq,
			@RequestParam String comment,
			@RequestParam Integer score,
			HttpSession session) {
		
		User loginUser = Util.getUser(session);
		
		System.out.println("???"); 
		Review review = new Review();
		review.setRv_seq(reviewSeq);
		review.setRv_score(score);
		review.setRv_comment(comment);
		
		review = reviewService.updateReview(loginUser, review); 
		double avgScore = movieService.updateReviewScore(review);
		
		Map map = new HashMap<String,Object>();
		map.put("review", review);
		map.put("avgScore", avgScore);
		
		return map;
	}
	@DeleteMapping("/review/{reviewSeq}")
	public Object deleteReview(@PathVariable Integer reviewSeq,HttpSession session) {
		User loginUser = Util.getUser(session);
		
		Review deletedReview = reviewService.deleteReview(reviewSeq,loginUser);
		double avgScore = movieService.updateReviewScore(deletedReview);
		
		Map map = new HashMap<String,Object>();
		map.put("review", deletedReview);
		map.put("avgScore", avgScore);
		
		return map;
	}
}
