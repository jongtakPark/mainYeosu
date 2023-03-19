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
	
	//자원봉사 지원자 조회
	@Override
	public Page<MemberFormDto> getAppVolunteer(MemberFormDto memberFormDto, Pageable pageable){
		QMember mem = QMember.member;
		
		QueryResults<MemberFormDto> result = queryFactory
				.select(new QMemberFormDto(mem.mid, mem.name, mem.email)).from(mem)
				.where(mem.approval.eq("W")).fetchResults();
		
		List<MemberFormDto> list = result.getResults();
		Long total = result.getTotal();
		return new PageImpl<>(list, pageable, total);

	}
	
	//일반회원을 자원봉사 회원으로 변경
	@Override 
	public void updateMemToVol(MemberFormDto memberFormDto){
		QMember mem = QMember.member;
		
		queryFactory.update(mem)
		.set(mem.role, Role.VOLUNTEER).set(mem.approval, "Y")
		.where(mem.approval.eq("W"))
		.where(mem.mid.eq(memberFormDto.getMid()))
		.execute();
		
	}
	
	//일반회원을 자원봉사 회원으로 모두변경
	@Override 
	public void updateAllMemToVol(){
		QMember mem = QMember.member;
		
		queryFactory.update(mem)
		.set(mem.role, Role.VOLUNTEER).set(mem.approval, "Y")
		.where(mem.approval.eq("W"))
		.execute();
		
	}
}
