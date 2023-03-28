package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long>{

}
