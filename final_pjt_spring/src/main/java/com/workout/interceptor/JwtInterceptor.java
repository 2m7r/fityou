package com.workout.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.workout.jwt.JwtUtil;
import com.workout.model.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	private final String HEADER_AUTH = "access-token";

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
    private TokenBlacklistService tokenBlacklistService;

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getMethod().equalsIgnoreCase("OPTIONS"))
            return true;

        String token = request.getHeader(HEADER_AUTH);
        if (token != null) {
            // 블랙리스트에 포함된 토큰인지 확인
            if (tokenBlacklistService.isBlacklisted(token)) {
                throw new IllegalAccessException("로그아웃된 토큰입니다.");
            }
            jwtUtil.validate(token); // 토큰 유효성 검증
            return true;
        }

		throw new Exception("유효하지 않은 접근입니다.");
	}

}
