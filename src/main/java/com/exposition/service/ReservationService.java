package com.exposition.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.exposition.dto.ReservationDto;
import com.exposition.entity.Files;
import com.exposition.entity.Reservation;
import com.exposition.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final FileService fileService;
	
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
}
