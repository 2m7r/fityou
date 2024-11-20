package com.workout.model.service;

import com.workout.model.dto.Challenge;

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

}
