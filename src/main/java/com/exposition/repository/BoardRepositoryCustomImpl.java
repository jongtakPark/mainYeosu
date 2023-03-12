package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.QBoardMainDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.QFile;
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
		QFile file = QFile.file;
		
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
}
