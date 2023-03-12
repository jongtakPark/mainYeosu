package com.exposition.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.File;
import com.exposition.entity.TourBoard;
import com.exposition.repository.TourBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TourBoardService {

	private final TourBoardRepository tourBoardRepository;
	private final FileService fileService;
//	//주변관광지 글 저장
//	public TourBoard saveTour(TourBoard tourBoard,File file) {
//		file.setTourboard(tourBoard);
//		fileService.saveFile(file);
//		return tourBoardRepository.save(tourBoard);
//	}
	
	//주변 관광지 게시판 리스트 출력(페이징) 3월12일 수정한거
//	public Page<TourBoard> tourBoardList(Pageable pageable){
//		return tourBoardRepository.findAll(pageable);
//	}
	
	
	//주변 관광지 게시판 리스트 출력(페이징)
	public Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		return tourBoardRepository.getBoardMainPage(tourBoardDto, pageable);
	}
	
	
	//주변 관광지 글 저장
	public TourBoard saveTour(List<MultipartFile> files, TourBoardDto tourBaordDto) throws Exception {
		TourBoard tourBoard = tourBaordDto.createItem();
		tourBoardRepository.save(tourBoard);
		for(int i=0; i<files.size(); i++) {
			File file = new File();
			file.setTourboard(tourBoard);
			if(i==0) 
				file.setThumbnail("Y");
			else
				file.setThumbnail("N");
			fileService.saveFile(file, files.get(i));
		}
		return tourBoard;
	}
}
