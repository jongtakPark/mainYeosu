package com.exposition.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.TourBoardDto;

public interface BoardRepositoryCustom {

	Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable);
}
