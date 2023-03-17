package com.exposition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Member findByMid(String mid);
	boolean existsByMid(String mid);
	Member findByMidAndEmail(String mid, String email);
	Member findByNameAndEmail(String name, String email);
	List<Member> findByEventBoardId(Long id);
}
