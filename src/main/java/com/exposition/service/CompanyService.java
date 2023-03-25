package com.exposition.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.dto.CompanyFormDto;
import com.exposition.entity.Company;
import com.exposition.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService implements UserDetailsService {
	
	private final CompanyRepository companyRepository;
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

}
