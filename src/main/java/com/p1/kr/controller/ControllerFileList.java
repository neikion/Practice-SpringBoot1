package com.p1.kr.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.service.IServiceUpload;
import com.p1.kr.vo.VOFileList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ControllerFileList {
	
	@Autowired
	private IServiceUpload service;
	
	@PostMapping(value = "upload")
	public ModelAndView Upload(VOFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq) throws Exception {
		ModelAndView mav = new ModelAndView();
		int seq = service.fileProcess(vo, request, httpreq);
		vo.setContent(""); //초기화
		vo.setTitle(""); //초기화
		
		mav = SelectOne(vo, String.valueOf(seq),request);
		mav.setViewName("board/boardList.html");
		return mav;
	}
	
	public ModelAndView SelectOne(@ModelAttribute("vo") VOFileList vo, String seq, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		map.put("seq", Integer.parseInt(seq));
		DomainBoardList boardListDomain =service.selectBoard(map);
		List<DomainBoardFile> fileList =  service.selectBoardFile(map);
		
		for (DomainBoardFile file : fileList) {
			String path = file.getPath().replaceAll("\\\\", "/");
			file.setPath(path);
		}
		mav.addObject("detail", boardListDomain);
		mav.addObject("files", fileList);
		//삭제시 사용할 용도
		session.setAttribute("files", fileList);

		return mav;
	}
}
