package com.exposition.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.exposition.constant.Role;
import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.CompanyModifyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.MemberModifyFormDto;
import com.exposition.entity.Company;
import com.exposition.entity.Member;
import com.exposition.repository.CompanyRepository;
import com.exposition.service.CompanyService;
import com.exposition.service.MailService;
import com.exposition.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/signup")
@RequiredArgsConstructor
public class MemberController{
	
	private final CompanyService companyService;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;
	
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

		//일반회원
		for(int i=2; i<11; i++) {
		check = memberService.checkMidDuplicate(String.valueOf(i));
		if (check)
			return;
		memberFormDto.setMid("user" + String.valueOf(i));
		memberFormDto.setPassword("user");
		memberFormDto.setName("사용자");
		memberFormDto.setEmail("User"+String.valueOf(i)+"@userEmail.com");
		member = Member.createMember(memberFormDto, passwordEncoder);
		String password1 = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password1);
		member.setRole(Role.USER);
		memberService.saveMember(member);
		}
		//기업회원
		for(int i=11; i<16; i++) {
		check = companyService.checkComDuplicate(String.valueOf(i));
		if (check)
			return;
		CompanyFormDto companyFormDto = new CompanyFormDto();
		companyFormDto.setCom("com" + String.valueOf(i));
		companyFormDto.setPassword("com");
		companyFormDto.setName("기업");
		companyFormDto.setEmail("com"+String.valueOf(i)+"@userEmail.com");
		Company company = Company.createCompany(companyFormDto, passwordEncoder);
		String password2 = passwordEncoder.encode(companyFormDto.getPassword());
		company.setPassword(password2);
		company.setRole(Role.COMPANY);
		companyService.saveCompany(company);
		
		}
	}
		
	
	
	
	//로그인창으로 이동
	@RequestMapping(value="/login", method= {RequestMethod.POST, RequestMethod.GET})
	public String login(Model model, HttpServletRequest request) {
		String uri = request.getHeader("Referer");
		if(uri != null && !uri.contains("/signup/login")) {
			request.getSession().setAttribute("prevPage", uri);
		}
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
	
	//일반회원 아이디 찾기
	@PostMapping(value="/findid")
	@ResponseBody
	public HashMap<String, Object> findId(@RequestParam("name") String name, @RequestParam("email") String email) throws MessagingException {
		Member member = memberService.findByNameAndEmail(name, email);
		HashMap<String, Object> map = new HashMap<>();
		map.put("result", mailService.sendFindIdMail(email, member));
		return map;
	}
	
	//일반 회원 비밀번호 찾기 CompletableFuture으로 return 된 값은 .get()으로 가져온다.
	@PostMapping(value="/findpw")
	@ResponseBody
	public String findPw(String mid, String email) throws MessagingException, InterruptedException, ExecutionException {
		Member member = memberService.findByMidAndEmail(mid, email);
		String password =mailService.sendFindPwMail(email, member).get();
		String pw= passwordEncoder.encode(password);
		member.setPassword(pw);
		memberService.updateMember(member);
		return "success";
	}
	
	//기업 회원 비밀번호 찾기
	@PostMapping(value="/findcompw")
	@ResponseBody
	public String findComPw(String com, String email) throws MessagingException, InterruptedException, ExecutionException {
		Company company = companyService.findByComAndEmail(com, email);
		String password =mailService.sendFindPwMail(email, company).get();
		String pw= passwordEncoder.encode(password);
		company.setPassword(pw);
		companyService.updateCompany(company);
		return "success";
	}
	
	//마이페이지로 이동
	@GetMapping(value="/mypage")
	public String mypage(Model model, Principal principal) {
		Member member = memberService.findByMid(principal.getName());
		MemberModifyFormDto memberModifyFormDto = MemberModifyFormDto.of(member);
	    model.addAttribute("memberModifyFormDto", memberModifyFormDto);
	    return "member/memberModify";
	}
	
	//마이페이지 비밀번호 변경          
	@PutMapping(value="/mypageupdate/{mid}")
	@Validated
	public String modifyMember(@Valid MemberModifyFormDto memberModifyFormDto, BindingResult bindingResult, @PathVariable String mid, Model model, MemberFormDto memberFormDto) {   
		if(bindingResult.hasErrors()) {
			return "member/memberModify";
	    } 
	    try {
	        Member member = memberService.findByMid(mid);
	        String password= passwordEncoder.encode(memberFormDto.getPassword());
	        member.setPassword(password);
	        memberService.updateMember(member);
	        model.addAttribute("succMessage", "회원 정보가 수정 되었습니다.");
	    }catch(IllegalStateException e) {
	        model.addAttribute("errorMessage", "회원 수정 중 오류가 발생 했습니다.");
	        return "member/memberModify";
	    }
	                     
	    return "redirect:/";
	}
	
	//마이페이지 회원탈퇴
	@DeleteMapping(value="/memDelete/{mid}")
	public String memdelete(@PathVariable String mid, HttpSession session) {
	   Member member = memberService.findByMid(mid);
	   memberService.deleteMem(member);
	   session.invalidate();
	   return "redirect:/";
	}
	
	//기업 마이페이지로 이동
	@GetMapping(value="/comMypage")
	public String commypage(Model model, Principal principal) {
		CompanyModifyFormDto companyModifyFormDto = companyService.findReservationByCom(principal.getName());
	    model.addAttribute("companyModifyFormDto", companyModifyFormDto);
	   return "member/companyModify";
	}
	   
	//기업 마이페이지 비밀번호 변경
	@PutMapping(value="/comMypageUpdate/{com}")
	@Validated
	public String modifyCompany(@Valid CompanyModifyFormDto companyModifyFormDto, BindingResult bindingResult, @PathVariable String com, Model model, CompanyFormDto companyFormDto) {
	   if(bindingResult.hasErrors()) {
		  model.addAttribute("errorMessage", "비밀번호는 8~16자 영문 대 소문자, 숫자를 사용하세요.");
	      return "member/companyModify";
	   }
	   try {
	      Company company = companyService.findByCom(com);
	      String password = passwordEncoder.encode(companyFormDto.getPassword());
	      company.setPassword(password);
	      companyService.updateCompany(company);
	   }catch(IllegalStateException e) {
	      model.addAttribute("errorMessage", e.getMessage());
	      return "member/companyModify";
	   }
	            
	   return "redirect:/";
	}
	
	//기업 예약 신청 취소
	@PutMapping(value="/cancle/{id}")
	public String cancle(@PathVariable Long id, Model model, Principal principal) {
		try {
			Company company = companyService.findByCom(principal.getName());
			company.setApproval("예약없음");
			companyService.reservationCancle(id);
		} catch(Exception e) {
			model.addAttribute("errorMessage", "예약삭제중 에러가 발생했습니다");
			return "redirect:/";
		}
		return "redirect:/";
	}
	
	//기업 회원탈퇴
    @DeleteMapping(value="/comDelete/{com}")
    public String comdelete(@PathVariable String com, HttpSession session) {
       Company company = companyService.findByCom(com); 
       companyService.deleteCom(company);
       session.invalidate();
       return "redirect:/";
    }
}
