package com.exposition.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.ReservationDto;

public interface ReservationRepositoryCustom {

	List<ReservationDto> getSameLocationReservation(ReservationDto reservationDto);
	
	Page<ReservationDto> getAttendCom(ReservationDto reservationDto, Pageable pageable);
}
