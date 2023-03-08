package com.exposition.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TourBoardDto {

	private Long id;
	@NotBlank(message = "제목은 필수 입력 값입니다.")
	private String title;
	private String content;
	
	private List<FileDto> fileDtoList = new ArrayList<>();
}
