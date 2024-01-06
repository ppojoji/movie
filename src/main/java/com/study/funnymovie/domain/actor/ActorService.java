package com.study.funnymovie.domain.actor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.study.funnymovie.domain.upfiles.Upfile;
import com.study.funnymovie.service.FileService;

@Service
@Transactional
public class ActorService {
	
	@Autowired
	ActorDAO actorDao;
	
	@Autowired
	FileService fileService;
	
	/**
	 * 주어진 영화의 출연진 조회
	 * @param movieSeq 조회할 영화
	 * @return 출연진들
	 */
	public List<Actor> findActorByMovie(Integer movieSeq) {
		List<Actor> actors = actorDao.findActorByMovie(movieSeq); 
		return actors;
	}
	/*
	public List<Actor> findDirectorByMovie(Integer movieSeq) {
		List<Actor> actor = actorDao.findDirectorByMovie(movieSeq); 
		return actor;
	}
	*/

	public Actor findActor(Integer actorSeq) {
		Actor actor = actorDao.findActor(actorSeq);
		return actor;
	}
	
	public List<Actor> SearchDirector(String keyword) {
		List<Actor> directors = actorDao.SearchDirector(keyword+"%"); // 박%
		return directors;
	}

	public void addDirector(Actor actor) {
		actorDao.addDirector(actor);
		
	}

	public List<Actor> actorNameInfo(Integer movieSeq) {
		return actorDao.actorNameInfo(movieSeq);
	}

	public List<Actor> searchActor(String keyword) {
		return actorDao.searchActor(keyword);
	}

	public Actor addCasting(Integer movieSeq, Integer actorSeq, String castingName, String role) {
		actorDao.addCasting(movieSeq,actorSeq,castingName,role);
		List<Actor> actors = actorDao.actorNameInfo(movieSeq);
		 // actors.find((actor) => actor.seq === actorSeq)
//		Actor casting = actors
//			.stream()
//			.filter((actor) -> actor.getActor_seq().equals(actorSeq))
//			.findFirst().get();
		for (Actor a : actors) {
			if (a.getActor_seq().equals(actorSeq)) {
				return a;
			}
		}
		return null;
	}
	/**
	 * 배우 간편 등록 - 이름으로 간편하게 등록함(성별은 남자로 함)
	 * @param actorName
	 * @return
	 */
	public Actor addActor(String actorName) {
		return this.addActor(actorName, "M", null, null);
	}
	/**
	 * 신규 배우 등록
	 * @param actorName
	 * @param birth 
	 * @param role 
	 * @param sex 
	 * @return
	 */
	public Actor addActor(String actorName, String sex, LocalDate birth, String picPath) {
		System.out.println("actorName " + actorName);
		System.out.println("sex " + sex);
//		System.out.println("role " + role);
		System.out.println("birth " + birth);
		
		Actor newActor = new Actor();
		newActor.setActor_name(actorName);
		newActor.setActor_sex(sex);
//		newActor.setCasting_role(role);
		newActor.setActor_birth(birth);
		newActor.setActor_pic(picPath);
		
		actorDao.addActor(newActor);
		return newActor;
	}
	
	public List<Actor> findAllActor() {
		return actorDao.findAllActor();
	}

	public Actor editActor(Actor actor) {
		
		actorDao.editActor(actor);
		return actor;
	}

	public List<Actor> actors(Integer size, Integer lastActorSeq, String role) {
		// 메소드 오버라이딩 메소드 이름(인자, 인자) 
		//              메소ㅡ 이름( 만자, 인자, ㅇ인자) =
		return actorDao.actors(size,lastActorSeq, role);
	}
	
	public List<Actor> actors(Integer size, Integer lastActorSeq) {
		// 기존 20군데
		return actors(size, lastActorSeq, null);
	}

	public void addDirector2(Actor actor) {
		actorDao.addDirector2(actor);
	}
	/**
	 * 배우 및 감독의 실명 변경
	 * @param actorSeq - 배우 및 감독 seq
	 * @param name - 새로운 이름
	 * @return
	 */
	public Actor updateActorName(Integer actorSeq, String name) {
		Actor actor =  actorDao.findActor(actorSeq);
		actor.setActor_name(name);
		actorDao.updateActorName(actor);
		return actor;
	}

	public Actor updateCastingName(Integer actorSeq, String casting,String role) {
		Actor actor =  actorDao.findActor(actorSeq);
		actor.setCasting_name(casting);
		actor.setCasting_role(role);
		
		actorDao.updateCastingName(actor);
		return actor;
	}

	public Actor actorEditPic(Integer actorSeq, MultipartFile file) {
		// 1. 새로운 파일을 디스크에 저장함
		Upfile upfile = fileService.createUpfile(file);
		
		// 2. 배우 정보를 읽어들임
		Actor actor = actorDao.findActor(actorSeq);
		
		// 3. 기존 배우 사진을 디스크에서 삭제함
		fileService.deletefile(actor.getActor_pic());
		
		// 4. 새로운 파일 경로를 배우 정보에 업데이트
		actor.setActor_pic(upfile.getFile_path());
		actorDao.editActorPic(actor);
		
//		if(!upfile.getFile_path())
//		return actorDao;
		return actor;
	}

}
