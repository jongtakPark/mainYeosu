package com.exposition.controller;

import java.util.HashMap;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exposition.constant.Role;
import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.entity.Company;
import com.exposition.entity.Member;
import com.exposition.service.CompanyService;
import com.exposition.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/signup")
@RequiredArgsConstructor
public class MemberController{
	
	private final CompanyService companyService;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	@PostConstruct
	//계정 생성
	private void createAdmin() {
		//관리자
		boolean check = memberService.checkMidDuplicate("admin");
		if (check)
			return;
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setMid("admin");
		memberFormDto.setPassword("admin123");
		memberFormDto.setName("관리자");
		memberFormDto.setEmail("admin@adminEmail.com");
		Member member = Member.createMember(memberFormDto , passwordEncoder);
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		member.setRole(Role.ADMIN);
		memberService.saveMember(member);
		
		for(int i = 2; i < 4 ; i++) {
			check = memberService.checkMidDuplicate(String.valueOf(i));
			if (check)
				return;
			memberFormDto.setMid("user" + String.valueOf(i));
			memberFormDto.setPassword(String.valueOf(i));
			memberFormDto.setName("사용자"+i);
			memberFormDto.setEmail("User"+i+"@userEmail.com");
			member = Member.createMember(memberFormDto, passwordEncoder);
			String password1 = passwordEncoder.encode(memberFormDto.getPassword());
			member.setPassword(password1);
			member.setRole(Role.USER);
			memberService.saveMember(member);
		}
		
	}
	
	
	
	//로그인창으로 이동
	@RequestMapping(value="/login", method= {RequestMethod.POST, RequestMethod.GET})
	public String login(Model model) {
		return "member/loginForm";
	}
	//로그인 오류시
	@GetMapping(value="/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
		return "member/loginForm";
	}
	//이용약관 동의창으로 이동
	@GetMapping(value="/agreement")
	public String agreement() {
		return "member/agreement";
	}
	//기업 회원가입창으로 이동
	@GetMapping(value="/company")
	public String companySignUp(Model model) {
		model.addAttribute("companyFormDto", new CompanyFormDto());
		return "member/companySignUp";
	}
	//일반 회원가입창으로 이동
	@GetMapping(value="/personal")
	public String personalSignUp(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/personalSignUp";
	}
	//일반회원가입
	@PostMapping(value="/new")
	@Validated
	public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "member/personalSignUp";
		}
		
		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
		} catch(IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/personalSignUp";
		}
		
		return "redirect:/";
	}
	//기업회원가입
	@PostMapping(value="/comnew")
	@Validated
	public String newCompany(@Valid CompanyFormDto companyFormDto, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "member/companySignUp";
		}
		
		try {
			Company company = Company.createCompany(companyFormDto, passwordEncoder);
			companyService.saveCompany(company);
		} catch(IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/companySignUp";
		}
		
		return "redirect:/";
	}
	//ajax를 이용한 아이디 중복검사
	@GetMapping(value="/exists")
	@ResponseBody
	public HashMap<String, Object> checkMidDuplicate(String mid){
		HashMap<String, Object> map = new HashMap<>();
		map.put("result", memberService.checkMidDuplicate(mid));
		return map;
	}
	
	//ajax를 이용한 사업자번호 중복검사
	@GetMapping(value="/existscom")
	@ResponseBody
	public HashMap<String, Object> checkComDuplicate(String com){
		HashMap<String, Object> map = new HashMap<>();
		map.put("result", companyService.checkComDuplicate(com));
		return map;
	}

	// 아이디/비밀번호 찾기창으로 이동
	@GetMapping(value="/findidpw")
	public String findIdPw() {
		return "member/findIdPw";
	}
	//문제 없이 잘 작동하지만 member테이블의 id를 가져오려면 id가 바인딩 되어 있어야 함.
	//설문게시판 페이지 넘어갈 때 session에 member id를 바인딩 해야함.
	//권환 변경 USER -> VOLUNTEER
	@GetMapping(value="/change/{id}")
	public String changeRole(@PathVariable("id") Long id, Model model, Optional<Member> member) {
		member = memberService.findById(id);
		member.get().setRole(Role.VOLUNTEER);
		memberService.updateMember(member.get());
		return "redirect:/";
	}
	
	@ResponseBody
	@PostMapping(value = "/findEmail")
	public HashMap<String, String> findId(@RequestParam("email") String email) {
		MemberFormDto memDto = memberService.findByEmail(email);
		HashMap<String, String> map = new HashMap<String, String>();

		if (memDto.getMid() == null) {
			map.put("answer", "Fail");
		} else {
			map.put("answer", "Success");
		}
		return map;
	}
	

}
