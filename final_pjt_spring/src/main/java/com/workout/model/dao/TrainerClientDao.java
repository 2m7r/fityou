package com.workout.model.dao;

import java.util.List;

import com.workout.model.dto.TrainerClient;
import com.workout.model.dto.User;

public interface TrainerClientDao {

	// 회원 등록
	int insertClient(TrainerClient trainerClient);

	// 회원 수정
	int updateClient(TrainerClient client);

	// 특정 회원 조회
	User selectClientById(long clientId);

	// 회원 목록 조회
	List<User> selectAllClients(long trainerId);

	// 회원 삭제
	int deleteClient(long clientId);

}
