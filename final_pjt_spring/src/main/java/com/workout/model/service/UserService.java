package com.workout.model.service;

import com.workout.model.dto.FindIdRequest;
import com.workout.model.dto.User;

public interface UserService {
	
	int registUser(User user);
	
	User login(String username, String password);

	String findUsername(FindIdRequest request);

	User findByEmail(String email);

	String findPassword(FindIdRequest request);

	int modifyUser(User user);
}
