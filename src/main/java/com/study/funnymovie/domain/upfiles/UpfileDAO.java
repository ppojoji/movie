package com.study.funnymovie.domain.upfiles;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UpfileDAO {

	@Autowired
	SqlSession session;
	
	public List<Upfile> findPictureByMovie(Integer movieSeq) {
		return session.selectList("upfileMapper.findPictureByMovie", movieSeq);
	}

}
