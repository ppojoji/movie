package com.study.funnymovie.domain.review;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.user.User;

@Repository
public class ReviewDAO {

	@Autowired
	SqlSession session;

	public List<Review> findReviewByMovie(Integer movieSeq) {
		return session.selectList("reviewMapper.findReviewByMovie",movieSeq);
	}

	public void writeReview(Review review) {
		session.insert("reviewMapper.writeReview",review);
	}

	public void updateReview(Review review) {
		session.update("reviewMapper.updateReview",review);
	}

	public Review findReviewSeq(Integer rv_seq) {
		return session.selectOne("reviewMapper.findReviewSeq",rv_seq);
	}

	public void deleteReview(Integer reviewSeq) {
		session.delete("reviewMapper.deleteReview",reviewSeq);
	}

	public Object findReviewByUser(Integer userSeq) {
		return session.selectList("reviewMapper.findReviewByUser",userSeq);
	}
	
}
