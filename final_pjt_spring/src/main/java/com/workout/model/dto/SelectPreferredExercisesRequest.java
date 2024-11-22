package com.workout.model.dto;

import java.util.List;

public class SelectPreferredExercisesRequest {
	private User loginUser;
    private List<String> exercises;
    
    
	public User getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}
	public List<String> getExercises() {
		return exercises;
	}
	public void setExercises(List<String> exercises) {
		this.exercises = exercises;
	}
    
}
