package com.example.individual.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.individual.dto.Individual;

@Mapper
public interface IndividualDao {
	public int maxNum() throws Exception;
	
	public void insertData(Individual individual) throws Exception;
}
