package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}
