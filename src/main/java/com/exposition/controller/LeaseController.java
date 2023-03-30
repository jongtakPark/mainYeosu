package com.exposition.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.ReservationDto;
import com.exposition.entity.Company;
import com.exposition.service.CompanyService;
import com.exposition.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/lease")
@RequiredArgsConstructor
public class LeaseController {

	private final CompanyService companyService;
	private final ReservationService reservationService;
	//업체등록 장소 선택 페이지로 이동
	@GetMapping(value="/")
	public String Lease(Model model) {
		model.addAttribute("reservationDto", new ReservationDto());
		return "lease/lease";
	}
	
	//DB에서 endDay 가져올때 +1 해줘야함
	//장소 선택후 기간 선택 페이지로 이동
	@GetMapping(value="/calendar")
	public String caldendar(Model model, HttpServletRequest request, ReservationDto reservationDto, Principal principal) throws Exception {
		Company company = companyService.findByCom(principal.getName());
		if(company!=null) {
			try {
					if(company.getApproval().equals("예약신청중")) {
						model.addAttribute("errorMessage", "예약 신청중입니다. 등록 수정을 원하시면 마이페이지에서 취소하시고 다시 신청해주세요");
						return "lease/lease";
					}
					if(company.getApproval().equals("예약완료")) {
						model.addAttribute("errorMessage", "예약이 완료 되었습니다. 등록 수정을 원하시면 마이페이지에서 취소하시고 다시 신청해주세요");
						return "lease/lease";
					}
					SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
					HttpSession session = request.getSession();
					session.setAttribute("location", reservationDto);
					List<ReservationDto> list = reservationService.getSameLocationReservation(reservationDto);
					for(int i=0; i<list.size(); i++) {
						Date dt = dtFormat.parse(list.get(i).getEndDay());
						Calendar cal = Calendar.getInstance();
						cal.setTime(dt);
						cal.add(Calendar.DATE, +1);
						String endDay = dtFormat.format(cal.getTime());
						list.get(i).setEndDay(endDay);
					}
					model.addAttribute("list", list);
			} catch(Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "장소 선택 중 에러가 발생했습니다.");
				return "lease/lease";
			}
		}
		return "lease/calendar";
	}
	
	//예약 기간 받아오는 컨트롤러
	@ResponseBody
	@PostMapping(value="/reservation")
	public String reservation(@RequestBody Map<String, Object> reserve_date, HttpServletRequest request, Model model) throws Exception  {
		String startDay = (String) reserve_date.get("startStr");
		String endDay = (String) reserve_date.get("end");
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = dtFormat.parse(endDay);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		endDay = dtFormat.format(cal.getTime());
		HttpSession session = request.getSession();
		if(endDay==null && startDay==null) {
			model.addAttribute("errorMessage", "등록 기간을 드래그로 선택해주세요.");
			return "lease/calendar";
		} else {
			session.setAttribute("endDay", endDay);
			session.setAttribute("startDay", startDay);
		}
		
		return "success";
	}
	
	//예약 저장
	@PostMapping(value="/new")
	public String newReservation(@RequestParam("content") String content, HttpServletRequest request, @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, ReservationDto reservationDto, Principal principal) throws Exception {
		HttpSession session = request.getSession();
		reservationDto = (ReservationDto) session.getAttribute("location");
		if(files.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "이미지 첨부는 필수 입니다.");
			model.addAttribute("reservationDto", reservationDto);
			return "lease/calendar";
		}
		try {
			Company company = companyService.findByCom(principal.getName());
			company.setApproval("예약신청중");
			companyService.updateCompany(company);
			String endDay = (String) session.getAttribute("endDay");
			String startDay =(String) session.getAttribute("startDay");
			reservationDto.setContent(content);
			reservationDto.setStartDay(startDay);
			reservationDto.setEndDay(endDay);
			reservationDto.setCompany(company);
			reservationService.saveReservation(files, reservationDto);
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "예약 신청 중 에러가 발생했습니다.");
			return "lease/calendar";
		}
		return "lease/lease";
	}
}
