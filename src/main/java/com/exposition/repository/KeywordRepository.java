package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long>, BoardRepositoryCustom{

}
