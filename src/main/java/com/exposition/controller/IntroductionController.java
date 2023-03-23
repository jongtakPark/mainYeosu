package com.exposition.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.service.KeywordService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value="/introduction")
@RequiredArgsConstructor
public class IntroductionController {

	private final KeywordService keywordService;
	//여수섬 키워드 페이지 이동
	@RequestMapping(value="/keyword", method= {RequestMethod.GET, RequestMethod.POST})
	public String tourPage(Model model, TourBoardDto tourBoardDto, Optional<Integer> page) {
		
		Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 6);
		Page<BoardMainDto> keywordList = keywordService.getKeywordMainPage(tourBoardDto, pageable);
		model.addAttribute("keyword", keywordList);
		int nowPage = keywordList.getPageable().getPageNumber() + 1 ;
	    int startPage =  Math.max(nowPage - 4, 1);
	    int endPage = Math.min(nowPage+9, keywordList.getTotalPages());
	    model.addAttribute("nowPage",nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    
		return "introduction/yeosuKeyword";
	}
	
	//여수섬 키워드 글 쓰기 페이지 이동
	@GetMapping(value="/keywordWrite")
	public String keywordWrite(Model model) {
		model.addAttribute("keywordWrite", new TourBoardDto());
		return "introduction/keywordWrite";
	}
	
	//여수섬 키워드 글 저장
	@PostMapping(value="/keywordSave")
	public String keywordSave(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid TourBoardDto tourBoardDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "introduction/keywordWrite";
		}
		if(files.get(0).isEmpty() && tourBoardDto.getId() == null) {
			model.addAttribute("errorMessage", "이미지는 필수 입니다.");
			return "introduction/keywordWrite";
		}
		try {
			keywordService.saveTour(files, tourBoardDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "글 작성 중 에러가 발생했습니다.");
			return "introduction/keywordWrite";
		}
		return "redirect:/introduction/keyword";
	}
	
	//여수섬 키워드 글 삭제
//	@DeleteMapping(value="delete/")
}
