package com.exposition.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.Idea;
import com.exposition.repository.IdeaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class IdeaService {

	private final IdeaRepository ideaRepository;
	
	//게시판 글 작성
	public Idea saveBoard(Idea idea) {
		return ideaRepository.save(idea);
	}
	
	//게시판 리스트 출력(페이징)
	public Page<Idea> boardList(Pageable pageable){
		return ideaRepository.findAll(pageable);
	}
	//게시판 상세보기 출력
	public Optional<Idea> findBoard(Long id) {
		return ideaRepository.findById(id);
	}
	//게시글 수정하기
	public Idea updateBoard(Long id) {
		return ideaRepository.findById(id).get();
	}
	//게시글 삭제
	public void deleteBoard(Long id) {
		ideaRepository.deleteById(id);
	}
	
}
