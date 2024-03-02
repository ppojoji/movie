package com.study.funnymovie.domain.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.funnymovie.domain.upfiles.Upfile;
import com.study.funnymovie.service.FileService;

@Service
public class MoviePosterService {

	@Autowired
	FileService fileService;
	
	@Autowired
	MovieDao movieDao;
	
	@Autowired
	MoviePosterDao moviePosterDao;
	
	/**
	 * 영화 포스터 추가
	 * (X)영화 포스터 교체
	 * @param movieSeq
	 */
	public MoviePoster insertMoviePoster(Integer movieSeq, MultipartFile file) {
		// 1. 새로운 포스터 이미지을 디스크에 저장함
		Upfile upfile = fileService.createUpfile(file);
		
		
		// 2. 영화 정보를 읽어들임
		Movie movie = movieDao.movieDetail(movieSeq);
		
		// 3. 영화 포스터 테이블(movie_poster insert into... )에 포스터 추가
		MoviePoster moviePoster = new MoviePoster();
		moviePoster.setMovie_ref(movie.getMovie_seq());
		moviePoster.setMoviePath(upfile.getFile_path());
		
		moviePosterDao.insertPoster(moviePoster);
		return moviePoster;
//		
//		// 3. 기존 배우 사진을 디스크에서 삭제함
//		//fileService.deletefile(movie.getMovie_poster());
//		
//		// 4. 새로운 파일 경로를 배우 정보에 업데이트
//		movie.setMovie_poster(upfile.getFile_path());
//		
//		movieDao.changeMoviePoster(movie);
		
//		return movie;
	}
	/**
	 * 주어진 영화 포스터를 삭제함
	 * @param movieSeq - 영화
	 * @param posterSeq  - 포스터
	 * @return
	 */
	public MoviePoster deleteMoviePoster(Integer movieSeq, Integer posterSeq) {
		MoviePoster moviePoster = moviePosterDao.findMoviePoster(posterSeq);
		
		moviePosterDao.deleteMoviePoster(moviePoster);
		System.out.println(moviePoster.getMoviePath());
		fileService.deletefile(moviePoster.getMoviePath());
		return moviePoster;
	}
	public int changeMainPoster(Integer movieSeq, Integer posterSeq) {
		MoviePoster moviePoster = moviePosterDao.findMoviePoster(posterSeq);
		Integer currentMax = moviePosterDao.maxPoster(movieSeq);
	
		moviePoster.setMainPoster(currentMax + 1);
		
		moviePosterDao.changeMainPoster(moviePoster);
		return moviePoster.getMainPoster();
	}
}
