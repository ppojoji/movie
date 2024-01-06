package com.study.funnymovie.domain.actor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActorDAO {

	@Autowired
	SqlSession session;
	
	public List<Actor> findActorByMovie(Integer movieSeq) {
		return session.selectList("actorMapper.findActorByMovie",movieSeq);
	}
	public List<Actor> findDirectorByMovie(Integer movieSeq) {
		return session.selectList("actorMapper.findDirectorByMovie",movieSeq);
	}
	/**
	 * 주어진 PK 의 배우를 조회함
	 * @param actorSeq
	 * @return
	 */
	public Actor findActor(Integer actorSeq) {
		return session.selectOne("actorMapper.findActor",actorSeq);
	}
	
	public List<Actor> SearchDirector(String keyword) {
		return session.selectList("actorMapper.SearchDirector",keyword);
	}
	public void addDirector(Actor actor) {
		session.insert("actorMapper.addDirector",actor);
	}
	
	/**
	 * 주어진 영화에 출연한 배우들 조회함
	 * @param movieSeq - 조회할 영화 PK
	 * @return 
	 */
	public List<Actor> actorNameInfo(Integer movieSeq) {
		return session.selectList("actorMapper.actorNameInfo",movieSeq);
	}
	public List<Actor> searchActor(String keyword) {
		return session.selectList("actorMapper.searchActor",keyword + "%");
	}
	public Object addCasting(Integer movieSeq, Integer actorSeq, String castingName, String role) {

		Map map = new HashMap<>();
		map.put("movieSeq", movieSeq);
		map.put("actorSeq", actorSeq);
		map.put("castingName", castingName);
		map.put("role", role);
		
		return session.insert("actorMapper.addCasting",map);
	}
	
	public void addActor(Actor newActor) {
		session.insert("actorMapper.addActor",newActor);
	}
	
	public List<Actor> findAllActor() {
		return session.selectList("actorMapper.findAllActor");
	}
	/**
	 * 배우의 기본정보를 수정함(name, birth, sex)
	 * @param actor
	 */
	public void editActor(Actor actor) {
		session.update("actorMapper.editActor",actor);
	}
	/**
	 * 배우의 사진 정보를 수정함
	 * @param actor
	 */
	public void editActorPic(Actor actor) {
		session.update("actorMapper.editActorPic",actor);
	}
	public List<Actor> actors(Integer size, Integer lastActorSeq, String role) {
		// size: 5, 1 (송, 박)
		// size: 5, page: 2 ()
		// 
		Map map = new HashMap<>();
		map.put("size", size);
		map.put("lastActorSeq", lastActorSeq);
		map.put("role", role);
		return session.selectList("actorMapper.actors", map);
	}
	public void addDirector2(Actor actor) {
		session.insert("actorMapper.addDirector2",actor);
	}
	public void updateActorName(Actor actor) {
		session.update("actorMapper.updateActorName", actor);
		
	}
	public void updateCastingName(Actor actor) {
		session.update("actorMapper.updateCastingName", actor);
	}
	
}
