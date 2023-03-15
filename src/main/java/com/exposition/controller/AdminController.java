package com.exposition.controller;

import java.util.List;

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
public class AdminController {

	private final CompanyService companyService;
	
	@GetMapping(value="/menu")
	public String adminMenu(Company company, Model model) {
		List<Company> appCom = companyService.findApprovalCom(company);
		model.addAttribute("appCom", appCom);
		return "admin/adminmenu";
	}
	
	
}
