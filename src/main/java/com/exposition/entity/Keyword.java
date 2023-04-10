package com.exposition.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.exposition.dto.TourBoardDto;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="keywordBoard")
@Data
public class Keyword {

	@Id
	@Column(name="keyword_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// 제목
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;
	
	// 내용
	@Column(length = 2000)
	private String content;
	
	@OneToMany(mappedBy="keyword", cascade=CascadeType.ALL)
	@ToString.Exclude
	private List<Files> fileList;
	
	public static Keyword createTourBoard(TourBoardDto tourBoardDto) {
		Keyword keyword = new Keyword();
		keyword.setContent(tourBoardDto.getContent());
		keyword.setTitle(tourBoardDto.getTitle());
		return keyword;
	}
}
