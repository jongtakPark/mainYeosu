package com.exposition.service;

import java.io.FileOutputStream;
import java.util.UUID;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.entity.File;
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
	
	public void saveFile(File file, MultipartFile files) throws Exception {
		String oriImg = files.getOriginalFilename();
		String img = "";
		String savePath = "";
		
		if(!StringUtils.isEmpty(oriImg)) {
			img = uploadFile(itemImgLocation, oriImg, files.getBytes());
			savePath = "/images/board/" + img;
		}
		
		file.updateFile(img, oriImg, savePath);
		fileRepository.save(file);
	}
}
