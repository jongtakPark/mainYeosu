package com.exposition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.FileDto;
import com.exposition.dto.FreeBoardDto;
import com.exposition.entity.Company;
import com.exposition.entity.Files;
import com.exposition.entity.FreeBoard;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
import com.exposition.entity.Survey;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.ReviewRepository;
import com.exposition.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final SurveyRepository surveyRepository;
	private final ReviewRepository reviewRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;
	
	//자유 게시판 글 작성
	public FreeBoard saveBoard(FreeBoard freeBoard) {
		return boardRepository.save(freeBoard);
	}
	
	//자유 게시판 리스트 출력(페이징)
	public Page<FreeBoard> boardList(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	//자유 게시판 상세보기 출력
	public Optional<FreeBoard> findBoard(Long id) {
		return boardRepository.findById(id);
	}
	//자유 게시판 글 수정하기
	public FreeBoard updateBoard(Long id) {
		return boardRepository.findById(id).get();
	}
	//자유 게시판 글 삭제
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}
	
	//관람후기 게시판 리스트 출력(페이징)
	public Page<Review> reviewBoardList(Pageable pageable){
		return reviewRepository.findAll(pageable);
	}
	
	//관람후기 글 저장(일반회원)
	public void reviewSave(Review review, Member member, List<MultipartFile> files) throws Exception{
		review.setMember(member);
		reviewRepository.save(review);
		if(!files.get(0).isEmpty()) {
			for(int i=0; i<files.size(); i++) {
				Files file = new Files();
				file.setReview(review);
				fileService.saveFile(file, files.get(i));	
			}
		}
	}
	
	//관람후기 글 저장(기업회원)
	public void reviewSave(Review review, Company company, List<MultipartFile> files) throws Exception {
		review.setCompany(company);
		reviewRepository.save(review);
		if(!files.get(0).isEmpty()) {
			for(int i=0; i<files.size(); i++) {
				Files file = new Files();
				file.setReview(review);
				fileService.saveFile(file, files.get(i));	
			}
		}
	}
	
	//관람후기 상세 글 페이지
	public FreeBoardDto reviewAndFileFindById(Long id) {
		List<Files> fileList = fileRepository.findByReviewId(id);
		if(!fileList.isEmpty()) {
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDtoList.add(fileDto);
			}
			Review review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(review);
			freeBoardDto.setFileDtoList(fileDtoList);
			return freeBoardDto;
		} else {
			Review review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(review);
			return freeBoardDto;
		}
	}
	
	//관람후기 글 Id로 찾기
	public Review reviewFindById(Long id) {
		return reviewRepository.findById(id).get();
	}
	
	//관람후기 수정 글 저장(일반회원)
	public void reviewUpdate(Review review, List<MultipartFile> files, Member member) throws Exception{
		review.setMember(member);
		reviewRepository.save(review);
		deleteFile(review.getId());
		if(!files.get(0).isEmpty()) {
			for(int i=0; i<files.size(); i++) {
				Files file = new Files();
				file.setReview(review);
				fileService.saveFile(file, files.get(i));	
			}
		}
	}
	
	//관람후기 수정 글 저장(기업)
	public void reviewUpdate(Review review, List<MultipartFile> files, Company company) throws Exception{
		review.setCompany(company);
		reviewRepository.save(review);
		deleteFile(review.getId());
		if(!files.get(0).isEmpty()) {
			for(int i=0; i<files.size(); i++) {
				Files file = new Files();
				file.setReview(review);
				fileService.saveFile(file, files.get(i));	
			}
		}
	}
	
	//첨부파일만 삭제하기(게시글 수정시에 사용될것)
	public void deleteFile(Long id) {
		Long reviewId = reviewFindById(id).getId();
		List<Files> list = fileService.findByReviewId(reviewId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
	}
	
	//관람후기 글 삭제
	public void deleteReview(Long id) {
		reviewRepository.deleteById(id);
	}
	
	//설문조사 게시판 리스트 출력
	public Page<Survey> surveyBoardList(Pageable pageable){
		return surveyRepository.findAll(pageable);
	}
	//설문조사 글 저장
	public void surveyBoardSave(Survey survey) {
		surveyRepository.save(survey);
	}
	//설문조사 글 찾기
	public Survey findSurveyBoard(Long id) {
		return surveyRepository.findById(id).get();
	}
	
}
