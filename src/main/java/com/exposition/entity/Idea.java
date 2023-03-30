package com.exposition.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.exposition.dto.FreeBoardDto;
import com.exposition.dto.IdeaDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="idea")
@RequiredArgsConstructor
public class Idea extends BaseEntity{

	// 글번호
	@Id
	@Column(name="idea_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// 제목
	@NotEmpty(message = "제목을 적어주세요.")
	private String title;

	// 내용
	@Column(length = 2000)
	private String content; 
	

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	@OneToMany(mappedBy="idea", cascade=CascadeType.ALL)
	@ToString.Exclude
	private List<Files> files;


	public static Idea createIdea(FreeBoardDto freeBoardDto) {
		Idea idea = new Idea();
		idea.setTitle(freeBoardDto.getTitle());
		idea.setContent(freeBoardDto.getContent());
		idea.setId(freeBoardDto.getId());
		return idea;
	}
}
