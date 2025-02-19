package com.workout.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.workout.model.service.FollowService;
import com.workout.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-user")
@Tag(name = "사용자 관리 API", description = "회원가입, 로그인, 로그아웃 기능을 제공합니다.")
public class UserRestController {

	private final UserService us;
	private final FollowService fs;
	private final JwtUtil jwtUtil;

	public UserRestController(UserService us, FollowService fs, JwtUtil jwtUtil) {
		this.us = us;
		this.fs = fs;
		this.jwtUtil = jwtUtil;
	}

	// 사용자 회원가입
	@PostMapping("/signup")
	@Operation(summary = "회원가입", description = "새로운 사용자를 회원가입합니다.")
	public ResponseEntity<?> signup(@RequestBody User user) {
		int result = us.registUser(user);
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("유저 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 등록 실패");
	}
	
	// 유저 수정
	@PutMapping("/update/{userId}")
	public ResponseEntity<?> modifyUser(@PathVariable long userId, @RequestParam("name") String name,
			@RequestParam("username") String username,
			@RequestParam("email") String email, 
			@RequestParam("phoneNum") String phoneNum,
			@RequestParam("gender") String gender, 
			@RequestParam("isPrivateAccount") boolean isPrivateAccount,
			@RequestParam(required = false) MultipartFile profileImage,
			@RequestParam("userDescription") String userDescription) {

		User user = new User();
		user.setUserId(userId);
		user.setUsername(username);
		user.setName(name);
		user.setEmail(email);
		user.setPhoneNum(phoneNum);
		user.setGender(gender);
		user.setPrivateAccount(isPrivateAccount);
		user.setUserDescription(userDescription);
		System.out.println(user);

		// 프로필 이미지 처리
		String profileImagePath = null;
		if (profileImage != null && !profileImage.isEmpty()) {
			try {
				profileImagePath = uploadImage(profileImage);
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
			}
		}

		if (profileImagePath != null) {
			user.setProfileImage(profileImagePath);
		}else {
			User preuser = us.selectUserByUsername(username);
			user.setProfileImage(preuser.getProfileImage());
		}

		int result = us.modifyUser(user);

		if (result > 0) {
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 수정 실패");
	}

	private String uploadImage(MultipartFile image) throws IOException {
		// 이미지 파일 저장 디렉토리
		String uploadDir = "uploads/profile_images/";
		File dir = new File(uploadDir);
		if (!dir.exists()) {
			dir.mkdirs(); // 디렉토리가 없으면 생성
		}

		// 파일명 처리 (중복 방지)
		String originalFilename = image.getOriginalFilename();
		String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(originalFilename);
		Path targetPath = Paths.get(uploadDir + fileName);

		try {
			// 파일 저장
			Files.copy(image.getInputStream(), targetPath);
		} catch (IOException e) {
			System.out.println("이미지 업로드 실패: " + e.getMessage());
			throw e; // 예외 다시 던지기
		}

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

	// 선호 운동 선택
	@PostMapping("/preferred-exercises")
	public ResponseEntity<?> selectPreferredExercises(@RequestBody SelectPreferredExercisesRequest request) {
		try {
			us.savePreferredExercises(request.getLoginUser().getUserId(), request.getExercises());
			return ResponseEntity.ok("선호 운동이 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("선호 운동 저장에 실패했습니다.");
		}
	}
	
	// 유저 검색 API (유저 ID에 포함된 문자열을 기반으로 검색)
    @GetMapping("/search/{username}")
    public ResponseEntity<List<User>> searchUsers(@PathVariable String username) {
    	List<User> list = us.searchUsers(username);
    	System.out.println(list);
    	return ResponseEntity.ok(list);
    }
    
//    // 유저 상세 조회
//    @GetMapping("/search")
//    public ResponseEntity<List<User>> searchUser(@RequestParam Long[] userIds){
//    	System.out.println("들어왔냐?");
//    	List<User> list = us.searchUser(userIds);
//    	System.out.println("유저 이름 불러와봐,,, " + list);
//    	return ResponseEntity.ok(list);
//    }
	
    // 유저 탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> joinOut(@PathVariable long userId){
    	int result = us.deleteUser(userId);
    	if(result > 0) {
    		return ResponseEntity.status(HttpStatus.OK).body("유저 탈퇴 성공");
    	}
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 탈퇴 실패");
    }
    
    @GetMapping("/recommend")
    public ResponseEntity<List<User>> recommendUsers(@RequestParam Long userId) {
        System.out.println("추천 들어옴");
        
        // 현재 유저 정보 가져오기
        User currUser = us.selectUserById(userId);
        System.out.println(currUser);
        
        // 현재 유저의 선호 운동 목록 가져오기
        List<String> preferredExercises = currUser.getPreferredExercises();
        if (preferredExercises == null) {
            preferredExercises = new ArrayList<>();
        }

        // 모든 유저 목록 가져오기
        List<User> allUsers = us.selectAllUsers();

        // 현재 유저가 팔로우한 유저 목록을 가져오기 (팔로우 관계 확인)
        List<Long> followedUserIds = fs.getFollowingByUserId(userId);  // 팔로우 서비스에서 팔로우한 유저 아이디 목록 가져오기

        List<UserRecommendation> recommendations = new ArrayList<>();

        // 3. 선호 운동이 겹치는 유저 찾기
        for (User user : allUsers) {
            // 현재 유저는 제외하고 팔로우한 유저도 제외
            if (!user.getUserId().equals(userId) && !followedUserIds.contains(user.getUserId())) {
                long commonExercises = preferredExercises.stream()
                    .filter(exercise -> user.getPreferredExercises().contains(exercise))
                    .count();

                if (commonExercises > 0) {
                    recommendations.add(new UserRecommendation(user, commonExercises));
                }
            }
        }

        // 4. 선호 운동이 겹치는 수를 기준으로 내림차순 정렬
        recommendations.sort((r1, r2) -> Long.compare(r2.getCommonExercises(), r1.getCommonExercises()));

        // 5. 상위 5명만 반환
        List<User> recommendedUsers = recommendations.stream()
            .limit(5)
            .map(UserRecommendation::getUser)
            .collect(Collectors.toList());

        return ResponseEntity.ok(recommendedUsers);
    }
    
    public static class UserRecommendation {
    	private User user;
        private long commonExercises;
        
        public UserRecommendation(User user, long commonExercises) {
            this.user = user;
            this.commonExercises = commonExercises;
        }

        public User getUser() {
            return user;
        }

        public long getCommonExercises() {
            return commonExercises;
        }
    }
}
