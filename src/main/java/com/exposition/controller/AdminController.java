package com.exposition.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exposition.constant.Role;
import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.entity.Member;
import com.exposition.service.CompanyService;
import com.exposition.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

	private final CompanyService companyService;
	private final MemberService memberService;
	
	//기업 승인게시판 페이지 이동
	@RequestMapping(value="/comConsent", method= {RequestMethod.PUT, RequestMethod.GET})
	public String comConsent(CompanyFormDto companyFormDto, MemberFormDto memberFormDto, Model model, Optional<Integer> page, HttpServletRequest request) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 10);
		Page<CompanyFormDto> appComList = companyService.findApprovalCom(companyFormDto, pageable);
		HttpSession session = request.getSession();
		int nowPage = appComList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, appComList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		model.addAttribute("appCom", appComList);
		session.setAttribute("appCom", appComList);
		return "admin/comConsent";
	}
	
	//자원봉사 승인페이지 이동
	@RequestMapping(value="/memConsent", method= {RequestMethod.PUT, RequestMethod.GET})
	public String comConsent(MemberFormDto memberFormDto, Model model, Optional<Integer> page, HttpServletRequest request) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 10);
		Page<MemberFormDto> appMemList = memberService.findByAppVolunteer(memberFormDto, pageable);
		HttpSession session = request.getSession();
	    int nowPage = appMemList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, appMemList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		model.addAttribute("appMem", appMemList);
		session.setAttribute("appMem", appMemList);
		return "admin/memConsent";
	}
	
	//일반회원 모두를 자원봉사회원으로 승인
	@PutMapping(value="/conMemAll")
	public String conMemAll(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Page<MemberFormDto> appMemList = (Page<MemberFormDto>) session.getAttribute("appMem");
		for(int i=0; i<appMemList.getSize(); i++) {
			memberService.updateMemToVol(appMemList.getContent().get(i));
		}
		return "redirect:/admin/memConsent";
	}
	
	//일반회원을 자원봉사회원으로 승인
	@PutMapping(value="/conMem/{mid}")
	public String conMem(@PathVariable("mid") String mid) {
		Member member = memberService.findByMid(mid);
		MemberFormDto memberFormDto = MemberFormDto.createMemberDto(member);
		memberService.updateMemToVol(memberFormDto);
		return "redirect:/admin/memConsent";
	}
	
	//회원관리 페이지 이동
	@GetMapping(value="/management")
	public String adminMenu2() {
	    return "admin/management";
	}
	
	
	
}
