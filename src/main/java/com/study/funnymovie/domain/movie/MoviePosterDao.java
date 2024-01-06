package com.study.funnymovie.domain.movie;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MoviePosterDao {

	@Autowired
	SqlSession session;

	public void insertPoster(MoviePoster moviePoster) {
		session.insert("moviePostMapper.insertPoster",moviePoster);
		
	}

	public MoviePoster findMoviePoster(Integer posterSeq) {
		return session.selectOne("moviePostMapper.findMoviePoster",posterSeq);
	}
	/**
	 * 영화 포스터 삭제
	 * @param moviePoster
	 */
	public void deleteMoviePoster(MoviePoster moviePoster) {
		session.delete("moviePostMapper.deleteMoviePoster", moviePoster);
		
	}
	
	public Integer maxPoster(Integer mainPoster) {
		return session.selectOne("moviePostMapper.maxPoster", mainPoster);
	}

	public void changeMainPoster(MoviePoster moviePoster) {
		session.update("moviePostMapper.changeMainPoster",moviePoster);
	}
	
}
