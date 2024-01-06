package com.study.funnymovie.domain.upfiles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalUpFile {
	private Integer Seq;
	private String OriginalName;
	private String GenName; 
	private Integer FileSize; 
	private String ContentType; 
	private Integer Post; 
	private String Origin;
	private byte [] data;
	
	public byte [] getContent () {
		return this.data;
	}
	public void setContent(byte[] data) {
		this.data = data;
	} 
}
