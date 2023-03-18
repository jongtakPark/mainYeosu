package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.exposition.constant.Role;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.QMemberFormDto;
import com.exposition.entity.Member;
import com.exposition.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

private JPAQueryFactory queryFactory;
	
	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public Page<MemberFormDto> getAppVolunteer(MemberFormDto memberFormDto, Pageable pageable){
		QMember mem = QMember.member;
		
		QueryResults<MemberFormDto> result = queryFactory
				.select(new QMemberFormDto(mem.mid, mem.name, mem.email)).from(mem)
				.where(mem.approval.eq("Y")).fetchResults();
		
		List<MemberFormDto> list = result.getResults();
		Long total = result.getTotal();
		return new PageImpl<>(list, pageable, total);

	}
	
	@Override 
	public void updateMemToVol(MemberFormDto memberFormDto){
		QMember mem = QMember.member;
		
		queryFactory.update(mem).set(mem.role, Role.VOLUNTEER).set(mem.approval, "O").where(mem.approval.eq("Y")).execute();
	}
}
