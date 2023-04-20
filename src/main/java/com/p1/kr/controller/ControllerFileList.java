package com.p1.kr.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ModelAndView uploadDetail(VOFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq) throws Exception {
		ModelAndView mav = new ModelAndView();
		int seq = service.fileProcess(vo, request, httpreq);
		vo.setContent(""); //초기화
		vo.setTitle(""); //초기화
		
		mav = selectOne(vo, String.valueOf(seq),request);
		mav.setViewName("board/boardList.html");
		return mav;
	}
	
	//board and file select
	public ModelAndView selectOne(@ModelAttribute("vo") VOFileList vo, String seq, HttpServletRequest request) {
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
	
	@GetMapping("detail")
    public ModelAndView getDetail(@ModelAttribute("vo") VOFileList vo, @RequestParam("seq") String seq, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		//파일 하나 가져오기
		mav = selectOne(vo, seq,request);
		mav.setViewName("board/boardList.html");
		return mav;
	}
	
	@GetMapping("edit")
	public ModelAndView editDetail(VOFileList vo, @RequestParam("seq") String seq, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();

		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		map.put("seq", Integer.parseInt(seq));
		DomainBoardList domain =service.selectBoard(map);
		List<DomainBoardFile> fileList =  service.selectBoardFile(map);
		
		for (DomainBoardFile list : fileList) {
			String path = list.getPath().replaceAll("\\\\", "/");
			list.setPath(path);
		}
		vo.setSeq(domain.getSeq());
		vo.setContent(domain.getContent());
		vo.setTitle(domain.getTitle());
		vo.setIsEdit("edit");  // upload 재활용하기위해서
		
	
		mav.addObject("detail", domain);
		mav.addObject("files", fileList);
		mav.addObject("fileLength",fileList.size());
		
		mav.setViewName("board/boardEditList.html");
		return mav;
	}
	
	@PostMapping("editSave")
	public ModelAndView saveEditDetail(@ModelAttribute("vo") VOFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		service.fileProcess(vo, request, httpReq);
		
		mav = selectOne(vo, vo.getSeq(),request);
		vo.setContent(""); //초기화
		vo.setTitle(""); //초기화
		mav.setViewName("board/boardList.html");
		return mav;
	}
	
	@GetMapping("remove")
	public ModelAndView removeDetail(@RequestParam("seq") String seq, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<DomainBoardFile> fileList = null;
		if(session.getAttribute("files") != null) {						
			fileList = (List<DomainBoardFile>) session.getAttribute("files");
		}

		map.put("seq", Integer.parseInt(seq));
		
		//내용삭제
		service.removeContent(map);

		for (DomainBoardFile list : fileList) {
			list.getPath();
			Path filePath = Paths.get(list.getPath());
	 
	        try {
	        	
	            // 파일 물리삭제
	            Files.deleteIfExists(filePath);
	            // db 삭제 
				service.removeFile(list);
				
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
		}

		//세션해제
		session.removeAttribute("files"); // 삭제
		mav = getBoardList();
		mav.setViewName("board/boardList.html");
		
		return mav;
	}

	// ControllerUser moveBoardList와 같음
	//리스트 가져오기 따로 함수뺌
	public ModelAndView getBoardList() {
		ModelAndView mav = new ModelAndView();
		List<DomainBoardList> items = service.listboard();
		mav.addObject("items", items);
		return mav;
	}
}
