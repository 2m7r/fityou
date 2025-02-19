package com.workout.model.dao;

import java.util.List;
import java.util.Map;

import com.workout.model.dto.User;

public interface UserDao {
	
	User selectOne(Map<String, String> info);

	int insertUser(User user);

	User findByEmailAndName(String email, String name);

	User selectByEmail(String email);

	User findByEmailAndNameAndId(String email, String name, String username);

	int updateUser(User user);

	void insertPreferredExercises(Long userId, String exercise);

	List<String> getprefereedExcercise(String username);
	
	// 유저 ID가 포함된 유저를 검색
    List<User> findByUserIdContaining(String username);

	User selectUserByUsername(String username);

	int deleteUser(long userId);

	User selectUserById(Long userId);

	List<User> selectAllUsers();
}
