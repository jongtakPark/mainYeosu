package com.exposition.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.modelmapper.ModelMapper;

import com.exposition.constant.Role;
import com.exposition.entity.Company;

import lombok.Data;

@Data
public class CompanyModifyFormDto {

	private String com;
	
	private String name;
	
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}", message = "8~16자 영문 대 소문자, 숫자를 사용하세요.")
	private String password;
    
	private String confirmPassword;
	
	private String approval;
	private String location;
	private String startDay;
	private String endDay;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static CompanyModifyFormDto of(Company company) {
		return modelMapper.map(company, CompanyModifyFormDto.class);
	}
}
