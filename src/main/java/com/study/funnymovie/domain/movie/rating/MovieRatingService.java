package com.study.funnymovie.domain.movie.rating;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieRatingService {

	@Autowired
	MovieRatingDao movieRatingDao;
	
	public List movieList() {
		List list = movieRatingDao.movieList();
		
		return list;
	}


}
