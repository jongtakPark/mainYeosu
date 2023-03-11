package com.exposition.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.exposition.entity.Member;

import lombok.Data;

@Data
public class MemberFormDto {
	
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Length(max=8, message="아이디는 8자 이하로 입력해주세요.")
    private String mid;
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
    
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}", message = "8~16자 영문 대 소문자, 숫자를 사용하세요.")
	private String password;
    
	private String confirmPassword;
    
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;
    
    private String confirmEmail;
    
    private String tel;
    
    public static MemberFormDto createMemberDto(Member mem) {
		MemberFormDto memDto = new MemberFormDto();
		memDto.mid = mem.getMid();
		memDto.name = mem.getName();
		memDto.email = mem.getEmail();
		memDto.password = mem.getPassword();
		memDto.tel = mem.getTel();
		return memDto;
	}

}
