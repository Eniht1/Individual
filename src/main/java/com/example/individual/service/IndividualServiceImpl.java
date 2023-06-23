package com.example.individual.service;

import java.util.List;

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

	@Override
	public Individual getReadData(int num) throws Exception {
		return individualMapper.getReadData(num);
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		return individualMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<Individual> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		return individualMapper.getLists(searchKey, searchValue, start, end);
	}

	@Override
	public void updateHitCount(int num) throws Exception {
		individualMapper.updateHitCount(num);
		
	}

	@Override
	public void updateData(Individual individual) throws Exception {
		individualMapper.updateData(individual);
	}

	@Override
	public void deleteData(int num) throws Exception {
		individualMapper.deleteData(num);
	}

}
