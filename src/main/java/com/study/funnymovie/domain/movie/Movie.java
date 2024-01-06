package com.study.funnymovie.domain.movie;

import java.time.LocalDate;
import java.util.List;

import com.study.funnymovie.domain.actor.Actor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Movie {
//	`movie_seq` INT NOT NULL AUTO_INCREMENT,
	private Integer movie_seq;
	private String movie_title;
	private String siteurl;
	//private Integer movie_director_ref;
	private String movie_base_info;
	private Integer running_time_mins;
	private LocalDate open_date;
	/**
	 * 영화 관람 등급인데 나이로 처리함 [0, 12, 15, 18, 255]
	 */
	private Integer grade; // 12세, 
	private String movie_story;
	private Double movie_avg_score;
	private String movie_produce;
	
	private String movie_poster; // "a|b|c|d"(X) 사파의 방식!! 최후의 수단
	
	private List<MoviePoster> movie_posters; // [ 'a', 'c', 'd']
	/**
	 * 이것도 꼼수
	 */
	private String casting_name;
	
	/**
	 * 영화 출연 배우들
	 */
	private List<Actor> actors;
	/**
	 * 영화 감독
	 */
	private List<Actor> directors;
	
	public MoviePoster getMainPoster() {
		// mainPoster 값이 제일 높은 포스터를 찾아야 함
		// TODO
		if(movie_posters.size() > 0) {
			MoviePoster maxPoster =  this.movie_posters.get(0);
			for (MoviePoster poster : movie_posters) {
				if(poster.getMainPoster() > maxPoster.getMainPoster()) {
					maxPoster = poster;
				}
			}
			return maxPoster;
		}else {
			return MoviePoster.EMPTY_POSTER;
		}
	}
	/**
	 * 메인 포스터 경로만 반환함
	 * @return
	 */
	public String getMainPosterPath() {
		MoviePoster poster = this.getMainPoster();
		return poster.getMoviePath();
	}
}
