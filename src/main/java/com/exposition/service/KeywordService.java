package com.exposition.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.BoardMainDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.Files;
import com.exposition.entity.Keyword;
import com.exposition.repository.KeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

	private final KeywordRepository keywordRepository;
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
		for(int i=0; i<files.size(); i++) {
			file.setKeyword(keyword);
			if(i==0) 
				fileService.saveFile(file, files.get(i));
			else
				fileService.saveKeyword(file, files.get(i));
			
		}
		return keyword;
	}
	
	public void delete(List<Long> id) {
		List<Files> files = new ArrayList<>();
		for(int i =0; i<id.size();i++) {
			files.addAll(fileService.findByKeyworBoardId(id.get(i)));
		}
		for(int i =0; i<files.size(); i++) {
			keywordRepository.deleteById(id.get(i));
			fileService.deleteComFile("C:/images/"+files.get(i).getBackSavePath().substring(16));
			fileService.deleteComFile("C:/images/"+files.get(i).getSavePath().substring(16));
		}
		
	}
}
