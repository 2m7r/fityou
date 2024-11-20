package com.workout.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workout.model.dto.User;
import com.workout.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Tag(name = "사용자 관리 API", description = "회원가입, 로그인, 로그아웃 기능을 제공합니다.")
public class UserRestController {

	private final UserService us;

	public UserRestController(UserService us) {
		this.us = us;
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
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
		try {
			User loggedInUser = us.authenticateUser(user.getUsername(), user.getPassword());
			if (loggedInUser != null) {
				session.setAttribute("user", loggedInUser); // 세션에 사용자 정보 저장
				return ResponseEntity.ok("로그인 성공");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호가 틀렸습니다.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패: " + e.getMessage());
		}
	}

	// 로그아웃
	@GetMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그아웃하여 세션을 무효화합니다.")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return ResponseEntity.ok("로그아웃 성공");
	}

}
