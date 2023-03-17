package com.exposition.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;

@Data
public class EventMemberDto {

	private Long id;
	private String mid;
	private String email;
	private String eventCount;
	
	@QueryProjection
	public EventMemberDto(Long id, String mid, String email, String eventCount) {
		this.id = id;
		this.email = email;
		this.mid = mid;
		this.eventCount = eventCount;
	}
}
