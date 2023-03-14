package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.TourBoard;

public interface TourBoardRepository extends JpaRepository<TourBoard, Long>, BoardRepositoryCustom{
	
}
