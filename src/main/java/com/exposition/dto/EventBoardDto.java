package com.exposition.dto;

import org.modelmapper.ModelMapper;

import com.exposition.entity.EventBoard;

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
	
	
}
