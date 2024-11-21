package com.workout.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workout.exception.InvalidAnswerException;
import com.workout.exception.UserNotFoundException;
import com.workout.model.dao.UserDao;
import com.workout.model.dto.FindIdRequest;
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
	
	@Override
	public String findUsername(FindIdRequest request) {
        User user = dao.findByEmailAndName(request.getEmail(), request.getName());
        
        // 없으면
        if(user == null) {
        	throw new UserNotFoundException("해당하는 유저가 없습니다.");
        }
        
        // 본인 확인 질문과 답변 비교
        if (!user.getSecurityAnswer().equals(request.getSecurityAnswer())) {
            throw new InvalidAnswerException("답변이 틀렸습니다.");
        }

        return user.getUsername();
    }

	@Override
	public User findByEmail(String email) {
		return dao.selectByEmail(email);
	}

}
