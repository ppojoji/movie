package com.study.funnymovie.domain.review;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
