package com.workout.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.workout.model.dto.Workout;
import com.workout.model.dto.WorkoutExercise;

public interface WorkoutDao {

	// 운동일기 등록
	int insertWorkout(Workout workout);

	// 운동-운동일기 매핑 등록
	int insertWorkoutExercise(WorkoutExercise workoutExercise);

	// 운동일기 기본 정보 조회
	Workout selectWorkoutById(long workoutId);

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

	// 특정 유저의 운동일기 목록 조회
	List<Workout> selectWorkoutsById(long userId);

	// 팔로우한 유저들의 운동일기 목록 조회
	List<Workout> selectFolloingWordoutsByUserId(long userId);

	Workout selectWorkoutByDate(String date, long userId);

}
