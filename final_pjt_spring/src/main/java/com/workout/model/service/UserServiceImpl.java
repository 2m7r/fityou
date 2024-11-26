package com.workout.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
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

	@Override
	public String findPassword(FindIdRequest request) {
		User user = dao.findByEmailAndNameAndId(request.getEmail(), request.getName(), request.getUsername());
		
		// 없으면
        if(user == null) {
        	throw new UserNotFoundException("해당하는 유저가 없습니다.");
        }
        
        // 본인 확인 질문과 답변 비교
        if (!user.getSecurityAnswer().equals(request.getSecurityAnswer())) {
            throw new InvalidAnswerException("답변이 틀렸습니다.");
        }
        
        return user.getPassword();
    }

	@Override
	public int modifyUser(User user) {
		return dao.updateUser(user);
	}

	@Override
	@Transactional
	public void savePreferredExercises(Long userId, List<String> exercises) {
		for (String exercise : exercises) {
			dao.insertPreferredExercises(userId, exercise);
		}
	}

	@Override
	public List<String> getprefereedExcercise(String username) {
		return dao.getprefereedExcercise(username);
	}

	@Override
	public List<User> searchUsers(String username) {
		return dao.findByUserIdContaining(username);
	}

	@Override
	public User selectUserByUsername(String username) {
		return dao.selectUserByUsername(username);
	}

	@Override
	public int deleteUser(long userId) {
		return dao.deleteUser(userId);
	}

	@Override
	public User selectUserById(Long userId) {
		return dao.selectUserById(userId);
	}

	@Override
	public List<User> selectAllUsers() {
		return dao.selectAllUsers();
	}

	@Override
	public List<User> searchUser(Long[] userIds) {
		List<User> list = new ArrayList<>();
		for (Long long1 : userIds) {
			User user = dao.selectUserById(long1);
			list.add(user);
		}
		return list;
	}

}
