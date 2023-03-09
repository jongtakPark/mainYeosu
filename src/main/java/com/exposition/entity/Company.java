package com.exposition.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.exposition.constant.Role;
import com.exposition.dto.CompanyFormDto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="company")
@Data
public class Company {
	@Id
	@Column(name="company_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	@NotNull
	private String com;
	
	@NotNull
	private String passwoad;
	@Transient
    private String confirmPassword;
	
	@NotNull
	private String name;
	@Column(unique=true)
	
	private String email;
	
	private String tel;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<FreeBoard> freeBoardList = new ArrayList<>();
	
	//스프링시큐리티 설정 클래스에(SecurityConfig.java) 등록한 BCryptPasswordEncoder Bean으로 파라미터로 넘겨서 비밀번호를 암호화
	public static Company createCompany(CompanyFormDto companyFormDto, PasswordEncoder passwordEncoder) {
		Company company = new Company();
		company.setCom(companyFormDto.getCom());
		company.setName(companyFormDto.getName());
		String password = passwordEncoder.encode(companyFormDto.getPassword());
		company.setPasswoad(password);
		String comfirmPw = passwordEncoder.encode(companyFormDto.getConfirmPassword());
		company.setConfirmPassword(comfirmPw);
		company.setEmail(companyFormDto.getEmail());
		company.setTel(companyFormDto.getTel());
		company.setRole(Role.COMPANY);
		return company;
	}
	

}
