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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.p1.kr.domain.DomainBoardFile;
import com.p1.kr.domain.DomainBoardList;
import com.p1.kr.domain.LoginDomain;
import com.p1.kr.service.IServiceUpload;
import com.p1.kr.service.IServiceUser;
import com.p1.kr.service.ServiceUser;
import com.p1.kr.util.CommonUtils;
import com.p1.kr.util.pagination;
import com.p1.kr.vo.VOFileList;
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
		session.setAttribute("ip", CommonUtils.getClientIP(request));
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
	
	//대시보드 리스트 보여주기
	@GetMapping("UserList")
	public ModelAndView UserList(HttpServletRequest request) {	
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String page = session.getAttribute("page").toString(); // session에 담고 있는 page 꺼냄
		if(page == null)page = "1"; // 없으면 1
		
		//클릭페이지 세션에 담아줌
		session.setAttribute("page", page);
		//페이지네이션
		mav = UserListCall(request);  //리스트만 가져오기
		
		mav.setViewName("admin/adminUserList.html");
		return mav; 
	};
			
			
			//페이징으로 리스트 가져오기 
	public ModelAndView UserListCall(HttpServletRequest request) { //클릭페이지 널이면 
		ModelAndView mav = new ModelAndView();
		//전체 갯수
		int totalcount = serviceUser.getAll();
		int contentnum = 10; // 데이터 가져올 갯수 
		
		
		//데이터 유무 분기때 사용
		boolean itemsNotEmpty;
		
		if(totalcount > 0) { // 데이터 있을때
			
			// itemsNotEmpty true일때만, 리스트 & 페이징 보여주기
			itemsNotEmpty = true;
			//페이지 표현 데이터 가져오기
			Map<String,Object> pagi = pagination.pagination(totalcount, request);
			
			Map map = new HashMap<String, Integer>();
	        map.put("offset",pagi.get("offset"));
	        map.put("pageNum",contentnum);
			
	        //페이지별 데이터 가져오기
			List<LoginDomain> loginDomain = serviceUser.AllList(map);
			
			//모델객체 넣어주기
			mav.addObject("itemsNotEmpty", itemsNotEmpty);
			mav.addObject("items", loginDomain);
			mav.addObject("rowNUM", pagi.get("rowNUM"));
			mav.addObject("pageNum", pagi.get("pageNum"));
			mav.addObject("startpage", pagi.get("startpage"));
			mav.addObject("endpage", pagi.get("endpage"));
		}else {
			itemsNotEmpty = false;
		}
		
		return mav;
	};
	
	
	
	@GetMapping("/modify/{seq}")
    public ModelAndView mbModify(@PathVariable("seq") String seq, RedirectAttributes re) throws IOException {
		ModelAndView mav = new ModelAndView();
		re.addAttribute("seq", seq);
		mav.setViewName("redirect:/adminEditUser");
		return mav;
	};
	
	
	//대시보드 리스트 보여주기
	@GetMapping("adminEditUser")
	public ModelAndView mbListEdit(@RequestParam("seq") String seq, HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		// 해당리스트 가져옴
		mav = UserListCall(request);  
		Map map = new HashMap<String, String>();
		map.put("seq", seq);
		LoginDomain loginDomain = serviceUser.Select(map);
		mav.addObject("item",loginDomain);
		mav.setViewName("admin/adminEditUser.html");
		return mav; 
	};
	
	@PostMapping("create")
	public ModelAndView create(VOLogin vo, HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//session 처리 
		HttpSession session = request.getSession();
		
		//페이지 초기화
		String page = (String) session.getAttribute("page");
		if(page == null)page = "1";
		
		// 중복체크
		Map<String, String> map = new HashMap();
		map.put("id", vo.getId());
		map.put("pw", vo.getPw());
		
		
		// 중복체크
		int dupleCheck = serviceUser.checkDuplication(map);
		if(dupleCheck > 0) { // 가입되있으면  
			String alertText = "중복이거나 유효하지 않은 접근입니다";
			String redirectPath = "/main";
			if(vo.getAdmin() != null) {
				redirectPath = "/main/UserList?page="+page;
			}
			CommonUtils.redirect(alertText, redirectPath, response);
			return mav;
		}else {
			
			//현재아이피 추출
			String IP = CommonUtils.getClientIP(request);
			
			//전체 갯수
			int totalcount = serviceUser.getAll();
			
			//db insert 준비
			LoginDomain loginDomain = LoginDomain.builder()
					.id(vo.getId())
					.pw(vo.getPw())
					// 최초가입자를 level 3 admin 부여
					.level((totalcount == 0) ? "3" : "2") 
					.ip(IP)
					.used("Y")
					.build();
			
			serviceUser.create(loginDomain);
			if(vo.getAdmin() == null) {
				session.setAttribute("ip",IP);
				session.setAttribute("id", loginDomain.getId());
				session.setAttribute("level",loginDomain.getLevel());  
				mav.setViewName("redirect:/List");
			}else { 
				// admin일때
				mav.setViewName("redirect:/UserList?page=1");
			}
		}
		
		return mav;

	};
	
	@RequestMapping("/update")
	public ModelAndView mbModify(VOLogin vo, HttpServletRequest request, RedirectAttributes re) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//page 초기화
		HttpSession session = request.getSession();
		
		String page = "1"; // 업데이트 되면 가장 첫화면으로 갈 것이다.  
		
		//db 업데이트
		LoginDomain loginDomain = null; //초기화
		String IP = CommonUtils.getClientIP(request);
		loginDomain = LoginDomain.builder()
				.seq(Integer.parseInt(vo.getSeq()))
				.id(vo.getId())
				.pw(vo.getPw())
				.level(vo.getLevel())
				.ip(IP)
				.used("Y")
				.build();
		serviceUser.update(loginDomain);
		
		//첫 페이지로 이동
		re.addAttribute("page",page); // 리다이렉트시 파람으로 실어서 보냄
		mav.setViewName("redirect:/UserList");
		return mav;
	};
	
	@GetMapping("/remove/{seq}")
    public ModelAndView mbRemove(@PathVariable("seq") String seq, RedirectAttributes re, HttpServletRequest request) throws IOException {
		ModelAndView mav = new ModelAndView();
		
		//db 삭제
		Map map = new HashMap<String, String>();
		map.put("seq", seq);
		serviceUser.remove(map);

		//page 초기화
		HttpSession session = request.getSession();
				
		//보고 있던 현재 페이지로 이동
		re.addAttribute("page",session.getAttribute("page")); // 리다이렉트시 파람으로 실어서 보냄
		mav.setViewName("redirect:/UserList");
		return mav;
	};
	
	@GetMapping("signin")
    public ModelAndView signIn() throws IOException {
		ModelAndView mav = new ModelAndView();
        mav.setViewName("signin/signin.html"); 
        return mav;
    }
	//로그아웃
	@RequestMapping("logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.invalidate(); // 전체삭제
		mav.setViewName("index.html");
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
		session.setAttribute("ip", CommonUtils.getClientIP(request));
		session.setAttribute("id", domain.getId());
		session.setAttribute("level", domain.getLevel());
		List<DomainBoardList> item=serviceUpload.listboard();
		mv.addObject("items",item);
		mv.setViewName("myboard/myboard.html");
		return mv;
		
	}
	
//	public ModelAndView SelectOne(@ModelAttribute("vo") VOFileList vo, String seq, HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView();
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		HttpSession session = request.getSession();
//		
//		map.put("seq", Integer.parseInt(seq));
//		List<DomainBoardFile> fileList =  serviceUpload.selectBoardFile(map);
//		if(fileList.size()>0) {
//			DomainBoardFile file=fileList.get(0);
//			String path = file.getPath().replaceAll("\\\\", "/");
//			file.setPath(path);
//			mav.addObject("files", file);
//		}
//		//삭제시 사용할 용도
//		session.setAttribute("files", fileList);
//
//		return mav;
//	}
//	
}
