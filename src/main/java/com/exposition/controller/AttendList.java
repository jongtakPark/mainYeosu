package com.exposition.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exposition.dto.ReservationDto;
import com.exposition.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/attend")
@RequiredArgsConstructor
public class AttendList {

	private final ReservationService reservationService;
	
	//참가업체목록 페이지로 이동
	@GetMapping(value="/view")
	public String attendView(ReservationDto reservationDto) {
//		List<ReservationDto> listDto = reservationService.getAttendCom(reservationDto);
//		System.out.println(listDto);
//		List<ReservationDto> comReservationDto = reservationService.getReservationList(listDto);
//		System.out.println(comReservationDto);
		return "list/attendList";
	}
	
	
	
}
