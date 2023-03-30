package com.exposition.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.exposition.dto.FreeBoardDto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

@Entity
@Table(name="announcement")
@Data
public class Announcement extends BaseEntity{

	@Id
	@Column(name="announcement_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	private String title;
	
	@NotNull
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	public static Announcement createAnnouncement(FreeBoardDto freeBoardDto) {
		Announcement announcement = new Announcement();
		announcement.setTitle(freeBoardDto.getTitle());
		announcement.setContent(freeBoardDto.getContent());
		announcement.setId(freeBoardDto.getId());
		announcement.setMember(freeBoardDto.getMember());
		return announcement;
	}
}
