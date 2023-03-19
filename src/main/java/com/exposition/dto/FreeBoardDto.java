package com.exposition.dto;

import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;

import com.exposition.entity.Survey;

import lombok.Data;

@Data
public class FreeBoardDto {

	private Long id;
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;
	private String content;

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static FreeBoardDto of(Survey survey) {
		return modelMapper.map(survey, FreeBoardDto.class);
	}
}
