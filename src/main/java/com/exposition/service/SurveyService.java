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
import com.exposition.entity.Survey;
import com.exposition.entity.Volunteer;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.CompanyRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.IdeaRepository;
import com.exposition.repository.MemberRepository;
import com.exposition.repository.ReviewRepository;
import com.exposition.repository.SurveyRepository;
import com.exposition.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {
	private final SurveyRepository surveyRepository;
	private final MemberRepository memberRepository;
	private final MemberService memberService;
	
	// 설문조사 저장
	public void save(List<Long> result, String name) {
		Survey survey = new Survey();
		Member member = memberService.findByMid(name);
		member.setSurvey("Y");
		survey.setFirst(result.get(0)); survey.setSecond(result.get(1)); survey.setThird(result.get(2)); survey.setFourth(result.get(3)); survey.setMember(member);
		memberRepository.save(member);
		surveyRepository.save(survey);
	}
} 	
	

	

	

