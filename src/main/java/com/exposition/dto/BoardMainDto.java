package com.exposition.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class BoardMainDto {

	private Long id;
	private String title;
	private String content;
	private String savePath;
	private String backSavePath;
	
	@QueryProjection
	public BoardMainDto(Long id, String title, String content, String savePath) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.savePath = savePath;
	}
	
	@QueryProjection
	public BoardMainDto(Long id, String title, String content, String savePath, String backSavePath) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.savePath = savePath;
		this.backSavePath = backSavePath;
	}
	
}
