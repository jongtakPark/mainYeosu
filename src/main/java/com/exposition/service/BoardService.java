package com.exposition.service;


import java.util.ArrayList;
import java.util.List;

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
import com.exposition.entity.Idea;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
import com.exposition.entity.Volunteer;
import com.exposition.repository.CompanyRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.IdeaRepository;
import com.exposition.repository.MemberRepository;
import com.exposition.repository.ReviewRepository;
import com.exposition.repository.VolunteerRepository;



import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
	private final ReviewRepository reviewRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
	private final CompanyRepository companyRepository;
	private final IdeaRepository ideaRepository;
	private final VolunteerRepository volunteerRepository; 
	
	//관람후기 게시판 리스트 출력(페이징)
	public Page<Review> reviewBoardList(Pageable pageable){
		return reviewRepository.findAll(pageable);
	}
	
	//관람후기 글 저장
	public void reviewSave(Review review, String id,List <MultipartFile> files) throws Exception{		
		try {
			Member member = memberRepository.findByMid(id);
			Company company = companyRepository.findByCom(id);
			review.setMember(member);
			review.setCompany(company);
			reviewRepository.save(review);
			fileService.saveFile(files,review);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	//관람후기 상세 글 페이지
	public FreeBoardDto reviewAndFileFindById(Long id) {
		List<Files> fileList = fileRepository.findByReviewId(id);
		
		if(!fileList.isEmpty()) {
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDto.setSavePath("https://storage.googleapis.com/yeosu11/"+file.getSavePath());
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
	
	//관람후기 수정 글 저장
	public void reviewUpdate(Review review, List<MultipartFile> files, String id) throws Exception{
		try {
			reviewRepository.save(review);
			Member member = memberRepository.findByMid(id);
			review.setMember(member);
			
			Company company = companyRepository.findByCom(id);
			review.setCompany(company);	
			if(!files.get(0).isEmpty()) {
				Long reviewId = reviewFindById(review.getId()).getId();
			List<Files> list = fileService.findByReviewId(reviewId);
			fileService.deleteReview(list,review);
			fileService.saveFile(files,review);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//관람후기 글 삭제
	public void deleteReview(Long id) throws Exception{
		Long reviewId = reviewFindById(id).getId();
		List<Files> list = fileService.findByReviewId(reviewId);
		fileService.deleteFile(list);
		reviewRepository.deleteById(id);
	}
	
	//국민아이디어 게시판 리스트 출력(페이징)
	public Page<Idea> ideaBoardList(Pageable pageable){
		return ideaRepository.findAll(pageable);
	}
	
	//국민아이디어 글 저장
	public void ideaSave(Idea idea, String id, List<MultipartFile> files) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			Company company = companyRepository.findByCom(id);
			idea.setMember(member);
			idea.setCompany(company);
			ideaRepository.save(idea);
			fileService.saveFile(files,idea);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	//국민아이디어 상세 글 페이지
	public FreeBoardDto ideaAndFileFindById(Long id) {
		List<Files> fileList = fileRepository.findByIdeaId(id);
		if(!fileList.isEmpty()) {
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDto.setSavePath("https://storage.googleapis.com/yeosu11/"+file.getSavePath());
				fileDtoList.add(fileDto);
			}
			Idea idea = ideaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(idea);
			freeBoardDto.setFileDtoList(fileDtoList);
			return freeBoardDto;
		} else {
			Idea idea = ideaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(idea);
			return freeBoardDto;
		}
	}
	
	//국민아이디어 글 Id로 찾기
	public Idea ideaFindById(Long id) {
		return ideaRepository.findById(id).get();
	}
	
	//국민아이디어 수정 글 저장
	public void ideaUpdate(Idea idea, List<MultipartFile> files, String id) throws Exception{
		try {
			ideaRepository.save(idea);
			
			Member member = memberRepository.findByMid(id);
			idea.setMember(member);
			
			Company company = companyRepository.findByCom(id);
			idea.setCompany(company);
			if(!files.get(0).isEmpty()) {
				Long ideaId = ideaFindById(idea.getId()).getId();
				List<Files> list = fileService.findByIdeaId(ideaId);
				fileService.deleteIdea(list, idea);
				fileService.saveFile(files, idea);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
	//국민아이디어 글 삭제
	public void deleteIdea(Long id) throws Exception {
		Long ideaId = ideaFindById(id).getId();
		List<Files> list = fileService.findByIdeaId(ideaId);
		fileService.deleteFile(list);
		ideaRepository.deleteById(id);
	}
	
	//자원봉사 게시판 리스트 출력
	public Page<Volunteer> volunteerBoardList(Pageable pageable){
		return volunteerRepository.findAll(pageable);
	}
	
	//자원봉사자 글 저장
	public void volunteerSave(Volunteer volunteer, String id, List<MultipartFile> files) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			volunteer.setMember(member);
			volunteerRepository.save(volunteer);
			fileService.saveFile(files, volunteer);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//자원봉사게시판 상세 글 페이지
	public FreeBoardDto volunteerAndFileFindById(Long id) {
		List<Files> fileList = fileRepository.findByVolunteerId(id);
		if(!fileList.isEmpty()) {
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDto.setSavePath("https://storage.googleapis.com/yeosu11/"+file.getSavePath());
				fileDtoList.add(fileDto);
			}
			Volunteer volunteer = volunteerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(volunteer);
			freeBoardDto.setFileDtoList(fileDtoList);
			return freeBoardDto;
		} else {
			Volunteer volunteer = volunteerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
			FreeBoardDto freeBoardDto = FreeBoardDto.of(volunteer);
			return freeBoardDto;
		}
	}
		
	//자원봉사게시판 글 Id로 찾기
	public Volunteer volunteerFindById(Long id) {
		return volunteerRepository.findById(id).get();
	}
	
	//자원봉사게시판 수정 글 저장
	public void volunteerUpdate(Volunteer volunteer, List<MultipartFile> files, String id) throws Exception{
		try {
			volunteerRepository.save(volunteer);
			Member member = memberRepository.findByMid(id);
			volunteer.setMember(member);
			if(!files.get(0).isEmpty()) {
				Long volunteerId = volunteerAndFileFindById(volunteer.getId()).getId();
				List<Files> list = fileService.findByVolunteerId(volunteerId);
				fileService.deleteVolunteer(list,volunteer);
				fileService.saveFile(files, volunteer);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	//자원봉사자 글 삭제
	public void deleteVolunteer(Long id) throws Exception {
		Long volunteerId = volunteerFindById(id).getId();
		List<Files> list = fileService.findByVolunteerId(volunteerId);
		fileService.deleteFile(list);
		volunteerRepository.deleteById(id);
	}
}
   