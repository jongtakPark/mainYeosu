package com.exposition.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.exposition.constant.Role;
import com.exposition.dto.FileDto;
import com.exposition.dto.MemberFormDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name="files")
@RequiredArgsConstructor
public class File extends BaseEntity {

	@Id
	@Column(name="file_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//추가 파일
	private String img;

	//현재 파일
	private String oriImg;
	
	//썸네일
	private String thumbnail;
	
	//저장위치
	private String savePath;
	
	@ManyToOne
	@JoinColumn(name="tourboard_id")
	private TourBoard tourboard;
	
	@ManyToOne
	@JoinColumn(name="freeboard_id")
	private FreeBoard freeBoard;
	
	public static File createFile(FileDto fileDto) {
		File file = new File();
		file.setId(fileDto.getId());
		file.setImg(fileDto.getImg());
		file.setOriImg(fileDto.getOriImg());
		file.setThumbnail(fileDto.getThumbnail());
		file.setSavePath(fileDto.getSavePath());
		return file;
	}
	
}
