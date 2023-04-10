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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exposition.constant.Role;
import com.exposition.dto.CompanyFormDto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="company")
@Data
@DynamicInsert
public class Company {
	@Id
	@Column(name="company_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	@NotNull
	private String com;
	
	@NotNull
	private String password;
	@Transient
    private String confirmPassword;
	
	@NotNull
	private String name;
	@Column(unique=true)
	
	private String email;
	
	private String tel;
	
	@ColumnDefault("'예약없음'")
	private String approval; //업체등록 신청
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(mappedBy="company", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private Reservation reservation;

	@OneToMany(mappedBy="company", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private List<Idea> idea;
	
	@OneToMany(mappedBy="company", cascade=CascadeType.REMOVE)
	@ToString.Exclude
	private List<Review> review;
	
	//스프링시큐리티 설정 클래스에(SecurityConfig.java) 등록한 BCryptPasswordEncoder Bean으로 파라미터로 넘겨서 비밀번호를 암호화
	public static Company createCompany(CompanyFormDto companyFormDto, PasswordEncoder passwordEncoder) {
		Company company = new Company();
		company.setCom(companyFormDto.getCom());
		company.setName(companyFormDto.getName());
		String password = passwordEncoder.encode(companyFormDto.getPassword());
		company.setPassword(password);
		company.setEmail(companyFormDto.getEmail());
		company.setTel(companyFormDto.getTel());
		company.setApproval(companyFormDto.getApproval());
		company.setRole(Role.COMPANY);
		return company;
	}
	

}
