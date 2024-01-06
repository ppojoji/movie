package com.study.funnymovie.domain.movie.rating;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MovieRatingDao {

	@Autowired
	SqlSession session;
	
	public List movieList() {
		List list = session.selectList("MovieRatingMapper.movieList");
		return list;
	}

}
