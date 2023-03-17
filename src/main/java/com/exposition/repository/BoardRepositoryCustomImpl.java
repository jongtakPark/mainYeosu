package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.QBoardMainDto;
import com.exposition.dto.QEventMemberDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.QFiles;
import com.exposition.entity.QMember;
import com.exposition.entity.QTourBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public BoardRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		QTourBoard tourBoard = QTourBoard.tourBoard;
		QFiles file = QFiles.files;
		
		QueryResults<BoardMainDto> result = queryFactory
				.select(new QBoardMainDto(tourBoard.id, tourBoard.title, tourBoard.content, file.savePath))
				.from(file)
				.join(file.tourboard, tourBoard)
				.where(file.thumbnail.eq("Y"))
				.orderBy(tourBoard.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		
		List<BoardMainDto> list = result.getResults();
		Long total = result.getTotal();
		return new PageImpl<>(list, pageable, total);
				
	}
	
	@Override
	public List<EventMemberDto> eventPrizeMember() {
		QMember member = QMember.member;
		
		List<EventMemberDto> results = queryFactory.select(new QEventMemberDto(member.id, member.mid, member.email, member.eventCount))
				.from(member).where(member.eventCount.eq("N"))
				.where(member.survey.eq("Y"))
				.fetch();
		
		return results;		
	}
}
