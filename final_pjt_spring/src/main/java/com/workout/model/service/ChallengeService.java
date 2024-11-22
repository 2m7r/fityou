package com.workout.model.service;

import java.util.List;

import com.workout.model.dto.Challenge;
import com.workout.model.dto.User;

public interface ChallengeService {

	// 챌린지 등록
	int registChallenge(Challenge challenge);

	// 챌린지 상세 조회
	Challenge getChallengeById(Long challengeId);

	// 챌린지 수정
	int modifyChallenge(Challenge challenge);

	// 챌린지 삭제
	int removeChallenge(Long challengeId);

	// 챌린지 참여
	int intoChallenge(Long challengeId, Long userId);
	
	// 챌린지 참여자 목록 조회
	List<User> getChallengeUsers(Long challengeId);
	
	// 전체 챌린지 목록 조회
	List<Challenge> getAllChallenges();

	List<Challenge> getChallenges(long userId);


}
