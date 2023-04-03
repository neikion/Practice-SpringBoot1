package com.p1.kr.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.vo.VOFileList;

public interface IServiceUpload {
	public List<DomainBoardList> listboard();
	public int fileProcess(VOFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq);
	
	public void removeContent(HashMap<String, Object> map);
	
	public void removeFile(DomainBoardFile domain);
	
	public DomainBoardList selectBoard(HashMap<String, Object> map);
	public List<DomainBoardFile> selectBoardFile(HashMap<String, Object> map);
}
