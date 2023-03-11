package com.exposition.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.FreeBoard;
import com.exposition.repository.BoardRepository;


import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	
	//게시판 글 작성
	public FreeBoard saveBoard(FreeBoard freeBoard) {
		return boardRepository.save(freeBoard);
	}
	
	//게시판 리스트 출력(페이징)
	public Page<FreeBoard> boardList(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	//게시판 상세보기 출력
	public Optional<FreeBoard> findBoard(Long id) {
		return boardRepository.findById(id);
	}
	//게시글 수정하기
	public FreeBoard updateBoard(Long id) {
		return boardRepository.findById(id).get();
	}
	//게시글 삭제
	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}
	
}
