package com.exposition.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
	private final EventBoardRepository eventBoardRepository;
	private final BoardRepository boardRepository;

	//주변 관광지 게시판 리스트 출력(페이징)
	public Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		return tourBoardRepository.getBoardMainPage(tourBoardDto, pageable);
	}
	
	
	//주변 관광지 글 저장
	public TourBoard saveTour(List<MultipartFile> files, TourBoardDto tourBoardDto) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		for(int i=0; i<files.size(); i++) {
			Files file = new Files();
			file.setTourboard(tourBoard);
			if(i==0) 
				file.setThumbnail("Y");
			else
				file.setThumbnail("N");
			fileService.saveFile(file, files.get(i));
		}
		return tourBoard;
	}
	
	//주변 관광지 상세 페이지 창
	@Transactional(readOnly=true)
	public TourBoardDto getTourBoardDetail(Long tourBoardId) {
		List<Files> fileList = fileRepository.findByTourboardId(tourBoardId);
		List<FileDto> fileDtoList = new ArrayList<>();
		List<FileDto> fileIds = new ArrayList<>();
		for(Files file : fileList) {
			FileDto fileDto = FileDto.of(file);
			fileDtoList.add(fileDto);
		}
		
		TourBoard tourBoard = tourBoardRepository.findById(tourBoardId).orElseThrow(EntityNotFoundException::new);
		TourBoardDto tourBoardDto = TourBoardDto.of(tourBoard);
		tourBoardDto.setFileDtoList(fileDtoList);
		return tourBoardDto;
	}
	
	//게시글 수정 글 등록
	public Long updateTourBoard(TourBoardDto tourBoardDto, List<MultipartFile> fileList) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		deleteFile(tourBoard.getId());
		for(int i=0; i<fileList.size(); i++) {
			Files file = new Files();
			file.setTourboard(tourBoard);
			if(i==0) 
				file.setThumbnail("Y");
			else
				file.setThumbnail("N");
			fileService.saveFile(file, fileList.get(i));
		}
		return tourBoardDto.getId();
	}
	
	
	//주변 관광지 글 삭제(나중에 첨부파일이 있는 게시판은 이걸 이용해서 삭제하면 됨)
	public void deleteBoard(Long id) {
		Long tourBoardId = findById(id).getId();
		List<Files> list = fileService.findByTourBoardId(tourBoardId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
		tourBoardRepository.deleteById(id);
	}
	
	//첨부파일만 삭제하기(게시글 수정시에 사용될것)
	public void deleteFile(Long id) {
		Long tourBoardId = findById(id).getId();
		List<Files> list = fileService.findByTourBoardId(tourBoardId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
	}
	
	//주변 관광지 글 찾아오기(첨부파일을 먼저 삭제 후 게시글을 지우기 위해)
	public TourBoard findById(Long id){
		return tourBoardRepository.findById(id).get();
	}
	
	//이벤트 게시판 글 작성과 동시에 이벤트 당첨자 회원 뽑음
	public List<EventMemberDto> saveBoardAndSelectMember(EventBoard eventBoard){
		eventBoardRepository.save(eventBoard);
		return boardRepository.eventPrizeMember();
	}
	
}
