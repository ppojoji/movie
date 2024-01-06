package com.study.funnymovie.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
//
//	public User(String id, String pass) {
//		// TODO Auto-generated constructor stub
//	}
	Integer user_seq;
	String user_id;
	String user_pass;
	String user_nic;
}
