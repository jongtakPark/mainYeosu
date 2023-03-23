package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.exposition.dto.QReservationDto;
import com.exposition.dto.ReservationDto;
import com.exposition.entity.QCompany;
import com.exposition.entity.QFiles;
import com.exposition.entity.QReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public ReservationRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	//예약된 기간 달력에 보여주기
	@Override
	public List<ReservationDto> getSameLocationReservation(ReservationDto reservationDto){
		QReservation reservation = QReservation.reservation;
		QCompany company = QCompany.company;
		
		List<ReservationDto> results = queryFactory.select(new QReservationDto(reservation.startDay, reservation.endDay, company.approval))
				.from(reservation).where(reservation.location.eq(reservationDto.getLocation())).fetch();
		
		return results;
	}
	
	//참가업체 목록 페이지에 예약완료 된 기업 보여주기
	@Override
	public List<ReservationDto> getAttendCom(ReservationDto reservationDto){
		QReservation reservation = QReservation.reservation;
		QCompany company = QCompany.company;
		
		List<ReservationDto> results = queryFactory
				.select(new QReservationDto(company.name, reservation.id ,reservation.startDay, reservation.endDay, reservation.location, reservation.content))
				.from(reservation).join(reservation.company, company)
				.where(company.approval.eq("예약완료")).orderBy(reservation.startDay.asc()).fetch();
		
		return results;
	}
}
