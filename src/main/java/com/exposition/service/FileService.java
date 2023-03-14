package com.exposition.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.entity.Files;
import com.exposition.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	private final FileRepository fileRepository;
	//주변관광지 글 등록	
//	public File saveFile(File file) {
//		return fileRepository.save(file);
//	}
	//게시글에 첨부파일 된 이름 변경, 읽기
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
		UUID uuid = UUID.randomUUID(); 
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension; 
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); 
		fos.write(fileData); 
		fos.close();
			return savedFileName; 
		}
	
	//첨부파일 테이블에 등록
	public void saveFile(Files file, MultipartFile files) throws Exception {
		String oriImg = files.getOriginalFilename();
		String img = "";
		String savePath = "";
		
		if(!StringUtils.isEmpty(oriImg)) {
			img = uploadFile(itemImgLocation, oriImg, files.getBytes());
			savePath = "/image/images/" + img;
		}
		
		file.updateFile(img, oriImg, savePath);
		fileRepository.save(file);
	}
	
	//게시글 수정시 첨부파일 변경
	public void updateFile(Long FileId, MultipartFile files) throws Exception {
		if(!files.isEmpty()) {
			Files saveFile = fileRepository.findById(FileId).orElseThrow(EntityNotFoundException::new);
			
			if(!StringUtils.isEmpty(saveFile.getImg())) {
				deleteComFile(itemImgLocation+"/"+saveFile.getImg());
			}
			
			String oriImg = files.getOriginalFilename();
			String img = uploadFile(itemImgLocation, oriImg, files.getBytes());
			String savePath = "/image/images/" + img;
			saveFile.updateFile(img, oriImg, savePath);
		}
	}
	
	
	//게시글 id로 첨부파일 찾기
	public List<Files> findByTourBoardId(Long tourBoardId) {
		return fileRepository.findByTourboardId(tourBoardId);
	}
	
	//첨부파일 삭제하기
	public void deleteFile(Long id) {
		fileRepository.deleteById(id);
	}
	
	//저장소에서 사진 삭제
	public void deleteComFile(String savePath) {
		File deleteFile = new File(savePath);
		if(deleteFile.exists()) {
			deleteFile.delete();
		} 
	}
	
}
