package com.p1.kr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.p1.kr.domain.LoginDomain;
import com.p1.kr.mybatis.IMapperUser;

@Service
public class ServiceUser implements IServiceUser{
	@Autowired
	private IMapperUser mapper;

	@Override
	public LoginDomain Select(Map<String, String> map) {
		return mapper.select(map);
	}

	@Override
	public List<LoginDomain> AllList(Map<String, Integer> map) {
		return mapper.AllList(map);
	}

	@Override
	public int getAll() {
		return mapper.getAll();
	}

	@Override
	public void create(LoginDomain loginDomain) {
		mapper.create(loginDomain);
	}

	@Override
	public LoginDomain getId(Map<String, String> map) {
		return mapper.getId(map);
	}

	@Override
	public int checkDuplication(Map<String, String> map) {
		return mapper.checkDuplication(map);
	}

	@Override
	public void update(LoginDomain loginDomain) {
		mapper.update(loginDomain);
	}

	@Override
	public void remove(Map<String, String> map) {
		mapper.remove(map);
	}
	
}
