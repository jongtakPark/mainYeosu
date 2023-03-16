package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.EventBoard;

public interface EventBoardRepository extends JpaRepository<EventBoard, Long>{

}
