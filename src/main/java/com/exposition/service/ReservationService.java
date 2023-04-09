package com.exposition.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.FileDto;
import com.exposition.dto.ReservationDto;
import com.exposition.entity.Files;
import com.exposition.entity.Reservation;
import com.exposition.repository.FileRepository;
import com.exposition.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final FileService fileService;
	private final FileRepository fileRepository;
	
	//업체 등록
	public Reservation saveReservation(List<MultipartFile> files, ReservationDto reservationDto) throws Exception {
		Reservation reservation = reservationDto.createTourBoard();
		reservationRepository.save(reservation);
		for(int i=0; i<files.size(); i++) {
			Files file = new Files();
			file.setReservation(reservation);
			fileService.saveFile(file, files.get(i));
		}
		return reservation;
	}
	
	//선택한 장소에 예약된 날짜 보여주기
	public List<ReservationDto> getSameLocationReservation(ReservationDto reservationDto){
		return reservationRepository.getSameLocationReservation(reservationDto);
	}
	
	//업체등록목록 페이지에 등록한 업체 보여주기
	public Page<ReservationDto> getAttendCom(ReservationDto reservationDto, Pageable pageable){
		return reservationRepository.getAttendCom(reservationDto, pageable);
	}
	
	//업체등록할때 첨부했던 사진 불러오기
	@Transactional(readOnly=true)
	public List<ReservationDto> getReservationList(List<ReservationDto> reservationDto) {
		for(int i=0; i<reservationDto.size(); i++) {
			List<Files> fileList = fileRepository.findByReservationId(reservationDto.get(i).getId());
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDtoList.add(fileDto);
			}
			reservationDto.get(i).setFileDtoList(fileDtoList);
		}
		return reservationDto;
	}
}
