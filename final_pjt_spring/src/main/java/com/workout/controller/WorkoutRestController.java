package com.workout.controller;

import java.util.ArrayList;
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

	@PostMapping("/create/{userId}")
	@Operation(summary = "운동일기 등록", description = "새로운 운동일기를 등록합니다.")
	public ResponseEntity<?> registWorkout(
	        @PathVariable long userId,
	        @RequestBody WorkoutRequest workoutRequest
	) {

	    // 운동 객체 생성 및 설정
	    Workout workout = new Workout();
	    workout.setUserId(userId);
	    workout.setDescription(workoutRequest.getDescription());
	    workout.setName(workoutRequest.getName());
	    workout.setRecordDate(workoutRequest.getRecordDate());

	    // 운동 데이터를 기반으로 운동 루틴 생성
	    List<WorkoutExercise> exercises = new ArrayList<>();
	    for (int i = 0; i < workoutRequest.getExerciseNames().size(); i++) {
	        WorkoutExercise exercise = new WorkoutExercise();
	        exercise.setExerciseName(workoutRequest.getExerciseNames().get(i));
	        exercise.setWeight(workoutRequest.getWeights().get(i));
	        exercise.setReps(workoutRequest.getReps().get(i));
	        exercise.setSets(workoutRequest.getSets().get(i));
	        exercises.add(exercise);
	    }
	    workout.setExercises(exercises);
	    
	    System.out.println("운동일기 등록 = " + workout);

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
	
	public static class WorkoutRequest {
	    private String description;
	    private String name;
	    private String recordDate;
	    private List<String> exerciseNames;
	    private List<Integer> weights;
	    private List<Integer> reps;
	    private List<Integer> sets;
	    
	    
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getRecordDate() {
			return recordDate;
		}
		public void setRecordDate(String recordDate) {
			this.recordDate = recordDate;
		}
		public List<String> getExerciseNames() {
			return exerciseNames;
		}
		public void setExerciseNames(List<String> exerciseNames) {
			this.exerciseNames = exerciseNames;
		}
		public List<Integer> getWeights() {
			return weights;
		}
		public void setWeights(List<Integer> weights) {
			this.weights = weights;
		}
		public List<Integer> getReps() {
			return reps;
		}
		public void setReps(List<Integer> reps) {
			this.reps = reps;
		}
		public List<Integer> getSets() {
			return sets;
		}
		public void setSets(List<Integer> sets) {
			this.sets = sets;
		}
		@Override
		public String toString() {
			return "WorkoutRequest [description=" + description + ", name=" + name + ", recordDate=" + recordDate
					+ ", exerciseNames=" + exerciseNames + ", weights=" + weights + ", reps=" + reps + ", sets=" + sets
					+ "]";
		}
	}
}
