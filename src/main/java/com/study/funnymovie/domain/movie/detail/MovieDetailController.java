package com.study.funnymovie.domain.movie.detail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.funnymovie.domain.actor.Actor;
import com.study.funnymovie.domain.actor.ActorService;
import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.movie.MovieService;
import com.study.funnymovie.domain.review.Review;
import com.study.funnymovie.domain.review.ReviewService;
import com.study.funnymovie.domain.upfiles.LocalUpFile;
import com.study.funnymovie.domain.upfiles.Upfile;
import com.study.funnymovie.domain.upfiles.UpfileService;

import jakarta.servlet.http.HttpServletResponse;
/**
 * 
 * const b = true;
 * if( b) {}
 * @author ppojo
 *
 */
@Controller
public class MovieDetailController {
	@Autowired
	MovieService movieService;
	@Autowired
	ActorService actorService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	UpfileService upFileService;
	
	@GetMapping(value="/movies/movie-detail")
	public String movieDetail(Model model,@RequestParam Integer movieSeq) {
		
		Movie movie = movieService.movieDetail(movieSeq);

		LocalDate now = LocalDate.now();
		LocalDate openDate = movie.getOpen_date();
		
		Boolean open = null;
		if(now.isAfter(openDate)) {
			 open = Boolean.TRUE;
		}else {
			open = Boolean.FALSE;
		}
		
		
		model.addAttribute("movie", movie);
		model.addAttribute("open", open);
		
		return "movie-detail";
	}
	// /movie/1/detail?type=movie_story
	@GetMapping(value="/movie/{movieSeq}/detail")
	@ResponseBody
	public Object getMovieDetailInfo(@PathVariable Integer movieSeq, @RequestParam String type) {
		Map<String, Object> res = new HashMap<>();
		if(type.equals("movie_story")) {
			// movie.movie_story
			String movieStory = movieService.movieStory(movieSeq);
			res.put("value", movieStory);
		}else if(type.equals("actor")) {
			List<Actor> actors = actorService.findActorByMovie(movieSeq);
			// { actors: [....], director: {...} }
			res.put("value", actors);
		}else if(type.equals("rating")) {
			List<Review> review = reviewService.findReviewByMovie(movieSeq);
			res.put("value", review);
		}else if(type.equals("pic")) {
			List<Upfile> upfile = upFileService.findPictureByMovie(movieSeq);
			System.out.println(upfile);
			res.put("value", upfile);
		}
		return res;
	}	
	@GetMapping(value="/detailImg/{imagePath}")
	public void UserPic(@PathVariable String imagePath,HttpServletResponse res) throws IOException {
		// imagePath = "adoek-383k--gddd.jpg"
		LocalUpFile detailImg = movieService.readDetailImg(imagePath);
		
		String contentType = detailImg.getContentType();
		System.out.println("[content type]"+ contentType);
		OutputStream out = res.getOutputStream();
		
		res.setContentType(contentType);
		
		byte [] data = detailImg.getContent();
		res.setContentLength(data.length);
		
		//ByteArrayInputStream bin = new ByteArrayInputStream(data);
		IOUtils.write(data, out);
		
		out.flush(); // 버퍼에 남아있는 데이터 전부 내려보내라!
	}
}
