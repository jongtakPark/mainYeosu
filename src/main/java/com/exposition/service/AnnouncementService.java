package com.exposition.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.Announcement;
import com.exposition.entity.Member;
import com.exposition.repository.AnnouncementRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementService {

	private final AnnouncementRepository announcementRepository;
	
	//공지사항 페이지로 이동
	public Page<Announcement> findAll(Pageable pageable){
		return announcementRepository.findAll(pageable);
	}
	
	//공지사항 글 저장
	public void announcementSave(Announcement announcement, Member member) {
		announcement.setMember(member);
		announcementRepository.save(announcement);
	}
	
	//Id로 글 찾기
	public Announcement findById(Long id) {
		return announcementRepository.findById(id).get();
	}
	
	//공지사항 글 수정 저장
	public void announcementUpdate(Announcement announcement, Member member) {
		announcement.setMember(member);
		announcementRepository.save(announcement);
	}
	
	//공지사항 글 삭제
	public void announcementDelete(Long id) {
		announcementRepository.deleteById(id);
	}
}
