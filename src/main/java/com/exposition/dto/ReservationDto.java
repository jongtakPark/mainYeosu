package com.exposition.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ReservationDto {

	private Long id;
	private String com;
	private String content;
	private String savePath;
	private String startDay;
	private String endDay;
	
	private List<FileDto> fileDtoList = new ArrayList<>();
	
}
