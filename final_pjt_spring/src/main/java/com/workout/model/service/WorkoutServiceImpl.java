package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.model.dao.WorkoutDao;
import com.workout.model.dto.Workout;
import com.workout.model.dto.WorkoutExercise;

@Service
public class WorkoutServiceImpl implements WorkoutService {

	@Autowired
	private WorkoutDao dao;

	@Override
	@Transactional
	public int registWorkout(Workout workout) {
		// 1. workouts 테이블에 운동일기 등록
		int workoutResult = dao.insertWorkout(workout);
		if (workoutResult <= 0) {
			throw new RuntimeException("운동일기 등록 실패");
		}

		// 2. workout_exercises 테이블에 운동 세부정보 등록
		if (workout.getExercises() != null) {
			for (WorkoutExercise exercise : workout.getExercises()) {
				exercise.setWorkoutId(workout.getWorkoutId()); // 운동일기 ID 설정
				int exerciseResult = dao.insertWorkoutExercise(exercise);
				if (exerciseResult <= 0) {
					throw new RuntimeException("운동 세부정보 등록 실패");
				}
			}
		}
		return workoutResult;
	}

	@Override
	public Workout getWorkoutById(long workoutId) {
		// workouts 테이블에서 기본 정보 조회
		Workout workout = dao.selectWorkoutById(workoutId);

		if (workout != null) {
			// workout_exercises 테이블에서 운동 세부정보 조회
			List<WorkoutExercise> exercises = dao.selectWorkoutExercises(workoutId);
			workout.setExercises(exercises);
		}

		return workout; // 완성된 Workout 객체 반환
	}

	@Override
	public int modifyWorkout(Workout workout) {
		// 기존 운동일기 업데이트
		int updateResult = dao.updateWorkout(workout);

		if (updateResult > 0) {
			// 기존 운동 세부 정보 삭제 후 새로 등록
			dao.deleteWorkoutExercises(workout.getWorkoutId());
			if (workout.getExercises() != null && !workout.getExercises().isEmpty()) {
				dao.insertWorkoutExercises(workout.getWorkoutId(), workout.getExercises());
			}
		}

		return updateResult;
	}

	@Override
	@Transactional
	public int removeWorkout(long workoutId) {
		return dao.deleteWorkout(workoutId);
	}

	@Override
	public List<Workout> getWorkoutsById(long userId) {
		// 1. 운동일기 기본 정보 가져오기
		List<Workout> workouts = dao.selectWorkoutsById(userId);

		// 2. 각 운동일기마다 운동 세부 정보 추가
		for (Workout workout : workouts) {
			List<WorkoutExercise> exercises = dao.selectWorkoutExercises(workout.getWorkoutId());
			workout.setExercises(exercises); // 운동일기 객체에 운동 세부 정보 세팅
		}

		return workouts;
	}

	@Override
	public List<Workout> getFollowingWorkoutsByUserId(long userId) {
		// 1. 팔로잉 유저들의 운동일기 기본 정보 가져오기
		List<Workout> workouts = dao.selectFolloingWordoutsByUserId(userId);

		// 2. 각 운동일기마다 운동 세부 정보 추가
		for (Workout workout : workouts) {
			List<WorkoutExercise> exercises = dao.selectWorkoutExercises(workout.getWorkoutId());
			workout.setExercises(exercises); // 운동일기 객체에 운동 세부 정보 세팅
		}

		return workouts;
	}

	@Override
	public Workout getWorkoutByDate(String date, long userId) {
		// 1. 운동일기 기본 정보 가져오기
		Workout workout = dao.selectWorkoutByDate(date, userId);
		
		// 2. 각 운동일기에 운동 세부 정보 추가
		List<WorkoutExercise> exercises = dao.selectWorkoutExercises(workout.getWorkoutId());
		workout.setExercises(exercises); // 운동일기 객체에 운동 세부 정보 세팅
		
		return workout;
	}

}
