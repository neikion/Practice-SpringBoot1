package com.p1.kr.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class pagination {
	public static Map<String,Object> pagination(int count, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		String pnum = request.getParameter("page");
		if (pnum == null) { pnum = "1"; }
		
		int rowNUM = Integer.parseInt(pnum);
		if(rowNUM < 0) {rowNUM = 1;}
		

		int pagelimit;
		if (count % 10 == 0) { 
			pagelimit = count / 10; 
		}else { 
			pagelimit = (count / 10) + 1; 
		}
		if(rowNUM > pagelimit) { rowNUM = pagelimit; }

		int middlepage = (rowNUM - 1) % 10;
		int startpage = rowNUM - middlepage;
		int endpage = startpage + (10-1);
	

		if (endpage > pagelimit) { endpage = pagelimit; } 
		

		int offset = (rowNUM - 1) * 10;
		
		map.put("rowNUM", rowNUM);
		map.put("pageNum", pagelimit);
		map.put("startpage", startpage);
		map.put("endpage", endpage);
		map.put("offset", offset);
		return map;
	}
}
