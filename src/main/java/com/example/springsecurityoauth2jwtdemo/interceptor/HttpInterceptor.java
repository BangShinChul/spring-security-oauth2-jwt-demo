package com.example.springsecurityoauth2jwtdemo.interceptor;

import com.example.springsecurityoauth2jwtdemo.util.LogUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    /**
     * Google OAuth2 인증을 통해 인증이 완료된 유저가 있을 경우, email 정보를 리턴해주는 메서드
     * @return email
     * */
    private String getRequestedUserEmail() {
        String email = "noname";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) { // security에서 인증이 되었을 때,
            Object principal = authentication.getPrincipal();
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            email = oAuth2User.getAttributes().get("email").toString();
        }
        return email;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LogUtil.writeAuditLog("preHandle", getRequestedUserEmail(), request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // Client에 응답을 보내기 전에 작업을 수행하는 곳
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // method 요청 및 Client에게 응답 완료 후 작업을 수행하는 곳
    }
}
