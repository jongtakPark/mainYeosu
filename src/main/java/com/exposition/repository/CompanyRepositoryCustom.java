package com.exposition.repository;

import java.util.List;

import com.exposition.dto.CompanyFormDto;
import com.exposition.entity.Company;

public interface CompanyRepositoryCustom {

	List<Company> getApprovalCom(Company company);
}
