package com.p1.kr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.mybatis.IMapperUpload;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ServiceUpload implements IServiceUpload{

	@Autowired
	private IMapperUpload mapper;
	
	@Override
	public List<DomainBoardList> listboard() {
		return mapper.listboard();
	}
	
}
