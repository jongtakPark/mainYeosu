package com.exposition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.FileDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.File;
import com.exposition.entity.TourBoard;
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
	@RequestMapping(value="/tour", method= {RequestMethod.GET, RequestMethod.POST})
	public String tourPage() {
		return "news/tourboard";
	}
		
	//주변관광지 글 작성 페이지 이동
	@GetMapping(value="/tourwrite")
	public String tourWrite(Model model) {
		model.addAttribute("tourBoardDto", new TourBoardDto());
		return "news/tourboardwrite";
	}
	
	//주변 관광지 글 등록
	@PostMapping(value="/toursave")
	public String tourSave(MultipartFile files, Model model, FileDto fileDto, TourBoardDto tourBoarDto) {
		String name="";
		for(int i=0; i<15; i++) {
			char names = (char)((int)(Math.random()*25)+97);
			name += names;
		}
		String imgName = files + name;
		fileDto.setImg(imgName);
		File file = File.createFile(fileDto);
		TourBoard tourBoard = TourBoard.createTourBoard(tourBoarDto);
		tourBoardService.saveTour(tourBoard,file);
		return "redirect:/news/tour";
	}
}
