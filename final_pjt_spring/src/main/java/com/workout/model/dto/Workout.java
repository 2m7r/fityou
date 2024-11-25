package com.workout.model.dto;

import java.util.List;

public class Workout {
	private Long workoutId;
    private Long userId;
    private String name; // 유저 이름
    private String description;
    private List<WorkoutExercise> exercises;
    private String recordDate;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getWorkoutId() {
		return workoutId;
	}
	public void setWorkoutId(Long workoutId) {
		this.workoutId = workoutId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<WorkoutExercise> getExercises() {
		return exercises;
	}
	public void setExercises(List<WorkoutExercise> exercises) {
		this.exercises = exercises;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	@Override
	public String toString() {
		return "Workout [workoutId=" + workoutId + ", userId=" + userId + ", name=" + name + ", description="
				+ description + ", exercises=" + exercises + ", recordDate=" + recordDate + "]";
	}
	
}
