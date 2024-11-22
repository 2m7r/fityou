package com.workout.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workout.exception.InvalidAnswerException;
import com.workout.exception.UserNotFoundException;
import com.workout.jwt.JwtUtil;
import com.workout.model.dto.FindIdRequest;
import com.workout.model.dto.SelectPreferredExercisesRequest;
import com.workout.model.dto.User;
import com.workout.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
		List<String> preferredExcercises = us.getprefereedExcercise(user.getUsername());
		
		if (loginUser != null) {
			loginUser.setPreferredExercises(preferredExcercises);
			result.put("message", "로그인 성공");
			result.put("access-token", jwtUtil.createToken(loginUser.getName())); // 토큰 생성
			result.put("loginUser", loginUser);
			status = HttpStatus.OK; // 200 상태 코드
			System.out.println("result : " + result);
		} else {
			result.put("message", "로그인 실패: 잘못된 자격 증명");
			status = HttpStatus.UNAUTHORIZED; // 401 상태 코드
		}
		return new ResponseEntity<>(result, status);
	}

	// 로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "사용자의 세션 토큰을 클라이언트에서 삭제합니다.")
	public ResponseEntity<String> logout() {
		// 서버에서는 아무 작업도 하지 않고, 클라이언트에서만 처리
		return ResponseEntity.ok("로그아웃 성공");
	}

	// 아이디 찾기
	@PostMapping("/find-id")
	public ResponseEntity<String> findId(@RequestBody FindIdRequest request) {
		try {
			String username = us.findUsername(request);
			return ResponseEntity.ok("귀하의 아이디는: " + username);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
		} catch (InvalidAnswerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("답변이 틀렸습니다.");
		}
	}

	// 비밀번호 찾기
	@PostMapping("/find-password")
	public ResponseEntity<String> findPW(@RequestBody FindIdRequest request) {
		try {
			String password = us.findPassword(request);
			return ResponseEntity.ok(password);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
		} catch (InvalidAnswerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("답변이 틀렸습니다.");
		}
	}

	// 이메일을 기준으로 보안 질문을 반환하는 API
	@GetMapping("/get-security-question")
	public ResponseEntity<Map<String, String>> getSecurityQuestion(@RequestParam String email) {
		// 이메일로 사용자를 찾음
		User user = us.findByEmail(email);

		if (user != null) {
			// 사용자가 존재하면 보안 질문 반환
			Map<String, String> response = new HashMap<>();
			response.put("securityQuestion", user.getSecurityQuestion());
			return ResponseEntity.ok(response);
		} else {
			// 사용자가 없으면 404 Not Found 반환
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	// 사용자 정보 수정
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> modifyUser(@PathVariable long userId, @RequestBody User user){
    	user.setUserId(userId);
    	
    	int result = us.modifyUser(user);
    	
    	if (result > 0) {
	        return ResponseEntity.ok("유저 수정 성공");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 수정 실패");
    }
    
    // 선호 운동 선택
    @PostMapping("/preferred-exercises")
    public ResponseEntity<?> selectPreferredExercises(@RequestBody SelectPreferredExercisesRequest request){
    	try {
            us.savePreferredExercises(request.getLoginUser().getUserId(), request.getExercises());
            return ResponseEntity.ok("선호 운동이 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("선호 운동 저장에 실패했습니다.");
        }
    }

}
