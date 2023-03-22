package com.exposition.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.FileDto;
import com.exposition.dto.ReservationDto;
import com.exposition.dto.TourBoardDto;
import com.exposition.entity.Files;
import com.exposition.entity.Reservation;
import com.exposition.entity.TourBoard;
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
	public List<ReservationDto> getAttendCom(ReservationDto reservationDto){
		return reservationRepository.getAttendCom(reservationDto);
	}
	
	//업체등록할때 첨부했던 사진 불러오기
	@Transactional(readOnly=true)
	public ReservationDto getReservationList(List<ReservationDto> reservationDto) {
		System.out.println(reservationDto);
		System.out.println("확인1");
		ReservationDto reservationDto1 = null;
		for(int i=0; i<reservationDto.size(); i++) {
			List<Files> fileList = fileRepository.findByReservationId(reservationDto.get(i).getId());
			System.out.println(fileList);
			System.out.println("확인2");
			List<FileDto> fileDtoList = new ArrayList<>();
			for(Files file : fileList) {
				FileDto fileDto = FileDto.of(file);
				fileDtoList.add(fileDto);
				System.out.println(fileDtoList);
				System.out.println("확인3");
			}
			Reservation reservation = reservationRepository.findById(reservationDto.get(i).getId()).orElseThrow(EntityNotFoundException::new);
			reservationDto1 = ReservationDto.of(reservation);
			reservationDto1.setFileDtoList(fileDtoList);
			
		}
		return reservationDto1;
	}
}
