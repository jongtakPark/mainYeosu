package com.exposition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/admin")
@RequiredArgsConstructor
public class AdminController {

	@GetMapping(value="/menu")
	public String adminMenu() {
		return "admin/adminMenu";
	}
}
