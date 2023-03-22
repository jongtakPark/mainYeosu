package com.exposition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/attend")
public class AttendList {

	@GetMapping(value="/view")
	public String attendView() {
		return "list/attendList";
	}
	
}
