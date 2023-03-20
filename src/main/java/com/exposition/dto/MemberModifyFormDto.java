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
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String email;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public static MemberModifyFormDto of(Member member) {
		return modelMapper.map(member, MemberModifyFormDto.class);
	}
}
