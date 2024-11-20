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
import org.springframework.web.bind.annotation.RestController;

import com.workout.model.dto.TrainerClient;
import com.workout.model.dto.User;
import com.workout.model.service.TrainerClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-trainer")
@Tag(name = "트레이너 회원 관리 API", description = "트레이너 회원관리 기능을 제공합니다.")
public class TrainerClientRestController {
	
	private final TrainerClientService trainerClientService;
	
	public TrainerClientRestController(TrainerClientService trainerClientService) {
		this.trainerClientService = trainerClientService;
	}
	
	// 회원으로 등록
	@PostMapping("/regist")
	@Operation(summary = "내 회원 등록", description = "새로운 회원을 등록합니다.")
	public ResponseEntity<?> registClient(@RequestBody TrainerClient trainerClient){
		int result = trainerClientService.registClient(trainerClient);
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("회원 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원 등록 실패");
	}
	
	// 회원 특이사항 수정
	@PutMapping("/{clientId}")
	@Operation(summary = "회원 특이사항 수정", description = "특정 회원의 특이사항을 수정합니다.")
	public ResponseEntity<?> modifyClient(@PathVariable long clientId, @RequestBody TrainerClient client){
		client.setClientId(clientId);
		int result = trainerClientService.modifyClient(client);
		if (result > 0) {
	        return ResponseEntity.ok("회원 수정 성공");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 수정 실패");
	}
	
	// 특정 회원 정보 조회
	@GetMapping("/client/{clientId}")
	@Operation(summary = "회원 정보 상세 조회", description = "특정 회원의 정보를 조회합니다.")
	public ResponseEntity<?> getClientById(@PathVariable long clientId){
		User user = trainerClientService.getClientById(clientId);
		if(user != null) {
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 회원을 찾을 수 없습니다.");
	}
	
	// 회원 목록 조회
	@GetMapping("/{trainerId}")
	@Operation(summary = "회원 목록 조회", description = "특정 트레이너의 회원 목록을 조회합니다.")
	public ResponseEntity<List<User>> selectClients(@PathVariable long trainerId){
		List<User> users = trainerClientService.getClients(trainerId);
			return ResponseEntity.ok(users);
	}
	
	// 회원 삭제
	@DeleteMapping("/{clientId}")
	@Operation(summary = "회원 삭제", description = "특정 회원을 삭제합니다.")
	public ResponseEntity<?> removeClient(@PathVariable long clientId){
		int result = trainerClientService.removeClient(clientId);
		if (result > 0) {
	        return ResponseEntity.ok("회원 삭제 성공");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 삭제 실패");
	}
	
	
	
	
}
