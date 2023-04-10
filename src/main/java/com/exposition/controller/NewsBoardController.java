package com.exposition.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.EventBoardDto;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.FreeBoardDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.Announcement;
import com.exposition.entity.EventBoard;
import com.exposition.entity.Files;
import com.exposition.entity.Member;
import com.exposition.entity.Survey;
import com.exposition.service.AnnouncementService;
import com.exposition.service.EventBoardService;
import com.exposition.service.FileService;
import com.exposition.service.MailService;
import com.exposition.service.MemberService;
import com.exposition.service.SurveyService;
import com.exposition.service.TourBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsBoardController {

	private final FileService fileService;
	private final TourBoardService tourBoardService;
	private final EventBoardService eventBoardService;
	private final MemberService memberService;
	private final MailService mailService;
	private final AnnouncementService announcementService;
	private final SurveyService surveyService;
	
	//주변관광지 페이지 이동
	@RequestMapping(value="/tour", method= {RequestMethod.GET, RequestMethod.POST})
	public String tourPage(Model model, TourBoardDto tourBoardDto, Optional<Integer> page) {
	
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 6);
		Page<BoardMainDto> tourBoardList = tourBoardService.getBoardMainPage(tourBoardDto, pageable);
		model.addAttribute("tourboards", tourBoardList);
		int nowPage = tourBoardList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, tourBoardList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    
		return "news/tourboard";
	}
		
	//주변관광지 글 작성 페이지 이동
	@GetMapping(value="/tourwrite")
	public String tourWrite(Model model) {
		model.addAttribute("tourBoardDto", new TourBoardDto());
		return "news/tourboardwrite";
	}
	
	//주변관광지 글 등록
	@PostMapping(value="/toursave")
	public String tourSave(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid TourBoardDto tourBoardDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목을 입력해주세요");
			model.addAttribute("keywordWrite", tourBoardDto);
			return "news/tourboardwrite";
		}
		if(files.get(0).isEmpty() && tourBoardDto.getId() == null) {
			model.addAttribute("errorMessage", "이미지는 필수 입니다.");
			model.addAttribute("keywordWrite", tourBoardDto);
			return "news/tourboardwrite";
		}
		try {
			tourBoardService.saveTour(files, tourBoardDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "글 작성 중 에러가 발생했습니다.");
			model.addAttribute("keywordWrite", tourBoardDto);
			return "news/tourboardwrite";
		}
		return "redirect:/news/tour";
	}
	
	//주변 관광지 상세 페이지 이동
	@GetMapping(value="view/{tourBoardId}")
	public String tourBoardDetail(@PathVariable("tourBoardId") Long tourBoardId, Model model) {
		try {
			TourBoardDto tourBoardDto = tourBoardService.getTourBoardDetail(tourBoardId);
			model.addAttribute("tourBoardDto",tourBoardDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMessage","존재하지 않는 글 입니다");
			model.addAttribute("tourBoardDto", new TourBoardDto());
			return "news/tourboardwrite";
		}
		return "news/tourboardview";
	}
	
	//주변 관광지 수정 페이지로 이동
	@GetMapping(value="/modify/{id}")
	public String modifyBoard(@PathVariable("id") Long tourBoardId, Model model) {
		try {
			TourBoardDto tourBoardDto = tourBoardService.getTourBoardDetail(tourBoardId);
			model.addAttribute("tourBoardDto",tourBoardDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMessage","존재하지 않는 글 입니다");
			model.addAttribute("tourBoardDto", new TourBoardDto());
			return "news/tourboardwrite";
		}
		return "news/updatewrite";
	}
	
	//주변 관광지 글 수정 등록
	@PutMapping(value="/update/{id}")
	public String updatesucc(@Valid TourBoardDto tourBoardDto, BindingResult bindingResult, Model model, @RequestParam("files") List<MultipartFile> fileList) throws Exception {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목을 입력해주세요.");
			model.addAttribute("tourBoardDto", tourBoardDto);
			return "news/updatewrite";
		}
		try {
			if(fileList.get(0).isEmpty()) {
				tourBoardService.updateOnlyTourBoard(tourBoardDto, fileList);
				return "redirect:/news/tour";
			} else {
				tourBoardService.updateTourBoard(tourBoardDto, fileList);
			}
		} catch (Exception e) {
			model.addAttribute("errorMessage", "글 수정 중 에러가 발생하였습니다.");
			return "news/updatewrite";
		}
		
		return "redirect:/news/tour";
	}
	
	//주변 관광지 글 삭제하기(첨부파일이 있을 경우 첨부파일을 먼저 지우고 게시글을 지워야 한다)
	@DeleteMapping(value="delete/{id}")
	public String deleteBoard(@PathVariable("id") Long id) throws Exception{
		tourBoardService.deleteBoard(id);
		return "redirect:/news/tour";
	}
	
	//이벤트 게시판 이동
	@GetMapping(value="/event")
	public String eventBoard(Model model) {
		model.addAttribute("eventBoardList", eventBoardService.findAll());
		return "news/eventboard";
	}
	//이벤트 게시판 글쓰기 창으로 이동
	@GetMapping(value="/eventboardwrite")
	public String eventBoardWrite(Model model) {
		model.addAttribute("eventBoardDto", new EventBoardDto());
		return "news/eventboardwrite";
	}
	//이벤트 게시판 글 등록과 동시에 이벤트 당첨자 회원을 3명 뽑음
	@PostMapping(value="/new")
	public String eventBoardNew(@Valid EventBoardDto eventBoardDto, BindingResult bindingResult, Model model) throws Exception {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목을 입력해주세요.");
			model.addAttribute("eventBoardDto", eventBoardDto);
			return "news/eventboardwrite";
		}
		Random rnd = new Random();
		EventBoard eventBoard = eventBoardDto.createEventBoard();
		for(int i=0; i<3; i++) {
			List<EventMemberDto> mem = eventBoardService.saveBoardAndSelectMember(eventBoard);
			if(mem.size()<1) {
				model.addAttribute("errorMessage", "이벤트 당첨 조건에 맞는 회원이 없습니다.");
				return "news/eventboardwrite";
			} else {
				int j = rnd.nextInt(mem.size());
				Member member = Member.EventMember(mem.get(j));
				Member m = memberService.findById(member.getId()).get();
				mailService.eventSendhMail(m.getEmail(), m.getName());
				m.setEventCount("Y");
				m.setEventBoardId(eventBoard.getId());
				memberService.updateMember(m);
			}
		}
		return "redirect:/news/event";
	}

	//이벤트 게시판 상세창으로 이동
	@GetMapping(value="/eventboardview/{id}")
	public String eventBoardView(@PathVariable("id") Long id, Model model) {
		EventBoard eventBoard = eventBoardService.findById(id);
		List<Member> member = memberService.findByEventBoardId(id);
		EventBoardDto eventBoardDto = EventBoardDto.of(eventBoard);
		model.addAttribute("eventBoardDto", eventBoardDto);
		model.addAttribute("member", member);
		return "/news/eventboardview";
	}

	//공지사항 게시판 페이지 이동
	@GetMapping(value="/announcement")
	public String announcement(Model model, @PageableDefault(page=0, size=10, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
		Page<Announcement> list = announcementService.findAll(pageable);
		
		model.addAttribute("announcementList", announcementService.findAll(pageable));

        //페이징	        
        int nowPage = list.getPageable().getPageNumber() + 1;	        
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, list.getTotalPages());

        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "news/announcement";	
	}
	
	//공지사항 글쓰기 페이지 이동
	@GetMapping(value="/announcementWrite")
	public String announcementWrite(@AuthenticationPrincipal User user, Model model) {
		if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
			model.addAttribute("freeBoardDto", new FreeBoardDto());
		} else {
			model.addAttribute("errorMessage", "관리자만 글을 작성 할수 있습니다.");
			return "news/announcement";	
		}
		return "news/announcementWrite";
	}
	
	//공지사항 글 저장
	@PostMapping(value="/announcementNew")
	public String announcementNew(@AuthenticationPrincipal User user, @Valid FreeBoardDto freeBoardDto, BindingResult bindingResult, Principal principal, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목과 내용을 입력해주세요.");
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "news/announcementWrite";
		}
		try {
			if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
				Member member = memberService.findByMid(user.getUsername());
				Announcement announcement = Announcement.createAnnouncement(freeBoardDto);
				announcementService.announcementSave(announcement, member);
			} else {
				model.addAttribute("errorMessage", "관리자만 글을 작성할 수 있습니다.");
				model.addAttribute("freeBoardDto", freeBoardDto);
				return "news/announcementWrite";
			}
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 작성중 에러가 발생했습니다.");
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "news/announcementWrite";
		}
		return "redirect:/news/announcement";
	}
	
	//공지사항 상세 페이지 이동
	@GetMapping(value="/announcementView/{id}")
	public String announcementView(@PathVariable("id") Long id, Model model) {
		Announcement announcement = announcementService.findById(id);
		FreeBoardDto freeBoardDto = FreeBoardDto.of(announcement);
		model.addAttribute("freeBoardDto", freeBoardDto);
		return "news/announcementView";
	}
	
	//공지사항 수정 페이지 이동
	@GetMapping(value="/announcementModify/{id}")
	public String announcementModify(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
		Announcement announcement = announcementService.findById(id);
		if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
				FreeBoardDto freeBoardDto = FreeBoardDto.of(announcement);
				model.addAttribute("freeBoardDto", freeBoardDto);
			} else {
				model.addAttribute("errorMessage", "관리자가 아니면 수정할 수 없습니다.");
				FreeBoardDto freeBoardDto = FreeBoardDto.of(announcement);
				model.addAttribute("freeBoardDto", freeBoardDto);
				return "news/announcementView";
			}
		return "news/announcementUpdate";
	}
	
	//공지사항 수정 등록
	@PutMapping(value="/announcementUpdate")
	public String announcementUpdate(@AuthenticationPrincipal User user, Model model,@Valid FreeBoardDto freeBoardDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMessage", "제목과 내용을 입력해주세요.");
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "news/announcementUpdate";
		}
		try {
			if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
				Member member = memberService.findByMid(user.getUsername());
				Announcement announcementUpdate = Announcement.createAnnouncement(freeBoardDto);
				announcementService.announcementUpdate(announcementUpdate, member);
			} else {
				model.addAttribute("errorMessage", "관리자만 글을 수정할 수 있습니다.");
				model.addAttribute("freeBoardDto", freeBoardDto);
				return "news/announcementUpdate";
			}
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "글 수정 중 에러가 발생했습니다");
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "news/announcementUpdate";
		}
		return "redirect:/news/announcement";
	}
	
	//공지사항 삭제하기
	@DeleteMapping(value="/announcementDelete/{id}")
	public String announcementDelete(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model, Principal principal) {
		Announcement announcement = announcementService.findById(id);
		try{
			if(String.valueOf(user.getAuthorities().iterator().next()).equals("ROLE_ADMIN")) {
				announcementService.announcementDelete(id);
				model.addAttribute("succMessage", "글이 삭제 되었습니다.");
			} else {
				model.addAttribute("errorMessage", "관리자만 글을 삭제 할 수 있습니다.");
				FreeBoardDto freeBoardDto = FreeBoardDto.of(announcement);
				model.addAttribute("freeBoardDto", freeBoardDto);
				return "news/announcementView";
			}
		} catch(Exception e) {
			model.addAttribute("errorMessage", "글 삭제 중 에러가 발생했습니다.");
			FreeBoardDto freeBoardDto = FreeBoardDto.of(announcement);
			model.addAttribute("freeBoardDto", freeBoardDto);
			return "news/announcementView";
		}
		return "redirect:/news/announcement";
	}
	
	//설문조사 페이지로 이동
	@GetMapping(value="/survey")
	public String survey(Principal principal, Model model) {
		try {
			Survey survey = surveyService.checkSurvey(principal.getName());
			if(survey!=null) {
				model.addAttribute("errorMessage", "이미 설문조사를 완료하였습니다.");
			}
		} catch(Exception e){
			model.addAttribute("errorMessage", "페이지 이동중 오류가 발생했습니다.");
		}
		return "news/survey";
	}
	
	//설문조사 결과
	@PostMapping(value="/surveyResult")
	@ResponseBody
	public String surveyResult(@RequestParam(value="result[]") List<Long> result, Principal principal) {
		surveyService.surveySave(result, principal.getName());
		return "succcess";
	}
	
}
