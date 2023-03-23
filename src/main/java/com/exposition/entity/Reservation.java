package com.exposition.entity;

import javax.persistence.CascadeType;
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
import lombok.ToString;

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
	
	private String content;
	
	private String startDay;
	
	private String endDay;
	
	
	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "company_id")
	@ToString.Exclude
	private Company company;
	
	
}
