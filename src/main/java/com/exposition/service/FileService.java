package com.exposition.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.entity.Files;
import com.exposition.entity.Idea;
import com.exposition.entity.Keyword;
import com.exposition.entity.Review;
import com.exposition.entity.TourBoard;
import com.exposition.entity.Volunteer;
import com.exposition.repository.FileRepository;
import com.exposition.repository.KeywordRepository;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {
	@Value("${spring.cloud.gcp.storage.credentials.bucket}")
	private String bucketName;
	
	private final Storage storage;
	private final FileRepository fileRepository;
	private final KeywordRepository keywordRepository;
	
// 주변관광지 저장
	public void saveFile(List<MultipartFile> files,TourBoard tourBoard) throws Exception{
		if(!files.get(0).isEmpty()) {
			for(int i = 0; i < files.size(); i++) {
				Files file = saveCloud(files.get(i));
				file.setOriImg(files.get(i).getOriginalFilename());
				file.setTourBoard(tourBoard);
				if(i==0) {
					file.setThumbnail("Y");
				}else {
					file.setThumbnail("N");
				}
				fileRepository.save(file);
			}
		}
	}
	
	// 관람후기 저장
	public void saveFile(List<MultipartFile> files, Review review) throws Exception{
		if(!files.get(0).isEmpty()) {
			for(int i = 0; i < files.size(); i++) {
				Files file = saveCloud(files.get(i));
				file.setOriImg(files.get(i).getOriginalFilename());
				file.setReview(review);;
				if(i==0) {
					file.setThumbnail("Y");
				}else {
					file.setThumbnail("N");
				}
				fileRepository.save(file);
			}
		}
	}
	
	// 국민아이디어 저장
	public void saveFile(List<MultipartFile> files, Idea idea) throws Exception {
		if(!files.get(0).isEmpty()) {
			for(int i = 0; i < files.size(); i++) {
				Files file = saveCloud(files.get(i));
				file.setOriImg(files.get(i).getOriginalFilename());
				file.setIdea(idea);
				if(i==0) {
					file.setThumbnail("Y");
				}else {
					file.setThumbnail("N");
				}
				fileRepository.save(file);
			}
		}
	}

	// 자원봉사자 저장
	public void saveFile(List<MultipartFile> files, Volunteer volunteer) throws Exception{
		if(!files.get(0).isEmpty()) {
			for(int i = 0; i < files.size(); i++) {
				Files file = saveCloud(files.get(i));
				file.setOriImg(files.get(i).getOriginalFilename());
				file.setVolunteer(volunteer);
				if(i==0) {
					file.setThumbnail("Y");
				}else {
					file.setThumbnail("N");
				}
				fileRepository.save(file);
			}
		}		
	}
	
	// 여수섬 키워드 저장
		public void saveFile(List<MultipartFile> files, Keyword keyword) throws Exception {
			Files file = new Files();
			for(int i = 0; i < files.size(); i++) {
				file.setKeyword(keyword);
				if(i==0) {
					file.setSavePath(saveCloud(files.get(i)).getSavePath());
					fileRepository.save(file);
				}
				else {
					file.setBackSavePath(backsaveCloud(files.get(i)).getBackSavePath());
					fileRepository.save(file);
				}
					
			}
			
		}
	
	// 주변관광지 삭제
	public void deleteTourBoard(List<Files> list,TourBoard tourBoard) throws Exception {
		for(int i = 0 ; i < list.size(); i++) {
			deleteCloud(list.get(i));
			fileRepository.deleteByTourBoard(tourBoard);
		}
	}
	
	// 관람후기 삭제
	public void deleteReview(List<Files> list, Review review) throws Exception {
		for(int i = 0 ; i < list.size(); i++) {
			deleteCloud(list.get(i));
			fileRepository.deleteByReview(review);
		}
	}
	
	// 국민 아이디어 삭제
	public void deleteIdea(List<Files> list, Idea idea) throws Exception{
		for(int i = 0 ; i < list.size(); i++) {
			deleteCloud(list.get(i));
			fileRepository.deleteByIdea(idea);
		}
		
	}
	
	// 자원봉사자 삭제
	public void deleteVolunteer(List<Files> list, Volunteer volunteer) throws Exception{
		for(int i = 0 ; i < list.size(); i++) {
			deleteCloud(list.get(i));
			fileRepository.deleteByVolunteer(volunteer);
		}
		
	}
	
//	// 여우섬 키워드 삭제
	public void deleteKeyword(List<Files> files,List<Long> id) throws Exception {
		for(int i =0; i<files.size(); i++) {
			deleteAllCloud(files.get(i));
			keywordRepository.deleteById(id.get(i));
	}		
}
	
	// 파일 및 클라우드 삭제
	public void deleteFile(List<Files> list) throws Exception {
		if(list!=null) {
			for(int i = 0; i < list.size(); i++) {
				fileRepository.delete(list.get(i));
				deleteCloud(list.get(i));
		}
	}		
}

	
	//주변관광지 게시글 id로 첨부파일 찾기
	public List<Files> findByTourBoardId(Long tourBoardId) {
		return fileRepository.findByTourBoardId(tourBoardId);
	}
	
	//관람후기 게시글 id로 첨부파일 찾기
	public List<Files> findByReviewId(Long reviewId){
		return fileRepository.findByReviewId(reviewId);
	}
	
	//국민아이디어 게시글 id로 첨부파일 찾기
	public List<Files> findByIdeaId(Long ideaId){
		return fileRepository.findByIdeaId(ideaId);
	}
	
	//자원봉사 게시글 id로 첨부파일 찾기
	public List<Files> findByVolunteerId(Long volunteerId){
		return fileRepository.findByVolunteerId(volunteerId);
	}
	
	// 키워드게시판 id로 첨부파일 찾기
	public List<Files> findByKeyworBoardId(Long KeywordBoardId) {
		return fileRepository.findByKeywordId(KeywordBoardId);
	}

	
	// 구글클라우드 저장
	public Files saveCloud(MultipartFile multipartFile) throws Exception{
		Files file = new Files();
		String ext = multipartFile.getContentType();
		String uuid = UUID.randomUUID().toString();
		file.setSavePath(uuid);
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName,uuid)
													.setContentType(ext)
													.build(),
													multipartFile.getInputStream());
		return file;
	}
	
	// 구글 클라우드 뒷면 저장
	public Files backsaveCloud(MultipartFile multipartFile) throws Exception{
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
	public void deleteCloud(Files files) throws Exception{
//		Blob blob = storage.get(bucketName, files.getSavePath());
//		Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());
		storage.delete(bucketName, files.getSavePath());
	}
	
	// 구글 클라우드 여수섬키워드 삭제
	public void deleteAllCloud(Files files) throws Exception{
		storage.delete(bucketName, files.getSavePath());
		storage.delete(bucketName, files.getBackSavePath());
	}
	
}
