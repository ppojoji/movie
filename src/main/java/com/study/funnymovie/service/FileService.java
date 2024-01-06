package com.study.funnymovie.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.funnymovie.domain.upfiles.Upfile;

@Service
public class FileService {
	
	@Value("${fmovie.movie.pic}")
	String rootDir;
	
	/**
	 * 클라이언트가 업로드한 파일을 디스크에 저장하고 유일한 이름을 부여함.
	 * @param file
	 * @return
	 */
	public Upfile createUpfile(MultipartFile file) {
		String originName = file.getOriginalFilename(); // 김민재.jpg
		/*
		 * my.uq.file.jpg
		 * 김민재.png
		 * 01234
		 */
		int pos = originName.lastIndexOf('.'); // 4
		String ext = originName.substring(pos); // 1) originName에서  확장자를 따냄
		String uqFileName = UUID.randomUUID().toString() + ext; 
		// 123e4567-e89b-12d3-a456-556642440000.jpg
		
		Path filepath = Paths.get(rootDir, uqFileName);
		// d:/uproot/movie/123e4567-e89b-12d3-a456-556642440000.jpg
		
		try {
			byte [] data = file.getBytes();
			Files.write(filepath, data, StandardOpenOption.CREATE_NEW);
			
			Upfile upfile = new Upfile(); 
			upfile.setFile_path(uqFileName);
			
			return upfile;
		} catch (IOException e) {
			throw new RuntimeException("fail to read file data");
		}
		
	}
	/**
	 * 주어진 파일 디스크에서 삭제
	 * @param fileName 
	 * @return
	 */
	public Upfile deletefile(String fileName) {
		if(fileName == null) {
			return null;
		}
		Path imagePath = Paths.get(rootDir, fileName);
		File file = imagePath.toFile();
		if (file.exists()) {
			file.delete();
		}
		return null;
	}
	public void insertfile() {
		// TODO Auto-generated method stub
		
	}
}
