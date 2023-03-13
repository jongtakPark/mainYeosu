package com.exposition.dto;

import org.modelmapper.ModelMapper;

import com.exposition.entity.File;

import lombok.Data;

@Data
public class FileDto {

	private Long id;
	private String img;
	private String oriImg;
	private String thumbnail;
	private String savePath;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static FileDto of(File file) {
		return modelMapper.map(file, FileDto.class);
	}

}
