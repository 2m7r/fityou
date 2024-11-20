package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.TrainerClient;
import com.workout.model.dto.User;

public interface TrainerClientService {

	// 회원 등록
	int registClient(TrainerClient trainerClient);

	// 회원 수정
	int modifyClient(TrainerClient client);

	// 특정 회원 정보 조회
	User getClientById(long clientId);

	// 회원 목록 조회
	List<User> getClients(long trainerId);

	// 회원 삭제
	int removeClient(long clientId);

}
