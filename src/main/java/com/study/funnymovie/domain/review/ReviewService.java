package com.study.funnymovie.domain.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.movie.MovieService;
import com.study.funnymovie.domain.user.User;

@Service
public class ReviewService {
	@Autowired
	ReviewDAO reviewDao;
	
	@Autowired
	MovieService movieService;
	
	public List<Review> findReviewByMovie(Integer movieSeq) {
		return reviewDao.findReviewByMovie(movieSeq);
	}

	public void writeReview(Review review) {
		reviewDao.writeReview(review);
	}

	public Review updateReview(User reviewOwner, Review updateData) {
		// 자기 리뷰인지
		// 실제로 리뷰가 있는지..
		Review review = this.reviewDao.findReviewSeq(updateData.getRv_seq());
		// review == null  일 수도 있다!!
		if (review.getUser_ref().equals(reviewOwner.getUser_seq())) {
			// 자기 리뷰가 맞음.
			review.setRv_comment(updateData.getRv_comment());
			review.setRv_score(updateData.getRv_score());
			
			reviewDao.updateReview(review);
			
			movieService.updateReviewScore(review);
		}
		return review;
	}
	/**
	 * 주어진 리뷰 삭제
	 * @param reviewSeq - 삭제할 리뷰 PK
	 * @param loginUser - 리뷰 작성자
	 */
	public Review deleteReview(Integer reviewSeq, User loginUser) {
		Review review = this.reviewDao.findReviewSeq(reviewSeq);
		// review == null  일 수도 있다!!
		if (review.getUser_ref().equals(loginUser.getUser_seq())) {
			reviewDao.deleteReview(reviewSeq);
		}
		return review;
	}

	public Object findReviewByUser(Integer userSeq) {
		return reviewDao.findReviewByUser(userSeq);
	}
}
