package com.exposition.controller;

import java.util.Optional;

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
	
	//승인게시판 페이지(업체등록, 자원봉사) 이동
	@RequestMapping(value="/consent", method= {RequestMethod.PUT, RequestMethod.GET})
	public String comConsent(CompanyFormDto companyFormDto, MemberFormDto memberFormDto, Model model, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 10);
		Page<CompanyFormDto> appComList = companyService.findApprovalCom(companyFormDto, pageable);
		Page<MemberFormDto> appMemList = memberService.findByAppVolunteer(memberFormDto, pageable);
		int nowPage = appComList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, appComList.getTotalPages());
	    int nowPage1 = appMemList.getPageable().getPageNumber() + 1 ;
	    int startPage1 =  Math.max(nowPage - 4, 1);
	    int endPage1 = Math.min(nowPage+9, appMemList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    model.addAttribute("nowPage1",nowPage1);
	    model.addAttribute("startPage1", startPage1);
	    model.addAttribute("endPage1", endPage1);
		model.addAttribute("appMem", appMemList);
		model.addAttribute("appCom", appComList);
		return "admin/consent";
	}
	
	//일반회원을 자원봉사회원으로 승인
	@PutMapping(value="/conMem/{mid}")
	public String conMem(@PathVariable("mid") String mid) {
		Member member = memberService.findByMid(mid);
		member.setRole(Role.VOLUNTEER);
		member.setApproval("O");
		System.out.println("확인");
		System.out.println(member);
		memberService.updateMember(member);
		return "redirect:/admin/consent";
	}
	
	//회원관리 페이지 이동
	@GetMapping(value="/management")
	   public String adminMenu2() {
	      return "admin/management";
	   }
	
}
