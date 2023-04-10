package com.exposition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.FileDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.EventBoard;
import com.exposition.entity.Files;
import com.exposition.entity.TourBoard;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.EventBoardRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.TourBoardRepository;


import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TourBoardService {
	private final TourBoardRepository tourBoardRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;

	//주변 관광지 게시판 리스트 출력(페이징)
	public Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		return tourBoardRepository.getBoardMainPage(tourBoardDto, pageable);
	}
	
	
	//주변 관광지 글 저장
	public TourBoard saveTour(List<MultipartFile> files, TourBoardDto tourBoardDto) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		fileService.saveFile(files,tourBoard);
		
		return tourBoard;
	}
	
	//주변 관광지 상세 페이지 창
	@Transactional(readOnly=true)
	public TourBoardDto getTourBoardDetail(Long tourBoardId) {
		List<Files> fileList = fileRepository.findByTourBoardId(tourBoardId);
		List<FileDto> fileDtoList = new ArrayList<>();
		for(Files file : fileList) {
			FileDto fileDto = FileDto.of(file);
			fileDto.setSavePath("https://storage.googleapis.com/yeosu11/"+file.getSavePath());
			fileDtoList.add(fileDto);
		}
		
		TourBoard tourBoard = tourBoardRepository.findById(tourBoardId).orElseThrow(EntityNotFoundException::new);
		TourBoardDto tourBoardDto = TourBoardDto.of(tourBoard);
		tourBoardDto.setFileDtoList(fileDtoList);
		return tourBoardDto;
	}
	
	//주변관광지 수정 글 등록(사진수정을 한 경우)
	public Long updateTourBoard(TourBoardDto tourBoardDto, List<MultipartFile> files) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		Long tourId = findById(tourBoard.getId()).getId();
		List<Files> list = fileService.findByTourBoardId(tourId);
		fileService.deleteTourBoard(list,tourBoard);
		fileService.saveFile(files, tourBoard);
		return tourBoardDto.getId();
	}
	
	//주변관광지 수정 글 등록(사진수정을 하지 않은 경우)
	public Long updateOnlyTourBoard(TourBoardDto tourBoardDto, List<MultipartFile> files) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		Long tourId = findById(tourBoard.getId()).getId();
		List<Files> list = fileService.findByTourBoardId(tourId);
//		fileService.deleteTourBoard(list,tourBoard);
//		fileService.saveFile(files, tourBoard);
		return tourBoardDto.getId();
	}
	

	//주변 관광지 글 삭제(나중에 첨부파일이 있는 게시판은 이걸 이용해서 삭제하면 됨)
	public void deleteBoard(Long id) throws Exception{
		Long tourBoardId = findById(id).getId();
		List<Files> list = fileService.findByTourBoardId(tourBoardId);
		fileService.deleteFile(list);
		tourBoardRepository.deleteById(id);
	}
	
	//주변 관광지 글 찾아오기(첨부파일을 먼저 삭제 후 게시글을 지우기 위해)
	public TourBoard findById(Long id){
		return tourBoardRepository.findById(id).get();
	}
	
}
