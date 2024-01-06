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
public class MoviePoster {
	
	public final static MoviePoster EMPTY_POSTER = new MoviePoster("empty.jpg");
	
	private Integer posterSeq;
	private String moviePath;
	private Integer movie_ref;
	private Integer mainPoster;
	
	public MoviePoster() {}
	
	
	public MoviePoster(String moviePath) {
		this.moviePath = moviePath;
	}


}
