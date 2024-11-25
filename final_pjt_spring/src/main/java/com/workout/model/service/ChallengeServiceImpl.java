package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.model.dao.ChallengeDao;
import com.workout.model.dto.Challenge;
import com.workout.model.dto.User;

@Service
public class ChallengeServiceImpl implements ChallengeService{
	
	@Autowired
	private ChallengeDao dao;

	@Override
	@Transactional
	public int registChallenge(Challenge challenge) {
		int result = dao.insertChallenge(challenge);
		if(result > 0) {
			// 챌린지가 정상적으로 등록되었으면, 챌린지 참여자도 추가
	        long challengeId = challenge.getChallengeId(); // 등록된 챌린지 ID
	        long creatorUserId = challenge.getCreatorUserId(); // 챌린지를 등록한 유저의 ID
	        
	        // 유저를 챌린지 참여자로 추가
	        int participantResult = dao.intoChallenge(creatorUserId, challengeId);
	        if (participantResult > 0) {
	            return result; // 성공적으로 등록되었으므로, 1을 반환
	        }
		}
		return 0; // 실패 시 0을 반환
	}

	@Override
	public Challenge getChallengeById(Long challengeId) {
		return dao.selectChallengeById(challengeId);
	}

	@Override
	@Transactional
	public int modifyChallenge(Challenge challenge) {
		return dao.modifyChallenge(challenge);
	}

	@Override
	@Transactional
	public int intoChallenge(Long challengeId, Long userId) {
		dao.countUp(challengeId);
		return dao.intoChallenge(userId, challengeId);
	}
	
	@Override
	public List<User> getChallengeUsers(Long challengeId) {
		return dao.selectChallengeUsers(challengeId);
	}

	@Override
	public List<Challenge> getAllChallenges() {
		return dao.selectAllChallenges();
	}

	@Override
	public List<Challenge> getChallenges(long userId) {
		return dao.selectChallenges(userId);
	}

	@Override
	@Transactional
	public int leaveChallenge(long challengeId, long userId) {
		dao.countDown(challengeId);
		return dao.leaveChallenge(userId, challengeId);
	}

}
