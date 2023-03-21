package com.exposition.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="reservation")
@Data
public class Reservation {

	// 예약번호
	@Id
	@Column(name="reservation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String location;
	
	private String startDay;
	
	private String endDay;
	
	private String content;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;
	
	
}
