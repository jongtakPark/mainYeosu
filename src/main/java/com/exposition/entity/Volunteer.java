package com.exposition.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.exposition.dto.FreeBoardDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name="volunteer")
@RequiredArgsConstructor
public class Volunteer extends BaseEntity{

	// 글번호
	@Id
	@Column(name="volunteer_id")
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
	
	@OneToMany(mappedBy="volunteer", cascade=CascadeType.ALL)
	@ToString.Exclude
	private List<Files> files;
	
	public static Volunteer createVolunteer(FreeBoardDto freeBoardDto) {
		Volunteer volunteer = new Volunteer();
		volunteer.setTitle(freeBoardDto.getTitle());
		volunteer.setContent(freeBoardDto.getContent());
		volunteer.setId(freeBoardDto.getId());
		return volunteer;
	}
}
