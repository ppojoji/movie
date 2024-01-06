package com.study.funnymovie.domain.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.funnymovie.domain.actor.Actor;
import com.study.funnymovie.domain.actor.ActorService;
import com.study.funnymovie.domain.actor.Casting;
import com.study.funnymovie.domain.casting.CastingService;
import com.study.funnymovie.domain.movie.Movie;
import com.study.funnymovie.domain.movie.MoviePoster;
import com.study.funnymovie.domain.movie.MoviePosterService;
import com.study.funnymovie.domain.movie.MovieService;
import com.study.funnymovie.domain.upfiles.Upfile;
import com.study.funnymovie.service.FileService;

@RestController
public class AdminController {
	
	@Autowired
	MovieService movieService;
	@Autowired
	ActorService actorService;
	
	@Autowired
	CastingService castingService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	MoviePosterService  moviePosterService;
	
	@PostMapping(value = "/admin/movie")
	public Object insertMovie(
			@RequestParam List<Integer> directorSeqs,
			@RequestParam String title,
			@RequestParam String movieBaseInfo,
			@RequestParam String producer,
			@RequestParam LocalDate openDate,
			@RequestParam Integer grade,
			@RequestParam String movieStory,
			@RequestParam Integer runningTime) {
		
		System.out.println("title: " + title);
		System.out.println("movieBaseInfo: " + movieBaseInfo);
		System.out.println("producer: " + producer);
		System.out.println("openDate: " + openDate);
		System.out.println("runningTime: " + runningTime);
		
		// Integer actorSeq = movieService.findActorSeq(directorSeq);
		// System.out.println("actorSeq: " + actorSeq);
		
		Movie movie = new Movie();
		movie.setSiteurl("http://daum.net");
		movie.setMovie_title(title);
		movie.setMovie_base_info(movieBaseInfo);
		//movie.setMovie_director_ref(directorSeq);
//		movie.
		movie.setMovie_produce(producer);
		movie.setOpen_date(openDate);
		movie.setGrade(grade);
		movie.setMovie_story(movieStory);
		movie.setRunning_time_mins(runningTime);
		
		movieService.insertMovie(movie, directorSeqs);
		return movie;
	}
	
	@PostMapping(value = "admin/movie/movieReg")
	public Movie addMovieReg(@RequestParam String title) {
		return movieService.addMovieReg(title);
	}
	
	@GetMapping(value = "/admin/search/director")
	public List<Actor> SearchDirector(@RequestParam String keyword) {
		System.out.println(keyword);
		
		List<Actor> directors = actorService.SearchDirector(keyword);
		return directors;
	}
	
