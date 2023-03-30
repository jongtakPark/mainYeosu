package com.exposition.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import com.exposition.entity.FreeBoard;
import com.exposition.entity.Member;

import com.exposition.dto.FileDto;
import com.exposition.dto.FreeBoardDto;
import com.exposition.entity.Company;
import com.exposition.entity.Files;
import com.exposition.entity.Idea;
import com.exposition.entity.Member;
import com.exposition.entity.Review;
>>>>>>> main
import com.exposition.entity.Survey;
import com.exposition.entity.Volunteer;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.MemberRepository;
import com.exposition.repository.SurveyRepository;
import com.exposition.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final SurveyRepository surveyRepository;
	private final MemberRepository memberRepository;

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
	

	// 자유 게시판 글 작성
	public FreeBoard saveBoard(FreeBoard freeBoard,String mid) {
		Member member = new Member();
		member = memberRepository.findByMid(mid);
		freeBoard.setMember(member);
		return boardRepository.save(freeBoard);
	}
	
	//자유 게시판 리스트 출력(페이징)
	public Page<FreeBoard> boardList(Pageable pageable){
		return boardRepository.findAll(pageable);

	//관람후기 글 저장
	public void reviewSave(Review review, String id, List<MultipartFile> files) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			Company company = companyRepository.findByCom(id);
			review.setMember(member);
			review.setCompany(company);
			reviewRepository.save(review);
			if(!files.get(0).isEmpty()) {
				for(int i=0; i<files.size(); i++) {
					Files file = new Files();
					file.setReview(review);
					fileService.saveFile(file, files.get(i));	
				}
			}
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
			Member member = memberRepository.findByMid(id);
			review.setMember(member);
			Company company = companyRepository.findByCom(id);
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
		} catch(Exception e) {
			e.printStackTrace();
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
		Long reviewId = reviewFindById(id).getId();
		List<Files> list = fileService.findByReviewId(reviewId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
		reviewRepository.deleteById(id);
	}
	
	//아이디어 게시판 리스트 출력(페이징)
	public Page<Idea> ideaBoardList(Pageable pageable){
		return ideaRepository.findAll(pageable);
	}
	
	//아이디어 글 저장
	public void ideaSave(Idea idea, String id, List<MultipartFile> files) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			Company company = companyRepository.findByCom(id);
			idea.setMember(member);
			idea.setCompany(company);
			ideaRepository.save(idea);
			if(!files.get(0).isEmpty()) {
				for(int i=0; i<files.size(); i++) {
					Files file = new Files();
					file.setIdea(idea);
					fileService.saveFile(file, files.get(i));	
				}
			}
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
	
	//관람후기 수정 글 저장
	public void ideaUpdate(Idea idea, List<MultipartFile> files, String id) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			idea.setMember(member);
			Company company = companyRepository.findByCom(id);
			idea.setCompany(company);
			ideaRepository.save(idea);
			ideaDeleteFile(idea.getId());
			if(!files.get(0).isEmpty()) {
				for(int i=0; i<files.size(); i++) {
					Files file = new Files();
					file.setIdea(idea);
					fileService.saveFile(file, files.get(i));	
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//첨부파일만 삭제하기(게시글 수정시에 사용될것)
	public void ideaDeleteFile(Long id) {
		Long ideaId = ideaFindById(id).getId();
		List<Files> list = fileService.findByIdeaId(ideaId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
	}
	
	//국민아이디어 글 삭제
	public void deleteIdea(Long id) {
		Long ideaId = ideaFindById(id).getId();
		List<Files> list = fileService.findByIdeaId(ideaId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
		ideaRepository.deleteById(id);
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


	//자원봉사 게시판 리스트 출력
	public Page<Volunteer> volunteerBoardList(Pageable pageable){
		return volunteerRepository.findAll(pageable);
	}
	
	//아이디어 글 저장
	public void volunteerSave(Volunteer volunteer, String id, List<MultipartFile> files) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			volunteer.setMember(member);
			volunteerRepository.save(volunteer);
			if(!files.get(0).isEmpty()) {
				for(int i=0; i<files.size(); i++) {
					Files file = new Files();
					file.setVolunteer(volunteer);
					fileService.saveFile(file, files.get(i));	
				}
			}
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
	
	//관람후기 수정 글 저장
	public void volunteerUpdate(Volunteer volunteer, List<MultipartFile> files, String id) throws Exception{
		try {
			Member member = memberRepository.findByMid(id);
			volunteer.setMember(member);
			volunteerRepository.save(volunteer);
			volunteerDeleteFile(volunteer.getId());
			if(!files.get(0).isEmpty()) {
				for(int i=0; i<files.size(); i++) {
					Files file = new Files();
					file.setVolunteer(volunteer);
					fileService.saveFile(file, files.get(i));	
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	//첨부파일만 삭제하기(게시글 수정시에 사용될것)
	public void volunteerDeleteFile(Long id) {
		Long volunteerId = volunteerFindById(id).getId();
		List<Files> list = fileService.findByVolunteerId(volunteerId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
	}
	
	//국민아이디어 글 삭제
	public void deleteVolunteer(Long id) {
		Long volunteerId = volunteerFindById(id).getId();
		List<Files> list = fileService.findByVolunteerId(volunteerId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
		volunteerRepository.deleteById(id);
	}
	
>>>>>>> main
}
	

