package com.exposition.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.exposition.dto.QReservationDto;
import com.exposition.dto.ReservationDto;
import com.exposition.entity.QCompany;
import com.exposition.entity.QReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public ReservationRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public List<ReservationDto> getSameLocationReservation(ReservationDto reservationDto){
		QReservation reservation = QReservation.reservation;
		QCompany company = QCompany.company;
		
		List<ReservationDto> results = queryFactory.select(new QReservationDto(reservation.startDay, reservation.endDay, company.approval))
				.from(reservation).where(reservation.location.eq(reservationDto.getLocation())).fetch();
		
		return results;
	}
}
