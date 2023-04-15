package com.p1.kr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.domain.LoginDomain;
import com.p1.kr.service.IServiceUpload;
import com.p1.kr.service.IServiceUser;
import com.p1.kr.util.CommonUtils;
import com.p1.kr.vo.VOLogin;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping(value="/")
public class ContollerUser {
	@Autowired
	private IServiceUser serviceUser;
	@Autowired
	private IServiceUpload serviceUpload;
	
	@RequestMapping(value="board")
	public ModelAndView login(VOLogin loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException{
		ModelAndView mv=new ModelAndView();
		HttpSession session=request.getSession();
		Map<String, String> map=new HashMap();
		map.put("id", loginDTO.getId());
		map.put("pw",loginDTO.getPw());
		LoginDomain domain=serviceUser.getId(map);
		if(serviceUser.checkDuplication(map)==0) {
			String text="없는 아이디 또는 패스워드가 잘못되었습니다.";
			CommonUtils.redirect(text, "/main/signin", response);
			return mv;
		}
		session.setAttribute("ip", CommonUtils.getCrilentIP(request));
		session.setAttribute("id", domain.getId());
		session.setAttribute("level", domain.getLevel());
		List<DomainBoardList> item=serviceUpload.listboard();
		mv.addObject("items",item);
		mv.setViewName("board/boardlist.html");
		return mv;
		
	}
	
	@RequestMapping(value = "List")
	public ModelAndView moveBoardList() { 
		ModelAndView mav = new ModelAndView();
		List<DomainBoardList> items = serviceUpload.listboard();
		mav.addObject("items", items);
		mav.setViewName("board/boardList.html");
		return mav; 
	}
	
	@RequestMapping(value = "mylist")
	public ModelAndView moveMyBoardList() { 
		ModelAndView mav = new ModelAndView();
		List<DomainBoardList> items = serviceUpload.listboard();
		mav.addObject("items", items);
		mav.setViewName("myboard/myboard.html");
		return mav; 
	}
	
	@RequestMapping(value="myboard")
	public ModelAndView mylogin(VOLogin loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException{
		ModelAndView mv=new ModelAndView();
		HttpSession session=request.getSession();
		Map<String, String> map=new HashMap();
		map.put("id", loginDTO.getId());
		map.put("pw",loginDTO.getPw());
		LoginDomain domain=serviceUser.getId(map);
		if(serviceUser.checkDuplication(map)==0) {
			String text="없는 아이디 또는 패스워드가 잘못되었습니다.";
			CommonUtils.redirect(text, "/main/signin", response);
			return mv;
		}
		session.setAttribute("ip", CommonUtils.getCrilentIP(request));
		session.setAttribute("id", domain.getId());
		session.setAttribute("level", domain.getLevel());
		List<DomainBoardList> item=serviceUpload.listboard();
		mv.addObject("items",item);
		mv.setViewName("myboard/myboard.html");
		return mv;
		
	}
	
}
