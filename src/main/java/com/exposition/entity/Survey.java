package com.exposition.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.exposition.dto.FreeBoardDto;

import lombok.Data;

@Entity
@Table(name="Survey")
@Data
public class Survey{

	// 글번호
	@Id
	@Column(name="freeBoard_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long first;
	private Long second;
	private Long third;
	private Long fourth;


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
		

	public static Survey createSurvey(FreeBoardDto freeBoardDto) {
		Survey survey = new Survey();
		survey.setId(freeBoardDto.getId());
		return survey;
	}
}
