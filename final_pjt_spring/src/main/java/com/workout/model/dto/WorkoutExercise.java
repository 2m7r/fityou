package com.workout.model.dto;

public class WorkoutExercise {
	private long id;
    private long workoutId; // 외래키
    private long exerciseId; // 운동 종류
    private int weight; // 무게
    private int reps;   // 횟수
    private int sets;   // 세트 수
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWorkoutId() {
		return workoutId;
	}
	public void setWorkoutId(long workoutId) {
		this.workoutId = workoutId;
	}
	public long getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getReps() {
		return reps;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public int getSets() {
		return sets;
	}
	public void setSets(int sets) {
		this.sets = sets;
	}
    
}
