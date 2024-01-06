package com.study.funnymovie.domain.upfiles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Upfile {
	private Integer file_seq;
	private String file_path;
	private Integer movie_ref;
	
}
