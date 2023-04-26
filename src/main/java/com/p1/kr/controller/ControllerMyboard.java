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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainMyBoard;
import com.p1.kr.domain.DomainMyBoardFile;
import com.p1.kr.domain.DomainMyBoardList;
import com.p1.kr.service.IServiceMyBoard;
import com.p1.kr.service.IServiceUser;
import com.p1.kr.vo.VOFileList;
import com.p1.kr.vo.VOMyFileList;

@Controller
public class ControllerMyboard {

	@Autowired
	IServiceMyBoard serviceBoard;
	
	@Autowired
	IServiceUser serviceUser;
	
	@RequestMapping(value = "mylist")
	public ModelAndView moveMyBoardList() { 
		//todo 중복 파일이 있을경우 여러개 표시됨.
		ModelAndView mav = new ModelAndView();
		List<DomainMyBoardList> items = serviceBoard.getBoardList();
		mav.addObject("items", items);
		mav.setViewName("myboard/myboard.html");
		return mav; 
	}
	
	@RequestMapping(value="mydetail")
	public ModelAndView getMyDetail(@ModelAttribute("vo") VOMyFileList vo, @RequestParam("boardid") String boardid, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		int id=Integer.parseInt(boardid);
		DomainMyBoard boardDomain =serviceBoard.getBoard(id);
		List<DomainMyBoardFile> fileList =  serviceBoard.getFileList(id);
		mav.addObject("detail", boardDomain);
		mav.addObject("files", fileList);
		mav.setViewName("myboard/myboard.html");
		return mav;
	}
	
	public ModelAndView selectOne(@ModelAttribute("vo") VOMyFileList vo, String boardid, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		int id=Integer.parseInt(boardid);
		DomainMyBoard boardDomain =serviceBoard.getBoard(id);
		List<DomainMyBoardFile> fileList =  serviceBoard.getFileList(id);
		mav.addObject("detail", boardDomain);
		mav.addObject("files", fileList);

		return mav;
	}
	
	@PostMapping(value = "myupload")
	public ModelAndView uploadMyDetail(@ModelAttribute("vo")VOMyFileList vo, MultipartHttpServletRequest request, HttpServletRequest httpreq) throws Exception {
		ModelAndView mav = new ModelAndView();
		int boardid = serviceBoard.uploadFile(vo, request, httpreq);
		vo.setContent(""); //초기화
		vo.setTitle(""); //초기화
		mav = selectOne(vo, String.valueOf(boardid),request);
		mav.setViewName("myboard/myboard.html");
		return mav;
	}
	
	@GetMapping("myedit")
	public ModelAndView editDetail(VOMyFileList vo, @RequestParam("boardid") String id, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		int intid=Integer.parseInt(id);
		DomainMyBoard domain =serviceBoard.getBoard(intid);
		List<DomainMyBoardFile> fileList =  serviceBoard.getFileList(intid);
		
		vo.setBoardid(id);
		vo.setContent(domain.getContent());
		vo.setTitle(domain.getTitle());
		vo.setIsEdit("edit");
		
	
		mav.addObject("detail", domain);
		mav.addObject("files", fileList);
		mav.addObject("fileLength",fileList.size());
		
		mav.setViewName("myboard/myboardEdit.html");
		return mav;
	}
	
	@GetMapping("myremove")
	public ModelAndView removeDetail(@RequestParam("boardid") String boardid, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		HashMap<String, Object> map = new HashMap<String, Object>();
		int id=Integer.parseInt(boardid);
		serviceBoard.removeFiles(id);
		//내용삭제
		serviceBoard.removeContent(id);
		
//		mav = getBoardList();
		mav.setViewName("redirect:/mylist");
		
		return mav;
	}
}
