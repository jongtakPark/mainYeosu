package com.exposition.entity;

import java.util.ArrayList;
import java.util.List;

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

import com.exposition.dto.TourBoardDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name="tourboard")
@RequiredArgsConstructor
public class TourBoard {
	
	@Id
	@Column(name="tourboard_id")
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
	
//	@OneToMany(fetch = FetchType.LAZY)
//	@ToString.Exclude
//	private List<File> fileList = new ArrayList<>();
	
	public static TourBoard createTourBoard(TourBoardDto tourBoardDto) {
		TourBoard tourBoard = new TourBoard();
		tourBoard.setContent(tourBoardDto.getContent());
		tourBoard.setTitle(tourBoardDto.getTitle());
		return tourBoard;
	}
	
	public void updateTourBoard(TourBoardDto tourBoardDto) {
		this.title = tourBoardDto.getTitle();
		this.content = tourBoardDto.getContent();
	}
}
