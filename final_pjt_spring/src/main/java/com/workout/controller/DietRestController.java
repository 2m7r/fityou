package com.workout.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

import com.workout.model.dto.Diet;
import com.workout.model.service.DietService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-diet")
@Tag(name = "식단일기 관리 API", description = "식단일기 관리 기능을 제공합니다.")
public class DietRestController {

	private final DietService dietService;
	
	public DietRestController(DietService dietService) {
		this.dietService = dietService;
	}
	
	// 특정 유저의 식단일기 목록 조회
	@GetMapping("/feed/{userId}")
	@Operation(summary = "특정 유저의 식단일기 목록 조회", description = "특정 유저에 대한 모든 식단일기 목록을 조회합니다.")
	public ResponseEntity<List<Diet>> getDietsByUserId(@PathVariable Long userId){
		List<Diet> diets = dietService.getDietsByUserId(userId);
		return ResponseEntity.ok(diets);
	}
	
	// 팔로우한 유저들의 식단일기 목록 조회
	@GetMapping("/follow/{userId}")
	@Operation(summary = "팔로우한 유저들의 식단일기 목록 조회", description = "팔로우한 유저에 대한 모든 식단일기 목록을 조회합니다.")
	public ResponseEntity<List<Diet>> getFollowingDietsByUserId(@PathVariable Long userId){
		List<Diet> diets = dietService.getFollowingDietsByUserId(userId);
		return ResponseEntity.ok(diets);
	}
	
	
	// 식단일기 상세조회
	@GetMapping("/{dietId}")
	@Operation(summary = "식단일기 상세 조회", description = "특정 식단일기의 상세 정보를 조회합니다.")
	public ResponseEntity<?> getDietById(@PathVariable Long dietId){
		Diet diet = dietService.getDietById(dietId);
		if(diet != null) {
			return ResponseEntity.ok(diet);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 식단일기를 찾을 수 없습니다.");
	}
	
	// 식단일기 등록
	@PostMapping("/create")
	@Operation(summary = "식단일기 등록", description = "새로운 식단일기를 등록합니다.")
	public ResponseEntity<?> registDiet(@RequestBody Diet diet){
		int result = dietService.registDiet(diet);
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body("식단일기 등록 성공");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("식단일기 등록 실패");
	}
	
	// 식단일기 수정
	@PutMapping("/{dietId}")
	@Operation(summary = "식단일기 수정", description = "기존 식단일기를 수정합니다.")
	public ResponseEntity<?> modifyDietWithImages(@PathVariable Long dietId, 
	                                               @RequestParam(required = false) MultipartFile breakfastImage,
	                                               @RequestParam(required = false) MultipartFile lunchImage,
	                                               @RequestParam(required = false) MultipartFile dinnerImage,
	                                               @RequestBody Diet diet) {

	    // 파일 업로드 처리
	    String breakfastImagePath = null;
	    String lunchImagePath = null;
	    String dinnerImagePath = null;

	    try {
	        if (breakfastImage != null) {
	            breakfastImagePath = uploadImage(breakfastImage);
	        }
	        if (lunchImage != null) {
	            lunchImagePath = uploadImage(lunchImage);
	        }
	        if (dinnerImage != null) {
	            dinnerImagePath = uploadImage(dinnerImage);
	        }
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
	    }

	    // 식단일기 객체에 경로 세팅
	    if (breakfastImagePath != null) diet.setBreakfastImagePath(breakfastImagePath);
	    if (lunchImagePath != null) diet.setLunchImagePath(lunchImagePath);
	    if (dinnerImagePath != null) diet.setDinnerImagePath(dinnerImagePath);

	    // 기존 식단일기 수정
	    diet.setDietId(dietId);
	    int result = dietService.modifyDiet(diet);
	    if (result > 0) {
	        return ResponseEntity.ok("식단일기 수정 성공");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("식단일기 수정 실패");
	}

	private String uploadImage(MultipartFile image) throws IOException {
	    // 이미지 파일 저장 디렉토리
	    String uploadDir = "uploads/diet_images/";

	    // 파일명 처리 (중복 방지)
	    String originalFilename = image.getOriginalFilename();
	    String fileName = StringUtils.cleanPath(originalFilename);
	    Path targetPath = Paths.get(uploadDir + fileName);

	    // 파일 저장
	    Files.copy(image.getInputStream(), targetPath);
	    
	    // 업로드한 이미지의 파일 경로를 반환
	    return targetPath.toString();
	}
	
	// 식단일기 삭제
	@DeleteMapping("/{dietId}")
	@Operation(summary = "식단일기 삭제", description = "특정 식단일기를 삭제합니다.")
	public ResponseEntity<?> removeDiet(@PathVariable Long dietId){
		int result = dietService.removeDiet(dietId);
		if (result > 0) {
            return ResponseEntity.ok("식단일기 삭제 성공");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("식단일기 삭제 실패");
	}
	
}
