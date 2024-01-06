package com.study.funnymovie.domain.casting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.study.funnymovie.domain.actor.Casting;

@Repository
public class CastingDao {

	@Autowired
	SqlSession session;
	
	public void addCasting(Casting casting) {
		session.insert("castingMapper.addCasting",casting);
		
	}

	public void editCasting(Casting casting, String newRole) {
		Map map = new HashMap<>();
		map.put("casting", casting);
		map.put("newRole", newRole);
		
		session.update("castingMapper.editCasting",map);
	}

	public List<Casting> findCasting(Integer actorSeq) {
		System.out.println("actorSeq >>> " + actorSeq);
		return session.selectList("castingMapper.findCasting", actorSeq);
	}

	public Casting findMovieInfo(Integer actorSeq, Integer movieSeq) {
		Map map = new HashMap<>();
		map.put("actorSeq", actorSeq);
		map.put("movieSeq", movieSeq);
		
		return session.selectOne("castingMapper.findMovieInfo",map);
	}

	public void updateCastingInfo(Integer actorSeq) {
		session.update("castingMapper.updateCastingInfo",actorSeq);
	}

	public Casting findUniqueCasting(Integer movieSeq, Integer actorSeq) {
		Map map = new HashMap<>();
		map.put("actorSeq", actorSeq);
		map.put("movieSeq", movieSeq);
		return session.selectOne("castingMapper.findUniqueCasting", map);
	}

	public void deleteCasting(Casting casting) {
		session.delete("castingMapper.deleteCasting",casting);
	}

}
