package com.example.individual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.individual.dao.IndividualDao;
import com.example.individual.dto.Individual;

@Service
public class IndividualServiceImpl implements IndividualService{
	@Autowired
	private IndividualDao individualMapper;

	@Override
	public int maxNum() throws Exception {
		return individualMapper.maxNum();
	}

	@Override
	public void insertData(Individual individual) throws Exception {
		individualMapper.insertData(individual);
	}

}
