package com.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.dto.EventMemberDto;
import com.exposition.entity.EventBoard;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.EventBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventBoardService {

	private final EventBoardRepository eventBoardRepository;
	private final BoardRepository boardRepository;
	
	//이벤트 게시판 이동
	public List<EventBoard> findAll(){
		return eventBoardRepository.findAll();
	}
	
	//이벤트 상세 페이지 이동
	public EventBoard findById(Long id) {
		return eventBoardRepository.findById(id).get();
	}
	
	//이벤트 게시판 글 작성과 동시에 이벤트 당첨자 회원 뽑음
	public List<EventMemberDto> saveBoardAndSelectMember(EventBoard eventBoard){
		eventBoardRepository.save(eventBoard);
		return boardRepository.eventPrizeMember();
	}
	
}
