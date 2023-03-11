package com.exposition.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.modelmapper.ModelMapper;

import com.exposition.entity.TourBoard;

import lombok.Data;

@Data
public class TourBoardDto {

	private Long id;
	@NotBlank(message = "제목은 필수 입력 값입니다.")
	private String title;
	private String content;
	
	private List<FileDto> fileDtoList = new ArrayList<>();
	
	private List<Long> fileIds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public TourBoard createItem() {
		return modelMapper.map(this, TourBoard.class);
	}

}
