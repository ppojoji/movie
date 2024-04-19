package com.study.funnymovie.domain.review;

import com.study.funnymovie.domain.movie.Movie;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자가 영화에 남긴 리뷰(별점도 포함되어 있다!)
 * @author ppojo
 *
 */
@Getter
@Setter
public class Review {
	private Integer rv_seq;
	private Integer rv_score;
	private String rv_comment;
	/**
	 * 이것도 마찬가지로 꼼수입니다.
	 */
	private Integer user_ref;
	private String user_id;
	// 원래는 이렇게 받아야 함
	// private User writer;
	
	private Integer movie_ref;
	private Movie movie;
}
