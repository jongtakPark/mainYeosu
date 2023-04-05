package com.exposition.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exposition.constant.Role;
import com.exposition.dto.EventMemberDto;
import com.exposition.dto.MemberFormDto;
import com.exposition.dto.MemberModifyFormDto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="member")
@Data
@ToString
@DynamicInsert
public class Member {
	
	@Id
	@Column(name="member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	@NotNull
	private String mid;
	
	@NotNull
	private String password;
	
	@Transient
    private String confirmPassword;
	
	@NotNull
	private String name;
	
	@Column(unique=true)
	private String email;
	
	private String tel;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@ColumnDefault("'N'")
	private String survey;  //설문조사 참여 Y/N
	
	@ColumnDefault("'N'")
	private String eventCount;  //이벤트 당첨 Y/N
	
	@ColumnDefault("'W'")
	private String approval; //자원봉사 봉사 지원 신청 Y/W/N

	private Long eventBoardId;
	
	@OneToMany(mappedBy="member", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private List<Idea> idea;
	
	@OneToMany(mappedBy="member", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private List<Review> review;
	
	@OneToMany(mappedBy="member", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private List<Volunteer> volunteer;
	
	//스프링시큐리티 설정 클래스에(SecurityConfig.java) 등록한 BCryptPasswordEncoder Bean으로 파라미터로 넘겨서 비밀번호를 암호화
	public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setMid(memberFormDto.getMid());
		member.setName(memberFormDto.getName());
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		member.setEmail(memberFormDto.getEmail());
		member.setTel(memberFormDto.getTel());
		member.setRole(Role.ADMIN);
		return member;
	}
	
	public static Member modifyMember(MemberModifyFormDto memberModifyFormDto, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setMid(memberModifyFormDto.getMid());
		member.setRole(memberModifyFormDto.getRole());
		return member;
	}
	
	public static Member EventMember(EventMemberDto eventMemberDto) {
		Member member = new Member();
		member.setId(eventMemberDto.getId());
		member.setMid(eventMemberDto.getMid());
		member.setEmail(eventMemberDto.getEmail());
		member.setEventCount(eventMemberDto.getEventCount());
		return member;
	}
}
