package com.exposition.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.dto.CompanyFormDto;
import com.exposition.dto.CompanyModifyFormDto;
import com.exposition.entity.Company;
import com.exposition.entity.Files;
import com.exposition.entity.Reservation;
import com.exposition.repository.CompanyRepository;
import com.exposition.repository.FileRepository;
import com.exposition.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService implements UserDetailsService {
	
	private final CompanyRepository companyRepository;
	private final ReservationRepository reservationRepository;
	private final FileRepository fileRepository;
	private final FileService fileService;
	//회원가입
	public Company saveCompany(Company company) {
		validateDuplicateCompany(company);
		return companyRepository.save(company);
	}
	//회원 중복검사
	private void validateDuplicateCompany(Company company) {
		Company findCompany = companyRepository.findByCom(company.getCom());
		if(findCompany != null) {
			throw new IllegalStateException("이미 가입된 회원입니다");
		}	
	}
	
	//기업회원 전체 조회
	public Page<CompanyFormDto> findAllComapny(Pageable pageable){
		return companyRepository.findAllCom(pageable);
	}
	
	//기업회원 갯수 조회
	public Long findAllComCount() {
		return companyRepository.findAllComCount();
	}
	
	//예약완료된 기업 갯수 조회
	public Long findSucReservationCom() {
		return companyRepository.findSucReservationCom();
	}
	
	public Company findByCom(String com) {
		return companyRepository.findByCom(com);
	}
	//ajax를 이용한 중복검사
	public boolean checkComDuplicate(String com) {
		return companyRepository.existsByCom(com);
	}
	//로그인
	@Override
	public UserDetails loadUserByUsername(String com) throws UsernameNotFoundException{
		
		Company company = companyRepository.findByCom(com);
		if(company==null) {
			throw new UsernameNotFoundException(com);
		}
		
		return User.builder().username(company.getCom()).password(company.getPassword()).roles(company.getRole().toString()).build();
		
	}
	
	//아이디와 이메일로 기업 유저 찾기
	public Company findByComAndEmail(String com, String email) {
		Company company = companyRepository.findByComAndEmail(com, email);
		if(company!=null) {
			return company;
		} else {
			throw new NullPointerException("가입된 회원이 아닙니다");
		}
	}
	
	//기업 유저 회원 변경
	public Company updateCompany(Company company) {
		return companyRepository.save(company);
	}
	
	//업체 등록 신청을 한 기업 찾기
	public Page<CompanyFormDto> findApprovalCom(CompanyFormDto companyFormDto, Pageable pageable){
		return companyRepository.getApprovalCom(companyFormDto,  pageable);
	}
	
	//업체등록 신청한 기업 승인
	public void updateApp(String com) {
		companyRepository.updateApp(com);
	}
	
	//기업 회원 탈퇴
	public void deleteCom(Company company) {
		companyRepository.delete(company);
	}

	//기업회원 마이페이지 조회
	public CompanyModifyFormDto findReservationByCom(String com) {
		Company company = findByCom(com);
		CompanyModifyFormDto companyModifyFormDto = CompanyModifyFormDto.of(company);
		try {
			Reservation reservation = reservationRepository.findByCompany(company);
			companyModifyFormDto.setReservationId(reservation.getId());
			companyModifyFormDto.setLocation(reservation.getLocation());
			companyModifyFormDto.setStartDay(reservation.getStartDay());
			companyModifyFormDto.setEndDay(reservation.getEndDay());
		} catch (Exception e) {
			return companyModifyFormDto;
		}
		return companyModifyFormDto;
	}
	
	//기업회원 예약신청 취소
	public void reservationCancle(Long id) {
		List<Files> files =  fileRepository.findByReservationId(id);
	      for(int i =0; i< files.size();i++) {
	         fileService.deleteComFile("C:/images/"+files.get(i).getImg());
	      }
		reservationRepository.deleteById(id);
	}
}
