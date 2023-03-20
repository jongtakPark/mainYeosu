package com.exposition.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.MemberModifyFormDto;
import com.exposition.entity.Company;
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
	public String comConsent(CompanyFormDto companyFormDto, Model model, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 10);
		Page<CompanyFormDto> appComList = companyService.findApprovalCom(companyFormDto, pageable);
		int nowPage = appComList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, appComList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		model.addAttribute("appCom", appComList);
		return "admin/comConsent";
	}
	
	//업체등록 신청한 기업 승인
	@PutMapping(value="/conCom/{com}")
	public String updateComConsent(@PathVariable String com) {
		companyService.updateApp(com);
		return "redirect:/admin/comConsent";
	}
	
	//자원봉사 승인페이지 이동
	@RequestMapping(value="/memConsent", method= {RequestMethod.PUT, RequestMethod.GET})
	public String comConsent(MemberFormDto memberFormDto, Model model, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 10);
		Page<MemberFormDto> appMemList = memberService.findByAppVolunteer(memberFormDto, pageable);
	    int nowPage = appMemList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, appMemList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		model.addAttribute("appMem", appMemList);
		return "admin/memConsent";
	}
	
	//일반회원 모두를 자원봉사회원으로 승인
	@PutMapping(value="/conMemAll")
	public String conMemAll() {
		memberService.updateAllMemToAll();
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
	@GetMapping(value="/memManagement")
	public String memManagement(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
	    Page<Member> memList =  memberService.findAllMember(pageable);
	    model.addAttribute("memManage", memList);
	    int nowPage = memList.getPageable().getPageNumber() + 1;	        
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, memList.getTotalPages());

        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		return "admin/memManagement";
	}
	
	//기업관리 페이지 이동
	@GetMapping(value="/comManagement")
	public String comManagement(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
		Page<Company> comList = companyService.findAllComapny(pageable);
		model.addAttribute("comManage", comList);
		int nowPage = comList.getPageable().getPageNumber() + 1;	        
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, comList.getTotalPages());

        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
		return "admin/comManagement";
	}
	
	//일반 회원 수정 페이지 이동
	@GetMapping(value="modifyMem/{mid}")
	public String modifyMem(Model model, @PathVariable String mid) {
		Member member = memberService.findByMid(mid);
		MemberModifyFormDto memberModifyFormDto = MemberModifyFormDto.of(member);
		model.addAttribute("memberModifyFormDto", memberModifyFormDto);
		return "admin/adminMemberModify";
	}
	
}
