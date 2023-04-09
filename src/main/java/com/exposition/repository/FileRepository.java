package com.exposition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.dto.ReservationDto;
import com.exposition.entity.Files;
import com.exposition.entity.Idea;
import com.exposition.entity.Review;
import com.exposition.entity.TourBoard;
import com.exposition.entity.Volunteer;

public interface FileRepository extends JpaRepository<Files, Long>{

//	List<Files> findByTourboardId(Long tourboradId);
	List<Files> findByTourBoardId(Long tourBoardId);
	
	List<Files> findByReservationId(Long reservationId);
	
	List<Files> findByReviewId(Long reviewId);
	
	List<Files> findByIdeaId(Long ideaId);
	
	List<Files> findByVolunteerId(Long volunteerId);

	List<Files> findByKeywordId(Long keywordId);
	
	void deleteByReview(Review review);

	void deleteByIdea(Idea idea);

	void deleteByVolunteer(Volunteer volunteer);
	
	void deleteByTourBoard(TourBoard tourBoard);

//	List<Files> findByKeywordBoardId(Long keywordBoardId);
}
