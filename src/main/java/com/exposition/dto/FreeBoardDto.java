package com.exposition.dto;

import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;

import com.exposition.entity.Announcement;
import com.exposition.entity.Survey;

import lombok.Data;

@Data
public class FreeBoardDto {

	private Long id;
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;
	@NotEmpty(message = "내용을 적어주세요.")
	private String content;
	
	private String createdBy;
	
	private String modifiedBy;

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static FreeBoardDto of(Survey survey) {
		return modelMapper.map(survey, FreeBoardDto.class);
	}
	
	public static FreeBoardDto of(Announcement announcement) {
		return modelMapper.map(announcement, FreeBoardDto.class);
	}
}
