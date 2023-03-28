package com.exposition.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class MainController {
	
	//메인화면
	@RequestMapping(value="/")
	public String main() {
		return "main";
	}
	//권한이 없는 사람이 접근했을경우
	@RequestMapping(value="/error_user")
    public String error() {
       return "redirect:/";
    }
}
