package com.exposition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.FileDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.EventBoard;
import com.exposition.entity.Files;
import com.exposition.entity.TourBoard;
import com.exposition.repository.BoardRepository;
import com.exposition.repository.EventBoardRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.TourBoardRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
@Service
@Transactional
@RequiredArgsConstructor
public class TourBoardService {
	@Value("${spring.cloud.gcp.storage.credentials.bucket}")
	private String bucketName;
	
	@Value("${spring.cloud.gcp.storage.credentials.project.id}")
	private String projectid;
	
	private final Storage storage;
	private final TourBoardRepository tourBoardRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;

	//주변 관광지 게시판 리스트 출력(페이징)
	public Page<BoardMainDto> getBoardMainPage(TourBoardDto tourBoardDto, Pageable pageable){
		return tourBoardRepository.getBoardMainPage(tourBoardDto, pageable);
	}
	
	
	//주변 관광지 글 저장
	public TourBoard saveTour(List<MultipartFile> files, TourBoardDto tourBoardDto) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		tourBoardRepository.save(tourBoard);
		
		for(int i = 0; i < files.size(); i++) {
			Files file = saveCloud(files.get(i));
			file.setTourBoard(tourBoard);
			if(i==0) {
				file.setThumbnail("Y");
			}else {
				file.setThumbnail("N");
			}
			fileRepository.save(file);
		}
		return tourBoard;
	}
	
	//주변 관광지 상세 페이지 창
	@Transactional(readOnly=true)
	public TourBoardDto getTourBoardDetail(Long tourBoardId) {
		List<Files> fileList = fileRepository.findByTourBoardId(tourBoardId);
		List<FileDto> fileDtoList = new ArrayList<>();
		for(Files file : fileList) {
			FileDto fileDto = FileDto.of(file);
			fileDto.setSavePath("https://storage.googleapis.com/yeosu11/"+file.getSavePath());
			fileDtoList.add(fileDto);
		}
		
		TourBoard tourBoard = tourBoardRepository.findById(tourBoardId).orElseThrow(EntityNotFoundException::new);
		TourBoardDto tourBoardDto = TourBoardDto.of(tourBoard);
		tourBoardDto.setFileDtoList(fileDtoList);
		return tourBoardDto;
	}
	
	//주변관광지 수정 글 등록
	public Long updateTourBoard(TourBoardDto tourBoardDto, List<MultipartFile> files) throws Exception {
		TourBoard tourBoard = tourBoardDto.createTourBoard();
		Long tourId = findById(tourBoard.getId()).getId();
		List<Files> list = fileService.findByTourBoardId(tourId);
		for(int i = 0 ; i < list.size(); i++) {
			deleteCloud(list.get(i));
			fileRepository.deleteByTourBoard(tourBoard);
		}
		for(int i = 0; i < files.size(); i++) {
			Files file = saveCloud(files.get(i));
			file.setTourBoard(tourBoard);
			fileRepository.save(file);
		}
		tourBoardRepository.save(tourBoard);
		return tourBoardDto.getId();
	}
	
	
	//주변 관광지 글 삭제(나중에 첨부파일이 있는 게시판은 이걸 이용해서 삭제하면 됨)
	public void deleteBoard(Long id) throws Exception{
		Long tourBoardId = findById(id).getId();
		List<Files> list = fileService.findByTourBoardId(tourBoardId);
		if(list!=null) {
			for(int i = 0; i < list.size(); i++) {
				fileRepository.delete(list.get(i));
				deleteCloud(list.get(i));
			}
		}
		tourBoardRepository.deleteById(id);
	}
	
	//첨부파일만 삭제하기(게시글 수정시에 사용될것)
	public void deleteFile(Long id) {
		Long tourBoardId = findById(id).getId();
		List<Files> list = fileService.findByTourBoardId(tourBoardId);
		if(list!=null) {
			for(int i=0; i<list.size(); i++) {
				fileService.deleteFile(list.get(i).getId());
				fileService.deleteComFile("C:/images/" + list.get(i).getImg());
			}
		}
	}
	
	//주변 관광지 글 찾아오기(첨부파일을 먼저 삭제 후 게시글을 지우기 위해)
	public TourBoard findById(Long id){
		return tourBoardRepository.findById(id).get();
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
	
	// 구글 클라우드 삭제
	public void deleteCloud(Files files) throws Exception{
//		Blob blob = storage.get(bucketName, files.getSavePath());
//		Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());
		storage.delete(bucketName, files.getSavePath());
	}
}
