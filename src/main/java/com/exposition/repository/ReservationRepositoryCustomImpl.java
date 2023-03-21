package com.exposition.repository;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;

public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public ReservationRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	
}
