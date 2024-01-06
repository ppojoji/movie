package com.study.funnymovie.domain.movie;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.funnymovie.Util;
import com.study.funnymovie.domain.review.Review;
import com.study.funnymovie.domain.review.ReviewService;
import com.study.funnymovie.domain.user.User;
import com.study.funnymovie.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class MovieController {

	@Autowired
	MovieService movieService;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/api/movie/all")
	private List<Movie> allMovie() {
		List<Movie> list = movieService.allMovie();
		return list;
	}
	
	@GetMapping("/movie/{movieSeq}/reviews")
	public List<Review> movieReview(@PathVariable Integer movieSeq) {
		List<Review> reviews = reviewService.findReviewByMovie(movieSeq);
		return reviews;
	}
	
	@PostMapping("/movie/writeReview")
	public Review writeReview(
			@RequestParam Integer movie_ref,
			@RequestParam Integer user_ref,
			@RequestParam Integer rv_score,
			@RequestParam String rv_comment,
			HttpSession session
			) {
	
		User sessionUser =  Util.getUser(session);
		
		System.out.println("movie_ref: " + movie_ref);
		System.out.println("rv_score: " + rv_score);
		System.out.println("rv_comment: " + rv_comment);
		System.out.println("[sessionUser]" + sessionUser);
		
		
		Review review = new Review(); 
		
		review.setMovie_ref(movie_ref);
		review.setUser_ref(sessionUser.getUser_seq());
		review.setRv_score(rv_score);
		review.setRv_comment(rv_comment);
		
		reviewService.writeReview(review);
		return review;
	}
}
