package com.exposition.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

@Controller
@RequestMapping(value="/lease")
public class LeaseController {

	@GetMapping(value="/")
	public String Lease() {
		return "lease/lease";
	}
	
	@GetMapping(value="/calendar")
	public String caldendar(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String endDay = (String) session.getAttribute("endDay");
		String startDay =(String) session.getAttribute("startDay");
		model.addAttribute("startDay", startDay);
		model.addAttribute("endDay", endDay);
		return "lease/calendar";
	}
	@ResponseBody
	@PostMapping(value="/reservation")
	public String reservation(@RequestBody Map<String, Object> reserve_date, HttpServletRequest request) throws Exception  {
		System.out.println(reserve_date);
		String startDay = (String) reserve_date.get("startStr");
		String endDay = (String) reserve_date.get("end");
		System.out.println(endDay);
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = dtFormat.parse(endDay);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DATE,+1);

		endDay = dtFormat.format(cal.getTime());
		System.out.println(endDay);
		HttpSession session = request.getSession();
		session.setAttribute("endDay", endDay);
		session.setAttribute("startDay", startDay);
		return "success";
	}
	//예약 신청
	@PostMapping(value="/new")
	public String newReservation(@RequestParam("content") String content, HttpServletRequest request, @RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, ReservationDto reservationDto) {
//		if(files.get(0).isEmpty()) {
//			model.addAttribute("errorMessage", "이미지는 필수 입니다.");
//			return "lease/calendar";
//		}
		HttpSession session = request.getSession();
		String endDay = (String) session.getAttribute("endDay");
		String startDay =(String) session.getAttribute("startDay");
		reservationDto.setCom(content);
		reservationDto.setStartDay(startDay);
		reservationDto.setEndDay(endDay);
		System.out.println(endDay);
		System.out.println(startDay);
		return "redirect:/";
	}
}