	@GetMapping(value = "/admin/search/movie")
	public List<Movie> SearchMovie(@RequestParam String keyword) {
		System.out.println(keyword);
		
		List<Movie> movie = movieService.SearchMovie(keyword);
		return movie;
	}
	/**
	 * 감독 등록(모든 정보 입력해서 감독 등록)
	 * @return
	 */
	@PostMapping(value = "/admin/director2")
	public Actor addDirector(
			@RequestParam String actorName,
			@RequestParam String sex,
			@RequestParam LocalDate birth,
			@RequestParam MultipartFile pic) {
		
		Actor actor = new Actor();
		actor.setActor_name(actorName);
		actor.setActor_sex(sex); 
		actor.setActor_birth(birth);
		
		Upfile upfile = fileService.createUpfile(pic);
		String originName = pic.getOriginalFilename();
		actor.setActor_pic(upfile.getFile_path());
		
		actorService.addDirector2(actor);
		
		return actor;
	}
	/**
	 * 감독 등록(간편등록, 이름만 사용해서..)
	 * @param name
	 * @return
	 */
	@PostMapping(value = "/admin/director")
	public Actor addDirector(@RequestParam String name) {
		Actor actor = new Actor();
		actor.setActor_name(name);
		actor.setActor_sex("M"); // FIXME FORM 에 만들어야 함
		actor.setActor_birth(LocalDate.of(1955, 03, 15)); // FIXME 실제 생일 받아와야 함
		
		actorService.addDirector(actor);
		return actor;
		
	}
	/**
	 * 주어진 영화에 출연하는 배우들 반환
	 * @param movieSeq
	 * @return
	 */
	@GetMapping(value = "/admin/movie/actorNameInfo/{movieSeq}")
//	@GetMapping(value = "/admin/movie/{movieSeq}/actors")
	public List<Actor> actorsOfMovie(@PathVariable Integer movieSeq) {
		List<Actor> actors = actorService.actorNameInfo(movieSeq);
		return actors;
	}
	/**
	 * 주어진 영화의 정보
	 * @param movieSeq
	 * @return
	 */
	@GetMapping(value = "/admin/movie/{movieSeq}/basic")
	public Movie movieInfo(@PathVariable String movieSeq) {
//		List<Actor> actorName = actorService.actorNameInfo(movieSeq);
		Movie movie = movieService.movieDetail(movieSeq);
		return movie;
	}
	/**
	 * 영화 포스터 추가 기능
	 * @param movieSeq
	 * @return
	 */
	@PostMapping(value = "/admin/movie/{movieSeq}/poster")
	@ResponseBody
	public MoviePoster insertMoviePoster(
			@PathVariable Integer movieSeq,
			@RequestParam MultipartFile file) {
//		List<Actor> actorName = actorService.actorNameInfo(movieSeq);
		MoviePoster moviePoster = moviePosterService.insertMoviePoster(movieSeq,file);
		return moviePoster;
	}
	/**
	 * 주어진 영화의 영화 포스터를 삭제함
	 * @return
	 */
	@DeleteMapping("/admin/movie/{movieSeq}/poster/{posterSeq}")
	public MoviePoster deleteMoviePoster(
			@PathVariable Integer movieSeq,
			@PathVariable Integer posterSeq) {
		moviePosterService.deleteMoviePoster(movieSeq, posterSeq);
		return null;
		
	}
	/**
	 * 주어진 영화의 대표 이미지 변경
	 * @param movieSeq
	 * @param posterSeq
	 * @return
	 */
	@PutMapping("/admin/movie/{movieSeq}/mainPoster/{posterSeq}")
	public int changeMainPoster(
			@PathVariable Integer movieSeq,
			@PathVariable Integer posterSeq) {
		int max = moviePosterService.changeMainPoster(movieSeq, posterSeq);
		return max;
		
	}
	
	@PostMapping("/admin/actor/{actorSeq}/pic")
	@ResponseBody
	public Actor actorEditPic(@PathVariable Integer actorSeq,@RequestParam MultipartFile file) {
		Actor actor = actorService.actorEditPic(actorSeq,file);
		return actor;
	}
	
