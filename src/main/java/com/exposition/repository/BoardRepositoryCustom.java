package com.exposition.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.TourBoardDto;

public interface BoardRepositoryCustom {

	//주변 관광지 게시글 리스트 출력
	Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable);
	
	
	//이벤트 페이지에 당첨자 출력
	List<EventMemberDto> eventPrizeMember();
	
	//여수섬키워드
	Page<BoardMainDto> getKeywordMainPage(TourBoardDto tourBoardDto, Pageable pageable);
	
}
