package com.exposition.dto;

import org.modelmapper.ModelMapper;

import com.exposition.entity.EventBoard;
import com.exposition.entity.Survey;

import lombok.Data;

@Data
public class EventBoardDto {

	private Long id;
	private String title;
	private String content;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public EventBoard createEventBoard() {
		return modelMapper.map(this, EventBoard.class);
	}
	
	public static EventBoardDto of(EventBoard eventBoard) {
		return modelMapper.map(eventBoard, EventBoardDto.class);
	}
}
