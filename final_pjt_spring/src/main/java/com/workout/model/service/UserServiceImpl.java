package com.workout.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workout.model.dao.UserDao;
import com.workout.model.dto.User;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao dao;

	@Override
	public int registUser(User user) {
		return dao.insertUser(user);
	}

	@Override
	public User authenticateUser(String username, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("password", password);
		
		// Dao를 통해 사용자 인증
		User user = dao.selectOne(params);
		
		//사용자가 null이 아니면 인증 성공으로 간주
		return user;
	}

}
