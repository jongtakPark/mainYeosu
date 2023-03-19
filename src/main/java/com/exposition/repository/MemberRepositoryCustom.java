package com.exposition.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.MemberFormDto;
import com.exposition.entity.Member;

public interface MemberRepositoryCustom {

	//자원봉사 지원자 조회
	Page<MemberFormDto> getAppVolunteer(MemberFormDto memberFormDto, Pageable pageable);
	
	//일반회원을 자원봉사 회원으로 변경
	void updateMemToVol(MemberFormDto memberFormDto);
	
	//일반회원을 자원봉사 회원으로 모두변경
	void updateAllMemToVol();
}
