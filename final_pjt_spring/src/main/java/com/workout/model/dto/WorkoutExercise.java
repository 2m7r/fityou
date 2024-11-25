package com.workout.model.dto;

public class WorkoutExercise {
	private long id;
    private long workoutId; // 외래키
    private String exerciseName;
    private String category;
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
	public String getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	@Override
	public String toString() {
		return "WorkoutExercise [id=" + id + ", workoutId=" + workoutId + ", exerciseName=" + exerciseName
				+ ", category=" + category + ", weight=" + weight + ", reps=" + reps + ", sets=" + sets + "]";
	}
}