	@GetMapping(value = "/admin/movie/searchActor/{keyword}")
	public List<Actor> searchActor(@PathVariable String keyword){
		System.out.println("[keyword]" + keyword);
		List<Actor> actor = actorService.searchActor(keyword);
		return actor;
	}
	/**
	 * 
	 * 새로운 배역 등록(배우를 영화에 등록함) 
	 * movie: 2332
	 * actor: 983
	 * 
	 * castingName: '강우'
	 * role: 'L', 'S', ...
	 * "/admin/movie/1/actor/10
	 */
	@PostMapping(value = "/admin/movie/{movieSeq}/actor/{actorSeq}")
	public Actor addCasting(
			@PathVariable Integer movieSeq, 
			@PathVariable Integer actorSeq, 
			@RequestParam String castingName ,
			@RequestParam String role) {
		System.out.println(movieSeq + ": " + actorSeq + "  " + castingName + " and " + role);
		Actor casting = actorService.addCasting(movieSeq,actorSeq,castingName,role);
		return casting;
	}

	
//	@PostMapping("/admin/casting")
//	@ResponseBody
//	public Object addCasting(@RequestParam Integer movieSeq ,
//			@RequestParam Integer actorSeq, 
//			@RequestParam String castingName, 
//			@RequestParam String role) {
//		castingService.addCasting(null, null, null, null)
//	}
	/**
	 * actorName=이범수&casting=ㅁㅁ& ....
	 * 
------WebKitFormBoundarybAinkppstoa1u1Bu
Content-Disposition: form-data; name="actorName"

이범수
------WebKitFormBoundarybAinkppstoa1u1Bu
Content-Disposition: form-data; name="casting"

ㅁㅁ
------WebKitFormBoundarybAinkppstoa1u1Bu
Content-Disposition: form-data; name="birth"

2023-03-11
------WebKitFormBoundarybAinkppstoa1u1Bu
Content-Disposition: form-data; name="sex"

M
------WebKitFormBoundarybAinkppstoa1u1Bu
Content-Disposition: form-data; name="role"

ㅎㅎㅎㅎ
------WebKitFormBoundarybAinkppstoa1u1Bu--
Content-Disposition: form-data; name="img.jpg"

..lsd.aldkfjalsdkj

	 * 
	 * 
	 */
	/**
	 * 배우(감독)를 새로 입력함. 정식 등록이므로 4가지 값이 있어야 함
	 * @param movieSeq
	 * @param actorSeq
	 * @return
	 */
	@PostMapping(value = "/admin/actor")
	public Actor addActor(
			@RequestParam String actorName,
			@RequestParam String sex,
			@RequestParam LocalDate birth,
			@RequestParam MultipartFile pic,
			
			@RequestParam(required = false) Integer movieSeq,
			@RequestParam(required = false) String role,
			@RequestParam(required = false) String casting
	) 
	{
		Upfile upfile = fileService.createUpfile(pic);
		String originName = pic.getOriginalFilename();
		
		Actor actor = actorService.addActor(actorName,sex,birth, upfile.getFile_path());
		Casting castingInfo = null;
		if (movieSeq != null) {
			castingInfo = castingService.addCasting(actor, movieSeq, casting, role);
		}
		return actor;
	}
	@PostMapping(value = "/admin/actor/easy")
	public Actor addEasyActor(@RequestParam String actorName) {
		Actor actor = actorService.addActor(actorName);
		return actor;
	}
	/**
	 * 배우 기본 정보 수정
	 * @param actorName
	 * @param birth
	 * @param sex
	 * @param pic
	 * @param movieSeq
	 * @param role
	 * @param casting
	 * @return
	 */
	@PostMapping(value = "/admin/edit/actor")
	public Actor editActorBasicInfo(
			@RequestParam Integer actorSeq, 
			@RequestParam String actorName,
			@RequestParam LocalDate birth,
			@RequestParam String sex
			
	) 
	{
		/* @RequestParam MultipartFile pic
		 * @RequestParam(required = false) Integer movieSeq,
			@RequestParam(required = false) String role,
			@RequestParam(required = false) String casting
		 */
		Actor actor = new Actor();
		Casting castingInfo = null;
		
		actor.setActor_seq(actorSeq);
		actor.setActor_name(actorName); 
		actor.setActor_birth(birth);
		actor.setActor_sex(sex);
		
		// Upfile upfile = fileService.createUpfile(pic);
		actor = actorService.editActor(actor);
		/*
		if (movieSeq != null) {
			castingInfo = castingService.editCasting(actor, movieSeq, role, casting);
		} 
		*/
		//actorService.editActor();
		
		return actor;
	}
	
	@GetMapping("/admin/movie/keyword")
	@ResponseBody
	public List<Movie> searchMovie(@RequestParam String keyword) {
		System.out.println(">>> " + keyword);
		List<Movie> movie = movieService.SearchMovie(keyword);
		return movie;
	}
	
	@GetMapping("/admin/actors")
	@ResponseBody
	public List<Actor>actors(@RequestParam(required = false, defaultValue = "5") Integer size,
			@RequestParam Integer lastActorSeq,
			@RequestParam(required = false) String role
			){
		List<Actor>actors = actorService.actors(size,lastActorSeq,role);
		return actors;
	}

	@PutMapping("/admin/actor/{actorSeq}/actorName")
	@ResponseBody
	public Actor updateActorName(
			@PathVariable Integer actorSeq,
			@RequestParam String name) {
		System.out.println(actorSeq + ", " + name);
		
		Actor actor = actorService.updateActorName(actorSeq,name);
		return actor;
	}
	
	// @PostMapping("/admin/casting/delete") 
	@DeleteMapping("/admin/casting/movie/{movieSeq}/actor/{actorSeq}")
	@ResponseBody
	public Casting deleteCasting(
			@PathVariable Integer movieSeq 
			, @PathVariable Integer actorSeq) {
		
		Casting casting = castingService.deleteCasting(movieSeq,actorSeq);
		return casting;
	}
}
