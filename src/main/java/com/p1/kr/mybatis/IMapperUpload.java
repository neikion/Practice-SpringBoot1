package com.p1.kr.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.p1.kr.domain.DomainBoardContent;
import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainBoardList;

@Mapper
public interface IMapperUpload {
	public List<DomainBoardList> listboard();
	public void uploadContent(DomainBoardContent domain);
	public void uploadFile(DomainBoardFile domain);

	public void updateContent(DomainBoardContent domain);
	public void updateFile(DomainBoardFile domain);

	public void removeContent(HashMap<String, Object> map);
	public void removeFile(DomainBoardFile domain);
	
	public DomainBoardList selectBoard(HashMap<String, Object> map);
	public List<DomainBoardFile> selectBoardFile(HashMap<String, Object> map);
}
