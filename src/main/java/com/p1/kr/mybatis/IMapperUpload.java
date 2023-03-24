package com.p1.kr.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.p1.kr.domain.DomainBoardList;

@Mapper
public interface IMapperUpload {
	public List<DomainBoardList> listboard();
}
