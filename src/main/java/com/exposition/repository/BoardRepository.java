package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.FreeBoard;

public interface BoardRepository extends JpaRepository<FreeBoard, Long>, BoardRepositoryCustom{

}
	