package com.exposition.dto;

import javax.validation.constraints.NotEmpty;

import org.modelmapper.ModelMapper;

import com.exposition.entity.EventBoard;
import com.exposition.entity.Survey;

import lombok.Data;

@Data
public class EventBoardDto {

	private Long id;
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;
	private String content;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public EventBoard createEventBoard() {
		return modelMapper.map(this, EventBoard.class);
	}
	
	public static EventBoardDto of(EventBoard eventBoard) {
		return modelMapper.map(eventBoard, EventBoardDto.class);
	}
}
