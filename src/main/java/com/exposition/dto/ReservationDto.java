package com.exposition.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.modelmapper.ModelMapper;

import com.exposition.entity.Company;
import com.exposition.entity.Reservation;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDto {

	private Long id;
	@Enumerated(EnumType.STRING)
	private String location;
	private String content;
	private String startDay;
	private String endDay;
	private Company company;
	private String approval;
	
	private List<FileDto> fileDtoList = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Reservation createTourBoard() {
		return modelMapper.map(this, Reservation.class);
	}
	
	@QueryProjection
	public ReservationDto(String startDay, String endDay, String approval) {
		this.startDay = startDay;
		this.endDay = endDay;
		this.approval = approval;
	}
	
}
