package com.workout.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.workout.model.dto.Challenge;
import com.workout.model.dto.User;
import com.workout.model.service.ChallengeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-challenge")
@Tag(name = "챌린지 관리 API", description = "챌린지 CRUD 기능을 제공합니다.")
public class ChallengeRestController {
	
	private final ChallengeService challengeService;
	
	public ChallengeRestController(ChallengeService challengeService) {
		this.challengeService = challengeService;
	}
	
	// 챌린지 등록
	@PostMapping("/create")
	@Operation(summary = "챌린지 등록", description = "새로운 챌린지를 등록합니다.")
	public ResponseEntity<?> registChallenge(@RequestBody Challenge challenge){
		int result = challengeService.registChallenge(challenge);
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("챌린지 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("챌린지 등록 실패");
	}
	
	
	// 챌린지 상세 조회
	@GetMapping("/{challengeId}")
	@Operation(summary = "챌린지 상세 조회", description = "특정 챌린지의 상세 정보를 조회합니다.")
	public ResponseEntity<?> getChallengeById(@PathVariable Long challengeId){
		Challenge challenge = challengeService.getChallengeById(challengeId);
		if(challenge != null) {
			return ResponseEntity.ok(challenge);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 챌린지를 찾을 수 없습니다.");
	}
	
	
	// 챌린지 수정
	@PutMapping("/{challengeId}")
	@Operation(summary = "챌린지 수정", description = "기존 챌린지를 수정합니다.")
	public ResponseEntity<?> modifyChallenge(@PathVariable Long challengeId, @RequestBody Challenge challenge){
		challenge.setChallengeId(challengeId);
		int result = challengeService.modifyChallenge(challenge);
		if (result > 0) {
	        return ResponseEntity.ok("챌린지 수정 성공");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("챌린지 수정 실패");
	}
	
	
	// 챌린지 떠나기
	@PostMapping("/leave")
	@Operation(summary = "챌린지 떠나기", description = "특정 챌린지를 탈퇴합니다.")
	public ResponseEntity<?> removeChallenge(@RequestBody ChallengeRequest challengeRequest){
		System.out.println(challengeRequest);
		long challengeId = challengeRequest.getChallengeId();
		long userId = challengeRequest.getUserId();
		int result = challengeService.leaveChallenge(challengeId, userId);
		if (result > 0) {
            return ResponseEntity.ok("챌린지 떠나기 성공");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("챌린지 떠나기 실패");
	}
	
	
	// 챌린지 참여하기
	@PostMapping("/join/{challengeId}")
	@Operation(summary = "챌린지 참여하기", description = "특정 챌린지에 참여합니다.")
	public ResponseEntity<?> takePartChallenge(@PathVariable long challengeId, @RequestParam long userId){
		int result = challengeService.intoChallenge(challengeId, userId);
		if (result > 0) {
            return ResponseEntity.ok("챌린지 참여 성공");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("챌린지 참여 실패");
	}
	
	// 챌린지 참여자 목록 조회
	@GetMapping("/people/{challengeId}")
	@Operation(summary = "특정 챌린지 참여자 목록 조회", description = "특정 챌린지의 참여자 목록을 조회합니다.")
	public ResponseEntity<?> selectChallengeUsers(@PathVariable Long challengeId){
		List<User> users = challengeService.getChallengeUsers(challengeId);
		if(users != null && !users.isEmpty()) {
			return ResponseEntity.ok(users);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 챌린지의 유저를 찾을 수 없습니다.");
	}
	
	// 내가 참여한 챌린지 목록 조회하기
	@GetMapping("/challenges")
	@Operation(summary = "내가 참여한 챌린지 목록 조회", description = "특정 유저의 참여 챌린지 목록을 조회합니다.")
	public ResponseEntity<List<Challenge>> selectChallenges(@RequestParam long userId){
		List<Challenge> list = challengeService.getChallenges(userId);
		return ResponseEntity.ok(list);
	}
	
	
	// 전체 챌린지 목록 조회
	@GetMapping
	@Operation(summary = "전체 챌린지 목록 조회", description = "모든 챌린지 목록을 조회합니다.")
	public ResponseEntity<List<Challenge>> getChallengeList(){
		List<Challenge> challenges = challengeService.getAllChallenges();
		return ResponseEntity.ok(challenges);
	}

	public static class ChallengeRequest {
		private long challengeId;
		private long userId;
		public long getChallengeId() {
			return challengeId;
		}
		public void setChallengeId(long challengeId) {
			this.challengeId = challengeId;
		}
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		@Override
		public String toString() {
			return "ChallengeRequest [challengeId=" + challengeId + ", userId=" + userId + "]";
		}
	}
	
}
