package com.study.funnymovie.domain.movie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.funnymovie.domain.actor.Actor;
import com.study.funnymovie.domain.actor.ActorDAO;
import com.study.funnymovie.domain.casting.CastingService;
import com.study.funnymovie.domain.review.Review;
import com.study.funnymovie.domain.review.ReviewDAO;
import com.study.funnymovie.domain.upfiles.LocalUpFile;
import com.study.funnymovie.domain.upfiles.Upfile;
import com.study.funnymovie.service.FileService;

@Service
public class MovieService {

	@Autowired
	ReviewDAO reviewDao;
	
	@Autowired 
	MovieDao movieDao;
	
	@Autowired
	ActorDAO actorDao;
	
	@Autowired
	CastingService castingService;
	@Value("${fmovie.movie.pic}")
	String movieImgRootDir;
	
	@Autowired
	FileService fileService;
	
	public List<Movie> allMovie() {
		return movieDao.allMovie();
	}

	public List movieYList(Date openDate) {
		return movieDao.movieYList(openDate);
	}

	public List movieNList(Date openDate) {
		return movieDao.movieNList(openDate);
	}
	/**
	 * 편집용 영화 정보 - 등장인물, 감독 등 모든 정보를 다 반환해야 함
	 * @return
	 */
	public Movie findMovieInfo(Integer movieSeq) {
		Movie movie = this.movieDetail(movieSeq);
		
		// castingService.findCasting(movieSeq);
		// Actor director = actorDao.findActor(movie.getMovie_director_ref());  // "9";
		
		// System.out.println("박쥐 감독: " + director);
		
		List<Actor> actors = actorDao.findActorByMovie(movieSeq);
		List<Actor> directors = new ArrayList<>();
		for (Actor actor : actors) {
			if(actor.getCasting_role().equals("D")) {
				directors.add(actor);
			}
		}
		
		movie.setDirectors(directors);
		
		movie.setActors(actors);
		return movie;
	}
	/**
	 * 주어진 영화의 기본 정보(제목, 상세 내용, 사진 등. 출연진 목록은 없음)
	 * @param movieSeq
	 * @return
	 */
	public Movie movieDetail(Integer movieSeq) {
		return movieDao.movieDetail(movieSeq);
	}
	/**
	 * Movie -> String -> Map<String, Object>
	 * @param movieSeq
	 * @return
	 */
	public String movieStory(Integer movieSeq) {
		return movieDao.movieStory(movieSeq);
//		return null;
	}
	public Object moviePicture( ) {
		return null;
	}
	public Object movieActor() {
		return null;
	}
	public Object movieRating() {
		return null;
	}

	public LocalUpFile readDetailImg(String DetailImgName) {
		File localFile = new File(movieImgRootDir,DetailImgName);
		
		LocalUpFile upfile = new LocalUpFile();
		try {
			String contentType = Files.probeContentType(localFile.toPath());
			upfile.setContentType(contentType);
			byte [] fileContents = Files.readAllBytes(localFile.toPath());
			upfile.setContent(fileContents);
			return upfile;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Movie> findOneMovies(Integer actor) {
		List<Movie> movies = movieDao.findOneMovies(actor);
		
		return movies;
	}
	/**
	 * 새로운 영화 등록
	 * @param movie
	 */
	public void insertMovie(Movie movie, List<Integer> directorSeqs) {
		movieDao.insertMovie(movie);
		
	
		for (Integer directorSeq : directorSeqs) {
			Actor director = actorDao.findActor(directorSeq);
			castingService.addCasting(director, movie.getMovie_seq(), director.getActor_name(), "D");
		}
		// (m:25, ac:15, role: 'D') 
		
	}

	public Movie addMovieReg(String title) {
		Movie movie = new Movie();
		movie.setMovie_title(title);
		movie.setSiteurl("");
		movie.setMovie_base_info("");
		movie.setRunning_time_mins(0);
		movie.setOpen_date(LocalDate.now());
		movie.setGrade(0);
		movie.setMovie_story("");
		movieDao.insertMovie(movie);
		return movie;
		// movieDao.findMovieByTitle(title);
	}

	public List<Movie> SearchMovie(String keyword) {
		List<Movie> movie = movieDao.searchMovie(keyword);
		return movie;
	}
	/**
	 * 주어진 영화 펑점 업데이트하고 새로운 평점값을 반환함
	 * @param review
	 * @return 새로운 영화 평점
	 */
	public double updateReviewScore(Review review) {
		// 1. Movie movie = ...;
		Movie movie = this.movieDetail(review.getMovie_ref());
		
		// 2. 리뷰 다 가져옴
		List<Review> reviews = reviewDao.findReviewByMovie(review.getMovie_ref());
		
		// 3. 평점 계산 = 합 / 갯수
		double total = 0;
		for (Review rv : reviews) {
			total = total + rv.getRv_score();
		}
		double avr = total/reviews.size();
		movie.setMovie_avg_score(avr);
		// 4. 평점 업데이트
		movieDao.updateReviewScore(movie);
		
		return avr;
	}
}
