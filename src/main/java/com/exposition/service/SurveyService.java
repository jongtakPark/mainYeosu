package com.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.Member;
import com.exposition.entity.Survey;
import com.exposition.repository.MemberRepository;
import com.exposition.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

	private final MemberRepository memberRepository;
	private final SurveyRepository surveyRepository;
	
	//설문조사 저장
	public void surveySave(List<Long> result, String mid) {
		Survey survey = new Survey();
		survey.setFirst(result.get(0));
		survey.setSecond(result.get(1));
		survey.setThird(result.get(2));
		survey.setFourth(result.get(3));
		Member member = memberRepository.findByMid(mid);
		survey.setMember(member);
		member.setSurvey("Y");
		surveyRepository.save(survey);
		memberRepository.save(member);
	}
	
	//설문조사 했는지 확인
	public Survey checkSurvey(String mid) {
		Member member = memberRepository.findByMid(mid);
		Survey survey = surveyRepository.findByMember(member);
		return survey;
	}
}
