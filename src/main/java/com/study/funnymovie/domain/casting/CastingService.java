package com.study.funnymovie.domain.casting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.funnymovie.domain.actor.Actor;
import com.study.funnymovie.domain.actor.Casting;

@Service
@Transactional
public class CastingService {
	@Autowired
	CastingDao castingDao;
	
	/**
	 * 주어진 배우를 영화에 출연진으로 등록함
	 * @param actor - 배우정보
	 * @param movieSeq - 출연 영화
	 * @param castingName - 영화 속 캐릭터 이름
	 * @param role - 주연(L), 조연(S), 단역(E)
	 */
	public Casting addCasting(Actor actor, Integer movieSeq, String castingName, String role) {
		Casting casting = new Casting();
		casting.setCastingName(castingName);
		casting.setActor(actor);
		casting.setMovieSeq(movieSeq);
		casting.setCastingRole(role);
		
		castingDao.addCasting(casting);
		return casting;
	}

	public Casting editCasting(
			Integer actorSeq, 
			Integer movieSeq, 
			String prevRole, 
			String newRole, 
			String castingName) {
		Casting casting = new Casting();
		casting.setActorSeq(actorSeq);
		casting.setMovieSeq(movieSeq);
		casting.setCastingRole(prevRole);
		casting.setCastingName(castingName);
		
		// casting.setCastingRole(newRole);
		
		castingDao.editCasting(casting, newRole);
		return casting;
	}

	public Casting deleteCasting(Integer movieSeq , Integer actorSeq) {
		
		Casting casting = castingDao.findUniqueCasting(movieSeq, actorSeq);
		if (casting == null) {
			// return null;
			throw new RuntimeException("NO_CASTING_INFO");
		}
		castingDao.deleteCasting(casting);
		return casting;
	}
	public List<Casting> findCasting(Integer actorSeq) {
		return castingDao.findCasting(actorSeq);
	}
	/**
	 *  주어진 배우가 주어진 영화에서의 배역 정보(casting) 조회
	 * @param actorSeq
	 * @param movieSeq
	 * @return
	 */
	public Casting findMovieInfo(Integer actorSeq, Integer movieSeq) {
		Casting casting = castingDao.findMovieInfo(actorSeq,movieSeq);
		return casting;
	}

	public void updateCastingInfo(Integer actorSeq) {
		castingDao.updateCastingInfo(actorSeq);
	}
}
