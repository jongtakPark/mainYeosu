package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.exposition.dto.CompanyFormDto;
import com.exposition.entity.Company;
import com.exposition.entity.QCompany;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

private JPAQueryFactory queryFactory;
	
	public CompanyRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public List<Company> getApprovalCom(Company company){
		
		List<Company> results = queryFactory
				.selectFrom(QCompany.company)
				.where(QCompany.company.approval.eq("Y"))
				.orderBy(QCompany.company.id.desc())
				.fetch();
		
		return results;
	}
}
