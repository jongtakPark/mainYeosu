package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.QCompanyFormDto;
import com.exposition.entity.Company;
import com.exposition.entity.QCompany;
import com.exposition.entity.QReservation;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

private JPAQueryFactory queryFactory;
	
	public CompanyRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	//업체등록 신청한 기업 조회
	@Override
	public Page<CompanyFormDto> getApprovalCom(CompanyFormDto companyFormDto, Pageable pageable){
		QCompany company = QCompany.company;
		QReservation reservation = QReservation.reservation;
		QueryResults<CompanyFormDto> result = queryFactory
				.select(new QCompanyFormDto(company.com, company.name, company.email , company.approval, reservation.startDay, reservation.endDay))
				.from(company)
				.where(company.approval.eq("W")).fetchResults();
		
		List<CompanyFormDto> list = result.getResults();
		Long total = result.getTotal();
		return new PageImpl<>(list, pageable, total);
	}
	
	//업체등록 신청한 기업 승인
	@Override
	public void updateApp(String com) {
		QCompany company = QCompany.company;
		
		queryFactory.update(company)
		.set(company.approval, "Y")
		.where(company.com.eq(com))
		.execute();
	}
}
