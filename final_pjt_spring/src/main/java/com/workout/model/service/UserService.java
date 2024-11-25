package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.FindIdRequest;
import com.workout.model.dto.User;

public interface UserService {
	
	int registUser(User user);
	
	User selectUserByUsername(String username);
	
	User login(String username, String password);

	String findUsername(FindIdRequest request);

	User findByEmail(String email);

	String findPassword(FindIdRequest request);

	int modifyUser(User user);

	void savePreferredExercises(Long userId, List<String> exercises);

	List<String> getprefereedExcercise(String username);

	List<User> searchUsers(String username);

	int deleteUser(long userId);
}
