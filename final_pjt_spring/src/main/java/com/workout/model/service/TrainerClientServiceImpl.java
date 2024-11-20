package com.workout.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.model.dao.TrainerClientDao;
import com.workout.model.dto.TrainerClient;
import com.workout.model.dto.User;

@Service
public class TrainerClientServiceImpl implements TrainerClientService {
	
	@Autowired
	private TrainerClientDao dao;

	@Override
	@Transactional
	public int registClient(TrainerClient trainerClient) {
		return dao.insertClient(trainerClient);
	}

	@Override
	public int modifyClient(TrainerClient client) {
		return dao.updateClient(client);
	}

	@Override
	public User getClientById(long clientId) {
		return dao.selectClientById(clientId);
	}

	@Override
	public List<User> getClients(long trainerId) {
		return dao.selectAllClients(trainerId);
	}

	@Override
	public int removeClient(long clientId) {
		return dao.deleteClient(clientId);
	}

}
