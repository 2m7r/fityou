package com.workout.model.dao;

import java.util.Map;

import com.workout.model.dto.User;

public interface UserDao {
	
	User selectOne(Map<String, String> info);

	int insertUser(User user);

	User findByEmailAndName(String email, String name);

	User selectByEmail(String email);
}
