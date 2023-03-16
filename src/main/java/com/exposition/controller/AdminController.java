package com.exposition.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exposition.entity.Company;
import com.exposition.service.CompanyService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

	private final CompanyService companyService;
	
	//승인게시판 페이지 이동
	@GetMapping(value="/consent")
	public String adminMenu(Company company, Model model) {
		List<Company> appCom = companyService.findApprovalCom(company);
		model.addAttribute("appCom", appCom);
		return "admin/consent";
	}
	
	//회원관리 페이지 이동
	@GetMapping(value="/management")
	   public String adminMenu2() {
	      return "admin/management";
	   }
	
	
}
