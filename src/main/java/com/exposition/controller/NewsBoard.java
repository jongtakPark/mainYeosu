package com.exposition.controller;

import java.util.List;

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
import com.exposition.service.FileService;
import com.exposition.service.TourBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsBoard {

	private final FileService fileService;
	private final TourBoardService tourBoardService;
	//주변관광지 페이지 이동
//	@RequestMapping(value="/tour", method= {RequestMethod.GET, RequestMethod.POST})
//	public String tourPage(Model model, @PageableDefault(page=0, size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
//		
//		Page<TourBoard> list = tourBoardService.tourBoardList(pageable);
//		System.out.println(list.getContent().get(1).getId());
//		model.addAttribute("tourboard", list);
//	    //페이징	        
//	    int nowPage = list.getPageable().getPageNumber() + 1;	        
//	    int startPage =  Math.max(nowPage - 4, 1);
//	    int endPage = Math.min(nowPage+9, list.getTotalPages());
//
//	    model.addAttribute("list", list);
//	    model.addAttribute("nowPage",nowPage);
//	    model.addAttribute("startPage", startPage);
//	    model.addAttribute("endPage", endPage);
//		
//		return "news/tourboard";
//	}
	
	//주변관광지 페이지 이동
	@RequestMapping(value="/tour", method= {RequestMethod.GET, RequestMethod.POST})
	public String tourPage(Model model, TourBoardDto tourBoardDto) {
		
		Pageable pageable = PageRequest.of(0, 4);
		Page<BoardMainDto> tourBoardList = tourBoardService.getBoardMainPage(tourBoardDto, pageable);
		System.out.println(tourBoardList.getNumber());
		System.out.println(tourBoardList.getPageable());
		System.out.println(tourBoardList.getTotalPages());
		System.out.println(tourBoardList.getNumberOfElements());
		System.out.println(tourBoardList.getSize());
		int nowPage = tourBoardList.getTotalPages() -1;	        
		int startPage =  Math.max(nowPage - 4, 1);
		int endPage = Math.min(nowPage+9, tourBoardList.getTotalPages());
		model.addAttribute("tourboard", tourBoardList);
//		model.addAttribute("tourBoardDto", tourBoardDto);
//		model.addAttribute("maxPage", 5);
		
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
	
//	//주변 관광지 글 등록
//	@PostMapping(value="/toursave")
//	public String tourSave(@RequestParam("files") List<MultipartFile> files, Model model, FileDto fileDto, TourBoardDto tourBoarDto) {
//		String name="";
//		for(int i=0; i<15; i++) {
//			char names = (char)((int)(Math.random()*25)+97);
//			name += names;
//		}
//		String imgName = files + name;
//		fileDto.setImg(imgName);
//		File file = File.createFile(fileDto);
//		TourBoard tourBoard = TourBoard.createTourBoard(tourBoarDto);
//		tourBoardService.saveTour(tourBoard,file);
//		return "redirect:/news/tour";
//	}
	
	//주변관광지 글 등록
	@PostMapping(value="/toursave")
	public String tourSave(@RequestParam(value = "files", required = false) List<MultipartFile> files, Model model, @Valid TourBoardDto tourBoardDto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "news/tourboardwrite";
		}
		if(files.get(0).isEmpty() && tourBoardDto.getId() == null) {
			model.addAttribute("errorMessage", "이미지는 필수 입니다.");
			return "news/tourboardwrite";
		}
		try {
			tourBoardService.saveTour(files, tourBoardDto);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "글 작성 중 에러가 발생했습니다.");
			return "news/tourboardwrite";
		}
		return "redirect:/news/tour";
	}
}
