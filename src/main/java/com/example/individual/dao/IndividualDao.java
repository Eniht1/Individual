package com.example.individual.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.individual.dto.Individual;

@Mapper
public interface IndividualDao {
	public int maxNum() throws Exception;
	
	public void insertData(Individual individual) throws Exception;

	public Individual getReadData(int num) throws Exception;

	public int getDataCount(String searchKey, String searchValue) throws Exception;

	public List<Individual> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	public void updateHitCount(int num) throws Exception;

	public void updateData(Individual individual) throws Exception;

	public void deleteData(int num) throws Exception;
	
}
