package com.exposition.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.CompanyFormDto;

public interface CompanyRepositoryCustom {

	//업체등록 신청한 기업 조회
	Page<CompanyFormDto> getApprovalCom(CompanyFormDto companyFormDto, Pageable pageable);
	
	//업체등록 신청한 기업 승인
	public void updateApp(String com);
	
	//기업회원 모두 조회
	Page<CompanyFormDto> findAllCom(Pageable pageable);
	
	//예약완료된 기업 모두 조회
	Long findSucReservationCom();
	
	//기업회원 갯수 조회
	Long findAllComCount();
}
