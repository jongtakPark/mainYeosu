package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.QCompanyFormDto;
import com.exposition.entity.QCompany;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

private JPAQueryFactory queryFactory;
	
	public CompanyRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public Page<CompanyFormDto> getApprovalCom(CompanyFormDto companyFormDto, Pageable pageable){
		QCompany company = QCompany.company;
		
		QueryResults<CompanyFormDto> result = queryFactory
				.select(new QCompanyFormDto(company.com, company.name, company.email , company.approval, company.startDay, company.finishDay))
				.from(company)
				.where(company.approval.eq("Y")).fetchResults();
		
		List<CompanyFormDto> list = result.getResults();
		Long total = result.getTotal();
		return new PageImpl<>(list, pageable, total);
	}
}
