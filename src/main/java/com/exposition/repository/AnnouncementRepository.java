package com.exposition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exposition.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

}
