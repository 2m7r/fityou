package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.Workout;

public interface WorkoutService {

	// 운동일기 등록
	int registWorkout(Workout workout);

	// 운동일기 상세조회
	Workout getWorkoutById(long workoutId);
	
	// 운동일기 수정
	int modifyWorkout(Workout workout);
	
	// 운동일기 삭제
	int removeWorkout(long workoutId);

	// 특정 유저의 운동일기 목록 조회
	List<Workout> getWorkoutsById(long userId);

	// 팔로우한 유저들의 운동일기 목록 조회
	List<Workout> getFollowingWorkoutsByUserId(long userId);

	Workout getWorkoutByDate(String date, long userId);

}
