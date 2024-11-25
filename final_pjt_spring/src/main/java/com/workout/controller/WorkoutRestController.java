package com.workout.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.workout.model.dto.Workout;
import com.workout.model.dto.WorkoutExercise;
import com.workout.model.service.WorkoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/api-workout")
@Tag(name = "운동일기 관리 API", description = "운동일기 CRUD 기능을 제공합니다.")
public class WorkoutRestController {

	private final WorkoutService workoutService;

	public WorkoutRestController(WorkoutService workoutService) {
		this.workoutService = workoutService;
	}

	// 운동일기 등록
	@PostMapping("/create/{userId}")
	@Operation(summary = "운동일기 등록", description = "새로운 운동일기를 등록합니다.")
	public ResponseEntity<?> registWorkout(
			@PathVariable long userId,
			@RequestParam(required = false) MultipartFile workoutImage,
			@RequestParam String description,
			@RequestParam List<WorkoutExercise> exercises
			) {
		
		String workoutImagePath = null;
		
		try {
            // 파일 업로드 처리
            if (workoutImage != null) {
            	workoutImagePath = uploadImage(workoutImage);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
		
		Workout workout = new Workout();
		workout.setUserId(userId);
		workout.setDescription(description);
		workout.setExercises(exercises);
		if(workoutImagePath != null) workout.setWorkoutImage(workoutImagePath);
		System.out.println(workout);
		try {
			int result = workoutService.registWorkout(workout);
			if (result > 0) {
				return ResponseEntity.status(HttpStatus.CREATED).body("운동일기 등록 성공");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("운동일기 등록 실패");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("운동일기 등록 중 오류 발생");
		}
	}

	private String uploadImage(MultipartFile image) throws IOException {
		// 이미지 파일 저장 디렉토리
		String uploadDir = "uploads/workout_images/";

		File dir = new File(uploadDir);
		if (!dir.exists()) {
		    dir.mkdirs();  // 디렉토리가 없으면 생성
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
	        throw e;  // 예외 다시 던지기
	    }

		// 업로드한 이미지의 파일 경로를 반환
		return targetPath.toString();
	}

	// 운동일기 상세조회
	@GetMapping("/{workoutId}")
	@Operation(summary = "운동일기 상세 조회", description = "특정 운동일기의 상세 정보를 조회합니다.")
	public ResponseEntity<?> getWorkoutById(@PathVariable long workoutId) {
		Workout workout = workoutService.getWorkoutById(workoutId);

		if (workout != null) {
			return ResponseEntity.ok(workout); // 운동일기 상세정보 반환
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("운동일기를 찾을 수 없습니다.");
		}
	}

	// 운동일기 수정
	@PutMapping("/{workoutId}")
	@Operation(summary = "운동일기 수정", description = "기존 운동일기를 수정합니다.")
	public ResponseEntity<?> modifyWorkout(@PathVariable long workoutId, @RequestBody Workout workout) {
		// workoutId를 workout 객체에 설정
		workout.setWorkoutId(workoutId);

		int result = workoutService.modifyWorkout(workout);
		if (result > 0) {
			return ResponseEntity.ok("운동일기 수정 성공");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("운동일기를 찾을 수 없거나 수정에 실패했습니다.");
		}
	}

	// 운동일기 삭제
	@DeleteMapping("/{workoutId}")
	@Operation(summary = "운동일기 삭제", description = "특정 운동일기를 삭제합니다.")
	public ResponseEntity<?> removeWorkout(@PathVariable long workoutId) {
		// 서비스 호출
		int result = workoutService.removeWorkout(workoutId);
		if (result > 0) {
			return ResponseEntity.ok("운동일기 삭제 성공");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("운동일기를 찾을 수 없거나 삭제에 실패했습니다.");
		}
	}

	// 특정 유저의 운동일기 목록 조회
	@GetMapping("/feed/{userId}")
	@Operation(summary = "특정 유저의 운동일기 목록 조회", description = "특정 유저에 대한 모든 운동일기 목록을 조회합니다.")
	public ResponseEntity<List<Workout>> getDietsByUserId(@PathVariable Long userId) {
		List<Workout> workouts = workoutService.getWorkoutsById(userId);
		return ResponseEntity.ok(workouts);
	}

	// 팔로우한 유저들의 운동일기 목록 조회
	@GetMapping("/follow/{userId}")
	@Operation(summary = "팔로우한 유저들의 식단일기 목록 조회", description = "팔로우한 유저에 대한 모든 식단일기 목록을 조회합니다.")
	public ResponseEntity<List<Workout>> getFollowingDietsByUserId(@PathVariable Long userId) {
		List<Workout> workouts = workoutService.getFollowingWorkoutsByUserId(userId);
		return ResponseEntity.ok(workouts);
	}
}
