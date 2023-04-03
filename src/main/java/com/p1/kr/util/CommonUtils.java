package com.p1.kr.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class CommonUtils {
	public static String currentTime() {
		SimpleDateFormat sdate=new SimpleDateFormat("yyyyMMddHHmmss",Locale.KOREA);
		Date date=new Date();
		return sdate.format(date);
	}
	public static String getCrilentIP(HttpServletRequest request) {
		String ip=request.getHeader("X-Forwarded-for");
		if(ip==null) {
			ip=request.getHeader("Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		if(ip.equals("0:0:0:0:0:0:0:1")) { 
			 ip = ip.replace("0:0:0:0:0:0:0:1", "127.0.0.1");
		}
		return ip;
	}
	public static void redirect(String text,String path, HttpServletResponse response) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=response.getWriter();
		ModelAndView mav=new ModelAndView();
		//test
		out.println("<script>alert('"+ text +"'); location.href='" + path + "'</script>");
		out.flush();
	}
}
