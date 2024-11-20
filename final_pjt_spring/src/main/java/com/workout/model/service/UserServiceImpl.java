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
	public User login(String username, String password) {
		Map<String, String> info = new HashMap<>();
		info.put("username", username);
		info.put("password", password);
		User tmp = dao.selectOne(info);
		return tmp;
	}

}
