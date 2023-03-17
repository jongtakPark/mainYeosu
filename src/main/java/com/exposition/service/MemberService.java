package com.exposition.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exposition.entity.Member;
import com.exposition.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;
	//회원가입
	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}
	//회원 중복검사
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByMid(member.getMid());

		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다");
		}	
	}

	//ajax를 이용한 중복검사
	public boolean checkMidDuplicate(String mid) {
		return memberRepository.existsByMid(mid);
	}
	//로그인
	@Override
	public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException{
		
		Member member = memberRepository.findByMid(mid);
		if(member==null) {
			throw new UsernameNotFoundException(mid);
		}
		
		return User.builder().username(member.getMid()).password(member.getPassword()).roles(member.getRole().toString()).build();
	}
	
	//id로 유저 찾기
	public Optional<Member> findById(Long id) {
		return memberRepository.findById(id);
	}
	
	//유저 회원 변경
	public Member updateMember(Member member) {
		return memberRepository.save(member);
	}

	//이름과 이메일로으로 유저 찾기
	public Member findByNameAndEmail(String name, String email) {
		Member member = memberRepository.findByNameAndEmail(name, email);
		if(member!=null) {
			return member;
		} else {
			throw new NullPointerException("가입된 회원이 아닙니다");
		}
	}
	
	//아이디와 이메일로 유저 찾기
	public Member findByMidAndEmail(String mid, String email) {
		Member member = memberRepository.findByMidAndEmail(mid, email);
		if(member!=null) {
			return member;
		} else {
			throw new NullPointerException("가입된 회원이 아닙니다");
		}
	}
	
	//아이디로 회원 찾기
	public Member findByMid(String mid) {
		Member member = memberRepository.findByMid(mid);
		if(member!=null) {
			return member;
		} else {
			throw new NullPointerException("가입된 회원이 아닙니다");
		}
	}
	
	//이벤트 당첨자 찾기
	public List<Member> findByEventBoardId(Long id) {
		List<Member> member = memberRepository.findByEventBoardId(id);
		if(member!=null) {
			return member;
		} else {
			throw new NullPointerException("이벤트에 당첨된 회원이 없습니다.");
		}
	}
	
}
