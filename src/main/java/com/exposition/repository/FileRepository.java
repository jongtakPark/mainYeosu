package com.exposition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.dto.ReservationDto;
import com.exposition.entity.Files;

public interface FileRepository extends JpaRepository<Files, Long>{

	List<Files> findByTourboardId(Long tourBoradId);
	
	List<Files> findByReservationId(Long reservationId);
	
	List<Files> findByReviewId(Long reviewId);
	
}
