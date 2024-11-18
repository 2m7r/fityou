package com.workout.model.dao;

import com.workout.model.dto.Workout;

public interface WorkoutDao {
	// Workout 등록
    int insertWorkout(Workout workout);

    // Workout 수정
    int updateWorkout(Workout workout);

    // Workout 삭제
    int deleteWorkout(Long workoutId);
}
