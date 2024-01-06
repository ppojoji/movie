package com.study.funnymovie.domain.actor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.movie.MovieService;

@RestController
public class ActorController {
	@Autowired
	ActorService actorService;
	@Autowired
	MovieService movieService;
	
	@GetMapping(value = "/actor/{actor}/movies")
	public List<Movie> Movie(@PathVariable Integer actor){
		List<Movie> movies = movieService.findOneMovies(actor);
		System.out.println(movies);
		return movies;
	}
}
