package com.p1.kr.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.p1.kr.type.Code;

import lombok.Getter;

@Getter
public class ExceptionRequest extends RuntimeException {
private static final long serialVersionUID = 855926457087732200L;  // 이기종간 충돌방지 / 클래스에서 클릭
	
	private Code code;
	private String msg;
	private HttpStatus httpStatus;
	private Exception exception;
	private BindingResult result;
	private String reqNo;
	

	// 생성자
	public ExceptionRequest(Code code) {
		this.code = code;
	}
	
	public ExceptionRequest(Code code, String errMsg) {
		this.code = code;
		this.msg = errMsg;
	}
	
	
	public ExceptionRequest(Code code, String errMsg, HttpStatus httpStatus) {
		this.code = code;
		this.msg = errMsg;
		this.httpStatus = httpStatus;
	}
	
	public ExceptionRequest(Code code, String errMsg, HttpStatus httpStatus, Exception exception) {
		this.code = code;
		this.msg = errMsg;
		this.httpStatus = httpStatus;
		this.exception = exception;
	}
	
	public ExceptionRequest(Code code, String errMsg, HttpStatus httpStatus, Exception exception, BindingResult result,
			String reqNo) {
		this.code = code;
		this.msg = errMsg;
		this.httpStatus = httpStatus;
		this.exception = exception;
		this.result = result;
		this.reqNo = reqNo;
	}
	
	//생성자 이용 객체생성
	public static ExceptionRequest fire(Code code) {
		return new ExceptionRequest(code);
	}
	
	public static ExceptionRequest fire(Code code, String errMsg) {
		return new ExceptionRequest(code, errMsg);
	}
	
	public static ExceptionRequest fire(Code code, String errMsg, HttpStatus httpStatus) {
		return new ExceptionRequest(code, errMsg, httpStatus);
	}
	
	public static ExceptionRequest fire(Code code, String errMsg, HttpStatus httpStatus, Exception exception) {
		return new ExceptionRequest(code, errMsg, httpStatus, exception);
	}
	
	public static ExceptionRequest fire(Code code, String errMsg, HttpStatus httpStatus, Exception exception, BindingResult result, String reqNo) {
		return new ExceptionRequest(code);
	}
}
