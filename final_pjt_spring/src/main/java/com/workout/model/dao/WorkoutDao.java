package com.workout.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workout.model.dto.Workout;
import com.workout.model.dto.WorkoutExercise;

public interface WorkoutDao {

	// 운동일기 등록
	int insertWorkout(Workout workout);

	// 운동일기 사진 등록
	int insertWorkoutPhoto(@Param("workoutId") long workoutId, @Param("photoUrl") String photoUrl);

	// 운동-운동일기 매핑 등록
	int insertWorkoutExercise(WorkoutExercise workoutExercise);

	// 운동일기 기본 정보 조회
	Workout selectWorkoutById(long workoutId);

	// 운동일기의 사진 경로 조회
	List<String> selectWorkoutPhotos(long workoutId);

	// 운동일기에 포함된 운동 세부정보 조회
	List<WorkoutExercise> selectWorkoutExercises(long workoutId);

	// 운동일기 업데이트
	int updateWorkout(Workout workout);

	// 운동 세부 정보 삭제
	int deleteWorkoutExercises(long workoutId);

	// 운동 세부 정보 등록
    int insertWorkoutExercises(@Param("workoutId") long workoutId, @Param("exercises") List<WorkoutExercise> exercises);

    // 운동일기 삭제
	int deleteWorkout(long workoutId);

}
