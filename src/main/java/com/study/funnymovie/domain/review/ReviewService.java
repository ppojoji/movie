package com.study.funnymovie.domain.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	@Autowired
	ReviewDAO reviewDao;

	
	public List<Review> findReviewByMovie(Integer movieSeq) {
		return reviewDao.findReviewByMovie(movieSeq);
	}
//
//	public List<Review> movieReview() {
//		// TODO Auto-generated method stub
//		return null;
//	}


	public void writeReview(Review review) {
		reviewDao.writeReview(review);
		
	}
	
}
