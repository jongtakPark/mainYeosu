package com.exposition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.exposition.dto.FreeBoardDto;

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
       return "member/error";
    }
	
	//eng 메인 페이지
    @GetMapping(value="/engMain")
    public String engMain() {
       return "lan/engMain";
    }
    
    //jap 메인 페이지
    @GetMapping(value="/japMain")
    public String japMain() {
       return "lan/japMain";
    }
    
    //chi 메인 페이지
    @GetMapping(value="/chiMain")
    public String chiMain() {
       return "lan/chiMain";
    }
}
