package com.exposition.repository;

import java.util.List;

import com.exposition.dto.ReservationDto;

public interface ReservationRepositoryCustom {

	List<ReservationDto> getSameLocationReservation(ReservationDto reservationDto);
	
	List<ReservationDto> getAttendCom(ReservationDto reservationDto);
}
