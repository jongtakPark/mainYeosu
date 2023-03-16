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
import javax.validation.constraints.NotEmpty;

import com.exposition.dto.FreeBoardDto;

import lombok.Data;

@Entity
@Table(name="Survey")
@Data
public class Survey extends BaseEntity{

	// 글번호
	@Id
	@Column(name="freeBoard_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
		
	// 제목
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;

	// 내용
	@Column(length = 2000)
	private String content; 
		

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
		

	public static Survey createSurvey(FreeBoardDto freeBoardDto) {
		Survey survey = new Survey();
		survey.setTitle(freeBoardDto.getTitle());
		survey.setContent(freeBoardDto.getContent());
		survey.setId(freeBoardDto.getId());
		return survey;
	}
}
