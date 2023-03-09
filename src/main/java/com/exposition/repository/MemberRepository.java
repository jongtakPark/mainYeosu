package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Member findByMid(String mid);
	boolean existsByMid(String mid);
	Member findByEmail(String email);
}
