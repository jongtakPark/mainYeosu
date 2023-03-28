package com.exposition.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.exposition.dto.FileDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name="files")
@RequiredArgsConstructor
public class Files extends BaseEntity {

	@Id
	@Column(name="files_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//처음 파일 이름을 바꾼 이름
	private String img;

	//처음 파일 이름
	private String oriImg;
	
	//썸네일
	private String thumbnail;
	
	//저장위치
	private String savePath;
	
	//뒷면 이미지 저장위치
	private String backSavePath;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tourboard_id")
	private TourBoard tourboard;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="freeboard_id")
	private FreeBoard freeBoard;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="keyword_id")
	private Keyword keyword;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="reservation_id")
	private Reservation reservation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="review_id")
	private Review review;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="idea_id")
	private Idea idea;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="volunteer_id")
	private Volunteer volunteer;
	
	public static Files createFile(FileDto fileDto) {
		Files file = new Files();
		file.setId(fileDto.getId());
		file.setImg(fileDto.getImg());
		file.setOriImg(fileDto.getOriImg());
		file.setThumbnail(fileDto.getThumbnail());
		file.setSavePath(fileDto.getSavePath());
		return file;
	}
	
	public void updateFile(String img, String oriImg, String savePath) {
		this.img = img;
		this.oriImg = oriImg;
		this.savePath = savePath;
	}
	
	public void updateBackFile(String img, String oriImg, String backSavePath) {
		this.img = img;
		this.oriImg = oriImg;
		this.backSavePath = backSavePath;
	}


	
}
