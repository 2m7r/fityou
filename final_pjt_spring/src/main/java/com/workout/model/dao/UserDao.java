package com.workout.model.dao;

import java.util.Map;

import com.workout.model.dto.User;

public interface UserDao {
	/**
	 * 사용자 정보를 조회하는 메서드 (비밀번호 제외)
	 * @param params 사용자 ID와 비밀번호를 담은 Map 객체
	 * @return 조회된 사용자 정보
	 */
	User selectOne(Map<String, Object> params);

	/**
	 * 새로운 사용자를 등록하는 메서드
	 * @param user 등록할 사용자 객체
	 * @return 삽입 결과에 영향을 받은 행의 수
	 */
	int insertUser(User user);
}
