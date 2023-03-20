package com.exposition.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.modelmapper.ModelMapper;

import com.exposition.constant.Role;
import com.exposition.entity.Member;

import lombok.Data;

@Data
public class MemberModifyFormDto {

	private String mid;
	
	private String name;
	
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}", message = "8~16자 영문 대 소문자, 숫자를 사용하세요.")
	private String password;
    
	private String confirmPassword;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String email;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static MemberModifyFormDto of(Member member) {
		return modelMapper.map(member, MemberModifyFormDto.class);
	}
}
