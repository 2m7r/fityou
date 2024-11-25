package com.workout.model.dto;

import java.util.List;

public class Workout {
	private Long workoutId;
    private Long userId;
    private String description;
    private List<WorkoutExercise> exercises;
    private String workoutImage;
    
    
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
	
	
	public String getWorkoutImage() {
		return workoutImage;
	}
	public void setWorkoutImage(String workoutImage) {
		this.workoutImage = workoutImage;
	}
	
	@Override
	public String toString() {
		return "Workout [workoutId=" + workoutId + ", userId=" + userId + ", description=" + description
				+ ", exercises=" + exercises + ", workoutImage=" + workoutImage + "]";
	}
	
	
	
}
