package com.exposition.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;

import com.exposition.entity.Announcement;
import com.exposition.entity.Idea;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
import com.exposition.entity.Survey;
import com.exposition.entity.Volunteer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

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
	
//	private Member member;
	
	private List<FileDto> fileDtoList = new ArrayList<>();

	private static ModelMapper modelMapper = new ModelMapper();
	
	public static FreeBoardDto of(Survey survey) {
		return modelMapper.map(survey, FreeBoardDto.class);
	}
	
	public static FreeBoardDto of(Announcement announcement) {
		return modelMapper.map(announcement, FreeBoardDto.class);
	}
	
	public static FreeBoardDto of(Review review) {
		return modelMapper.map(review, FreeBoardDto.class);
	}
	
	public static FreeBoardDto of(Idea idea) {
		return modelMapper.map(idea, FreeBoardDto.class);
	}
	
	public static FreeBoardDto of(Volunteer volunteer) {
		return modelMapper.map(volunteer, FreeBoardDto.class);
	}
}
