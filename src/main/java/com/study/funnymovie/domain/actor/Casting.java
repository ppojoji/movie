package com.study.funnymovie.domain.actor;

import com.study.funnymovie.domain.movie.Movie;

import lombok.Getter;
import lombok.Setter;

/**
 * 배역 정보
 * @author ppojo
 *
 */
@Getter
@Setter
public class Casting {

	Integer movieSeq; // 1
	
	Integer actorSeq;
	
	// 실제 배우
	Actor actor;
	
	Movie movie;
//	String movieName;
	String castingName;
	String castingRole; // 주연 조연
}
