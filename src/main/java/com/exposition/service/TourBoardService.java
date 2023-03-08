package com.exposition.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.TourBoard;
import com.exposition.repository.FileRepository;
import com.exposition.repository.TourBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TourBoardService {

	private final TourBoardRepository tourBoardRepository;
	
	//주변관광지 글 저장
	public TourBoard saveTour(TourBoard tourBoard) {
		return tourBoardRepository.save(tourBoard);
	}
}
