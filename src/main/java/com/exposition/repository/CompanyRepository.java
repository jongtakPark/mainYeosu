package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	Company findByCom(String string);
	boolean existsByCom(String com);
	Company findByEmail(String email);


}
