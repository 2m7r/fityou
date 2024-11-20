package com.workout.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workout.model.dto.Follow;
import com.workout.model.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/follow")
@Tag(name = "팔로우 관리 API", description = "회원의 팔로우 관리 기능을 제공합니다.")
public class FollowRestController {

	private final FollowService followService;

	public FollowRestController(FollowService followService) {
		this.followService = followService;
	}

	// 팔로우 등록
	@PostMapping
	@Operation(summary = "팔로우 등록", description = "사용자가 다른 사용자를 팔로우합니다.")
	public ResponseEntity<?> addFollow(@RequestBody Follow follow) {
		int result = followService.addFollow(follow);
		if (result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("팔로우 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("팔로우 등록 실패");
	}

	// 팔로잉 삭제
	@DeleteMapping
	@Operation(summary = "팔로잉 삭제", description = "사용자가 팔로잉을 취소합니다.")
	public ResponseEntity<?> removeFollow(@RequestBody Follow follow) {
		int result = followService.removeFollow(follow);
		if (result > 0) {
			return ResponseEntity.ok("팔로잉 삭제 성공");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("팔로잉 삭제 실패");
	}

	// 특정 사용자의 팔로잉 목록 조회
	@GetMapping("/following/{followerId}")
	@Operation(summary = "팔로잉 목록 조회", description = "특정 사용자가 팔로우하는 모든 사용자를 조회합니다.")
	public ResponseEntity<List<Long>> getFollowingByUserId(@PathVariable Long followerId) {
		List<Long> followingList = followService.getFollowingByUserId(followerId);
		return ResponseEntity.ok(followingList);
	}

	// 특정 사용자의 팔로워 목록 조회
	@GetMapping("/followers/{followingId}")
	@Operation(summary = "팔로워 목록 조회", description = "특정 사용자를 팔로우하는 모든 사용자를 조회합니다.")
	public ResponseEntity<List<Long>> getFollowersByUserId(@PathVariable Long followingId) {
		List<Long> followersList = followService.getFollowersByUserId(followingId);
		return ResponseEntity.ok(followersList);
	}

}
