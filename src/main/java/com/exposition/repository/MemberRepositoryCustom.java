package com.exposition.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.MemberFormDto;
import com.exposition.entity.Member;

public interface MemberRepositoryCustom {

	Page<MemberFormDto> getAppVolunteer(MemberFormDto memberFormDto, Pageable pageable);
}
