package com.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.EventBoard;
import com.exposition.repository.EventBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventBoardService {

	private final EventBoardRepository eventBoardRepository;
	
	//이벤트 게시판 이동
	public List<EventBoard> findAll(){
		return eventBoardRepository.findAll();
	}
}
