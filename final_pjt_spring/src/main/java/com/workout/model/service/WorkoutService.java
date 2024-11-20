package com.workout.model.service;

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

}
