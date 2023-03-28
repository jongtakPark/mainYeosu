package com.exposition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.exposition.service.CompanyService;
import com.exposition.service.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final MemberService memberService;
	private final CompanyService companyService;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
//		http.csrf().disable(); 
		http.formLogin().loginPage("/signup/login").successHandler(authenticationSuccessHandler)
		.usernameParameter("mid")
		.passwordParameter("password")
		.failureUrl("/signup/login/error")
		.and()
		.logout()
		.logoutUrl("signup/logout")
		.logoutRequestMatcher(new AntPathRequestMatcher("/signup/logout"))
		.logoutSuccessUrl("/")
		.invalidateHttpSession(true).deleteCookies("JSESSIONID");	
		
		http.authorizeRequests()
		.mvcMatchers("/board/volunteer**","/board/volunteer**/**").hasAnyRole("VOLUNTEER","ADMIN")
		.mvcMatchers("/introduction/keywordWrite","/introduction/keywordSave","/admin/**","/news/tourwrite","/news/modify/**","/news/delete/**").hasRole("ADMIN")
		.mvcMatchers("signup/mypage").hasRole("USER")
		.mvcMatchers("signup/commypage").hasRole("COMPANY")
		.mvcMatchers("/lease/**").hasAnyRole("ADMIN","COMPANY")
		.mvcMatchers("/","/signup/**","/board/**","/introduction/**","/attend/**","/news/**","/board/review","/board/reviewView/**","/board/idea","/board/ideaView/**").permitAll() // 모든 사용자 인증없이 해당경로에 접근하도록 설정
		.anyRequest().authenticated(); // 나머지 경로들은 모두 인증을 요구하도록 설정
		
		http.exceptionHandling() // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러 등록
		.accessDeniedPage("/error_user");
	}
	
	
	//비밀번호 인코딩
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth
		.userDetailsService(memberService)
		.passwordEncoder(passwordEncoder())
		.and()
		.userDetailsService(companyService)
		.passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/javascript/**", "/images/**","/video/**" , "/error", "/docs/**", "/image/**");
	}
	
}
