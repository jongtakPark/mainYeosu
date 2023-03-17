package com.exposition.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.exposition.dto.CompanyFormDto;

public interface CompanyRepositoryCustom {

	Page<CompanyFormDto> getApprovalCom(CompanyFormDto companyFormDto, Pageable pageable);
}
