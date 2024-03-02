package com.study.funnymovie.domain.movie;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.funnymovie.domain.actor.Actor;

@Repository
public class MovieDao {
	
	@Autowired
	SqlSession session; 
	
	public List<Movie> allMovie() {
		return session.selectList("movieMapper.allMovie");
	}

	public List movieYList(Date openDate) {
		return session.selectList("movieMapper.movieYList",openDate);
	}

	public List movieNList(Date openDate) {
		return session.selectList("movieMapper.movieNList",openDate);
	}
	/**
	 * 영화 기본 정보를 반환함
	 * 
	 * @param movieSeq
	 * @return
	 */
	public Movie movieDetail(Integer movieSeq) {
		return session.selectOne("movieMapper.movieDetail", movieSeq);
	}

	public String movieStory(Integer movieSeq) {
		
		return session.selectOne("movieMapper.movieStory",movieSeq);
	}

	public List<Movie> findOneMovies(Integer actor) {
		return session.selectList("movieMapper.findOneMovies",actor);
	}

	public void insertMovie(Movie movie) {
		session.insert("movieMapper.insertMovie",movie);
		
	}

	public List<Movie> searchMovie(String keyword) {
		return session.selectList("movieMapper.searchMovie",keyword+"%");
	}

	public void addMovieReg(String title) {
		session.insert("movieMapper.addMovieReg",title);
	}

	public void changeMoviePoster(Movie movie) {
		session.update("movieMapper.changeMoviePoster",movie);
	}
	/**
	 * 영화 평점 업데이트
	 * @param movie
	 */
	public void updateReviewScore(Movie movie) {
		session.update("movieMapper.updateReviewScore",movie);
	}
	
}
