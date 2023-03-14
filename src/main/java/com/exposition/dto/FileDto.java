package com.exposition.dto;

import org.modelmapper.ModelMapper;

import com.exposition.entity.Files;

import lombok.Data;

@Data
public class FileDto {

	private Long id;
	private String img;
	private String oriImg;
	private String thumbnail;
	private String savePath;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static FileDto of(Files file) {
		return modelMapper.map(file, FileDto.class);
	}

}
