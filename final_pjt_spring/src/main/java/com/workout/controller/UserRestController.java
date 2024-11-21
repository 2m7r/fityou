package com.workout.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workout.jwt.JwtUtil;
import com.workout.model.dto.User;
import com.workout.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-user")
@Tag(name = "사용자 관리 API", description = "회원가입, 로그인, 로그아웃 기능을 제공합니다.")
public class UserRestController {

	private final UserService us;
	private final JwtUtil jwtUtil;
	
		public UserRestController(UserService us, JwtUtil jwtUtil) {
		this.us = us;
		this.jwtUtil = jwtUtil;
	}

	// 사용자 회원가입
	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "새로운 사용자를 회원가입합니다.")
	public ResponseEntity<?> signup(@RequestBody User user,
			@RequestParam(required = false) MultipartFile profileImage) {
		
		// 파일 업로드 처리
		String profileImagePath = null;

		try {
			if (profileImage != null) {
				profileImagePath = uploadImage(profileImage);
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
		}

		// 식단일기 객체에 경로 세팅
		if (profileImagePath != null)
			user.setProfile(profileImagePath);

		int result = us.registUser(user);
		System.out.println("들어옴");
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("유저 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 등록 실패");
	}

	private String uploadImage(MultipartFile image) throws IOException {
		// 이미지 파일 저장 디렉토리
		String uploadDir = "uploads/profile_images/";

		// 파일명 처리 (중복 방지)
		String originalFilename = image.getOriginalFilename();
		String fileName = StringUtils.cleanPath(originalFilename);
		Path targetPath = Paths.get(uploadDir + fileName);

		// 파일 저장
		Files.copy(image.getInputStream(), targetPath);

		// 업로드한 이미지의 파일 경로를 반환
		return targetPath.toString();
	}

	// 로그인
	@PostMapping("/login")
	@Operation(summary = "로그인", description = "사용자가 로그인하여 세션에 사용자 정보를 저장합니다.")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
	    Map<String, Object> result = new HashMap<>();
	    HttpStatus status;

	    User loginUser = us.login(user.getUsername(), user.getPassword());
	    if (loginUser != null) {
	        result.put("message", "로그인 성공");
	        result.put("access-token", jwtUtil.createToken(loginUser.getName())); // 토큰 생성
	        status = HttpStatus.OK; // 200 상태 코드
	        System.out.println("result : " + result);
	    } else {
	        result.put("message", "로그인 실패: 잘못된 자격 증명");
	        status = HttpStatus.UNAUTHORIZED; // 401 상태 코드
	    }
	    return new ResponseEntity<>(result, status);
	}

	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "사용자의 세션 토큰을 클라이언트에서 삭제합니다.")
	public ResponseEntity<String> logout() {
	    // 서버에서는 아무 작업도 하지 않고, 클라이언트에서만 처리
	    return ResponseEntity.ok("로그아웃 성공");
	}
	
	// 수정
	
}
