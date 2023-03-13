package com.exposition.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.File;

public interface FileRepository extends JpaRepository<File, Long>{

	List<File> findByTourboard(Long tourBoradId);
}
