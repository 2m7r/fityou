package com.workout.model.dto;

public class Workout {
	private String exerciseText;
    private String exerciseImage;

    // 생성자, getters, setters
    public Workout(String exerciseText, String exerciseImage) {
        this.exerciseText = exerciseText;
        this.exerciseImage = exerciseImage;
    }

	public String getExerciseText() {
		return exerciseText;
	}

	public void setExerciseText(String exerciseText) {
		this.exerciseText = exerciseText;
	}

	public String getExerciseImage() {
		return exerciseImage;
	}

	public void setExerciseImage(String exerciseImage) {
		this.exerciseImage = exerciseImage;
	}
    
}
