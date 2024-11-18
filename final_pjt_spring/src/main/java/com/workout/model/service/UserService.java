package com.workout.model.service;

import com.workout.model.dto.User;

public interface UserService {
	
	int registUser(User user);
	
	User authenticateUser(String userId, String password);

}
