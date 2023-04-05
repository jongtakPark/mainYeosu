package com.exposition.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
    //로그인 성공시 이전화면으로 이동
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    		Authentication authentication) throws IOException, ServletException {
    	clearSession(request);
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		String prevPage = (String) request.getSession().getAttribute("prevPage");
		if(prevPage != null) {
			request.getSession().removeAttribute("prevPage");
		}
		
		String uri = "/";
		
		if(savedRequest != null) {
			uri = savedRequest.getRedirectUrl();
		} else if(prevPage != null && !prevPage.equals("")) {
			if(prevPage.contains("/signup/login")) {
				uri = "/";
			} else {
				uri = prevPage;
			}
		}
		redirectStrategy.sendRedirect(request, response, uri);
		
		//로그인 성공시 아이디 저장
		HttpSession session = request.getSession();
		session.setAttribute("userId", authentication.getName());
		session.setAttribute("userAuth", String.valueOf(authentication.getAuthorities().iterator().next()));
    }
    

    //로그인 성공시 에러 세션 제거
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
