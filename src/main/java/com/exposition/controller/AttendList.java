package com.exposition.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exposition.dto.ReservationDto;
import com.exposition.service.CompanyService;
import com.exposition.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/attend")
@RequiredArgsConstructor
public class AttendList {

	private final ReservationService reservationService;
	private final CompanyService companyService;
	
	//참가업체목록 페이지로 이동
	@GetMapping(value="/view")
	public String attendView(Model model,ReservationDto reservationDto, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 5);
		Page<ReservationDto> listDto = reservationService.getAttendCom(reservationDto, pageable);
		List<ReservationDto> comReservationDto = reservationService.getReservationList(listDto.getContent());
		Long count = companyService.findSucReservationCom();
		int endPage = (int) (count/5);
		int nowPage = listDto.getPageable().getPageNumber() + 1;	        
        int startPage =  Math.max(nowPage - 4, 1);
        endPage = Math.min(nowPage+9, endPage);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		model.addAttribute("reservationDto", comReservationDto);
		System.out.println(comReservationDto);
		return "attend/attendList";
	}
	
	
	
}
