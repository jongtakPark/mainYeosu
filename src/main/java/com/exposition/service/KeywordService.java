package com.exposition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.Files;
import com.exposition.entity.Keyword;
import com.exposition.repository.FileRepository;
import com.exposition.repository.KeywordRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {
	@Value("${spring.cloud.gcp.storage.credentials.bucket}")
	private String bucketName;
	
	@Value("${spring.cloud.gcp.storage.credentials.project.id}")
	private String projectid;
	
	private final Storage storage;
	private final KeywordRepository keywordRepository;
	private final FileRepository fileRepository;
	private final FileService fileService;
	
	//여수섬 키워드 리스트 출력(페이징)
	public Page<BoardMainDto> getKeywordMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		return keywordRepository.getKeywordMainPage(tourBoardDto, pageable);
	}
	
	//여수섬 키워드 저장
	public Keyword saveTour(List<MultipartFile> files, TourBoardDto tourBoardDto) throws Exception {
		Keyword keyword = tourBoardDto.createKeyword();
		keywordRepository.save(keyword);
		
		Files file = new Files();
		for(int i = 0; i < files.size(); i++) {
			file.setKeyword(keyword);
			if(i==0) {
				file.setSavePath(saveFileAndBoard(files.get(i)).getSavePath());
				fileRepository.save(file);
			}
			else {
				file.setBackSavePath(backsaveFileAndBoard(files.get(i)).getBackSavePath());
				fileRepository.save(file);
			}
				
		}
		return keyword;
	}
	
	// 여수섬 키워드 삭제
	public void delete(List<Long> id) throws Exception{
		List<Files> files = new ArrayList<>();
		for(int i =0; i<id.size();i++) {
			files.addAll(fileService.findByKeyworBoardId(id.get(i)));
		}
		for(int i =0; i<files.size(); i++) {
				deleteFileAndBoard(files.get(i));
			keywordRepository.deleteById(id.get(i));
		}		
	}
	
	
	// 구글클라우드 저장
	public Files saveFileAndBoard(MultipartFile multipartFile) throws Exception{
		Files file = new Files();
		String ext = multipartFile.getContentType();
		String uuid = UUID.randomUUID().toString()+"_"+ multipartFile.getOriginalFilename();
		file.setSavePath(uuid);
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName,uuid)
													.setContentType(ext)
													.build(),
													multipartFile.getInputStream());
		return file;
	}
	
	public Files backsaveFileAndBoard(MultipartFile multipartFile) throws Exception{
		Files file = new Files();
		String ext = multipartFile.getContentType();
		String uuid = UUID.randomUUID().toString()+"_"+ multipartFile.getOriginalFilename();
		file.setBackSavePath(uuid);
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName,uuid)
													.setContentType(ext)
													.build(),
													multipartFile.getInputStream());
		return file;
	}
	
	// 구글 클라우드 삭제
	public void deleteFileAndBoard(Files files) throws Exception{
		storage.delete(bucketName, files.getSavePath());
		storage.delete(bucketName, files.getBackSavePath());
	}

}
