package com.p1.kr.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.p1.kr.domain.DomainMyBoardList;
import com.p1.kr.vo.VOMyFileList;
import com.p1.kr.domain.DomainMyBoardFile;
import com.p1.kr.domain.DomainMyBoard;

public interface IServiceMyBoard {
	public List<DomainMyBoardList> getBoardList();
	
	public int uploadFile(VOMyFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq);
	
	public void removeContent(int id);
	
	public void removeFile(int id);
	public void removeFiles(int id);
	public DomainMyBoard getBoard(int boardid);
	
	public List<DomainMyBoardFile> getFileList(int boardid);
	public DomainMyBoardFile getFile(int boardid);
}
