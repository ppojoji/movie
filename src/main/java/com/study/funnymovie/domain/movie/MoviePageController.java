package com.study.funnymovie.domain.movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.funnymovie.domain.movie.rating.MovieRatingService;

@Controller
public class MoviePageController {

	@Autowired
	MovieRatingService movieRatingService;
	
	@Autowired
	MovieService movieService;
	/**
	 * 영화 순위 페이지
	 * "/movies" - 현재 상영작
	 * "/movies?open=Y"- 현재 상영작
	 * "/movies?open=N" - 개봉 예정작
	 * 
	 * "/movies/Y
	 * @return
	 */
	@GetMapping("/movies")
	public String movieListPage(
			Model model,
			@RequestParam(defaultValue = "Y")  String open) {
		List list = null;
		System.out.println("[open ]" + open);
		Date openDate = new Date();
		if(open.equals("Y")) {
			 list = movieService.movieYList(openDate);
		}else if(open.equals("N")) {
			 list = movieService.movieNList(openDate);
		}
		List ratings = movieRatingService.movieList();
		
		model.addAttribute("ratings",ratings);
		model.addAttribute("movies", list);
		return "movie-list";
	}
	
//	@GetMapping("/movieRating")
//	public String movieRatingPage() {
//		return "movieRating";
//	}
}
