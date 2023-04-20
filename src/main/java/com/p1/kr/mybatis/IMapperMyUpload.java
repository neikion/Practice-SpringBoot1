package com.p1.kr.mybatis;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.p1.kr.domain.DomainMyBoard;
import com.p1.kr.domain.DomainMyBoardFile;
import com.p1.kr.domain.DomainMyBoardList;

@Mapper
public interface IMapperMyUpload {
	public List<DomainMyBoardList> getBoardList();
	public void uploadContent(DomainMyBoard domain);
	public void uploadFile(DomainMyBoardFile domain);
	public void updateContent(DomainMyBoard domain);
//	public void updateFile(DomainMyBoardFile domain);
//
	public void removeContent(int id);
	public void removeFile(int id);
	
	public DomainMyBoard getBoard(int boardid);
	
	public List<DomainMyBoardFile> getFileList(int boardid);
	public DomainMyBoardFile getFile(int boardid);
}
