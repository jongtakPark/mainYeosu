package com.exposition.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.Volunteer;
import com.exposition.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class VolunteerService {
	
	private final VolunteerRepository volunteerRepository;
	
	
	//게시판 글 작성
		public Volunteer saveVolunteer(Volunteer volunteer) {
			return volunteerRepository.save(volunteer);
		}
		
		//게시판 리스트 출력(페이징)
		public List volunteerList(){
			return volunteerRepository.findAll();
		}
		
	
		//게시판 상세보기 출력
		public Optional<Volunteer> findVolunteer(Long id) {
			return volunteerRepository.findById(id);

		}
		//게시글 수정하기
		public Volunteer updateVolunteer(Long id) {
			return volunteerRepository.findById(id).get();

		
		}

		

		
}
