package com.study.funnymovie.domain.actor;
/**
 * 배우를 나타냄
 * @author ppojo
 *
 */

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Actor {
	
	@EqualsAndHashCode.Include
	private Integer actor_seq;
	
	private String actor_name ; 
	private LocalDate actor_birth;
	private String actor_sex;
	private String actor_pic; // "duejau-ajndkke-sjdjfjkd.jfif"
	
	/**
	 * 원래는 Casting dto 에 있어야 하는 정보입니다.
	 * 일단 이곳에 배역 정보를 넣습니다.
	 * 이거는 정석은 아님(일단 꼼수)
	 */
	String casting_name;
	String casting_role;
	
	/*
	public boolean Equals(Actor other) {
		
		return this.actor_seq.equals(other.actor_seq)
	}
	*/
}
