package com.study.funnymovie.domain.upfiles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpfileService {
	@Autowired
	UpfileDAO upfileDao;
	
	/**
	 * 주어진 영화의 촬영 사진들 조회
	 * @param movieSeq
	 * @return
	 */
	public List<Upfile> findPictureByMovie(Integer movieSeq) {
		return upfileDao.findPictureByMovie(movieSeq);
	}

}
