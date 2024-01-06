package com.study.funnymovie;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.study.funnymovie.domain.movie.Movie;

class LombokTest {

	@Test
	void test() {
		Movie m = new Movie();
		m.setGrade(-33);
	}

}
