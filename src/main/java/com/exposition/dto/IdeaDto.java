package com.exposition.dto;

import javax.validation.constraints.NotEmpty;

import com.exposition.entity.FreeBoard;

import lombok.Data;

@Data
public class IdeaDto {

	private Long id;
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;
	private String content;

}
