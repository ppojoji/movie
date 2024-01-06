package com.study.funnymovie.domain.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.funnymovie.domain.actor.Actor;
import com.study.funnymovie.domain.actor.ActorService;
import com.study.funnymovie.domain.actor.Casting;
import com.study.funnymovie.domain.casting.CastingService;
import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.movie.MovieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminPageController {
	
	@Autowired
	MovieService movieService;
	@Autowired
	ActorService actorService;
	@Autowired
	CastingService castingService;
	
	/**
	 * 관리자 메인 페이지
	 * @return
	 */
	@GetMapping(value = "/admin")
	public String PageAdmin() {
		return "admin/admin";
	}
	
	/**
	 * 영화 등록 페이지
	 * @return
	 */
	@GetMapping("/admin/reg/movie")
	public String pageRegMovie() {
		return "admin/movie-reg";
	}
	
	@GetMapping("/admin/edit/movie")
	public String pageMovieEdit(Model model) {
		
		List<Movie> movies = movieService.allMovie();
		model.addAttribute("movies", movies);
		return "admin/movie-edit-list";
	}
	
	@GetMapping("/admin/edit/movie-detail")
	public String pageMovieDetail(@RequestParam Integer movieSeq, Model model) {
		
		Movie movie =movieService.findMovieInfo(movieSeq);
		
		System.out.println("director" + movie.getDirectors());
		model.addAttribute("movie",movie);
		
//		actorSer
		
		return "admin/movie-detail";
	}
	
	/**
	 * 배우 등록 페이지
	 * @return
	 */
	@GetMapping("/admin/reg/actor")
	public String pageRegActor() {
		return "admin/actor-reg";
	}
	
	/**
	 * 배우 수정 페이지
	 * @return
	 */
	@GetMapping("/admin/edit/actor")
	public String pageEditActor(
			@RequestParam Integer actorSeq, Model model) {
		System.out.println("actorSeq " + actorSeq);
		
		Actor actor = actorService.findActor(actorSeq); 
		//Movie movie = movieService.movieDetail(movieSeq);
		List<Casting> castings = castingService.findCasting(actorSeq);
		
		System.out.println("actor " + actor.getActor_name());
		System.out.println("casting: " + castings);
		for (Casting c : castings) {
			System.out.println(c.getMovie());
		}
		//System.out.println("casting " + casting.getCastingName());
		
		model.addAttribute("actor",actor);
		model.addAttribute("castings",castings);
		
		//model.addAttribute("movie",movie);
		
		return "admin/actor-edit";
	}
	
	/**
	 * 배우 목록 페이지
	 * @return
	 */
	@GetMapping("/admin/list/actor")
	public String pageListActor( Model model){
		List<Actor> actor = actorService.actors(5,-1);
		List<Movie> movie = movieService.allMovie();
		model.addAttribute("actors",actor);
		model.addAttribute("movies",movie);
		
		// List<Actor> empty = new ArrayList<>();
		// model.addAttribute("actors",empty);
		return "admin/actor-list";
	}
	/**
	 * 주어진 배우가 주어진 영화에서의 출연정보를 조회함
	 * 
	 * */
	 @GetMapping("/admin/casting/actor/{actorSeq}/movie/{movieSeq}")
	 @ResponseBody
	 public Object movieInfo(
			 @PathVariable Integer actorSeq,
			 @PathVariable Integer movieSeq) {
		 
		 System.out.println(actorSeq + ", " + movieSeq);
		 Casting casting = castingService.findMovieInfo(actorSeq,movieSeq);
		 return casting;
	 }
	 
	 /**
	  * 배역 수정
	  * 
	  */
	 @PostMapping("/admin/casting/actorUd")
	 @ResponseBody
	 public Object updateCastingInfo(
			 @RequestParam Integer actorSeq,
			 @RequestParam Integer movieSeq,
			 @RequestParam String prevRole,
			 @RequestParam String newRole,
			 @RequestParam String castingName
			 ) {
		 Casting casting = castingService.editCasting(
				 actorSeq,
				 movieSeq,
				 prevRole,
				 newRole,
				 castingName);
		 return casting;
	 }
	 
	 /**
	 * 감독 등록 페이지
	 * @return
	 */
	@GetMapping("/admin/reg/director")
	public String RegDirector( Model model){
		
		return "admin/director-reg";
	}
}
