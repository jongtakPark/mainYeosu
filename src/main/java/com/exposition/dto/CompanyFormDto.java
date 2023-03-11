package com.exposition.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CompanyFormDto {
	
	@NotBlank(message = "사업자번호(아이디)는 필수 입력값입니다.")
	@Length(min=0 ,max=10, message="사업자번호(아이디)는 10자리 숫자 입니다.")
    private String com;
	
	@NotBlank(message = "기업명은 필수 입력 값입니다.")
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

}
