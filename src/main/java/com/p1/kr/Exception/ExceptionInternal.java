package com.p1.kr.Exception;

import java.util.Map;

import com.p1.kr.type.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionInternal extends RuntimeException {

	private static final long serialVersionUID = -7457312605485052136L;
	//serialVersionUID == '분산처리 환경에서 유일한Unique 클래스라는 것을 증명하기 위한 신분증'
	
	private Code code;
	private Map<String, Object> map;
	private String errorMsg;
	
	public ExceptionInternal(Code code) {
		this.code = code;
	}
	
	public ExceptionInternal(Code code, Map<String, Object> map) {
		this(code);
		this.map = map;
	}
	
	public ExceptionInternal(Code code,  String errorMsg) {
		this(code);
		this.errorMsg = errorMsg;
	}
	
	public ExceptionInternal(Code code, Map<String, Object> map, String errorMsg) {
		this(code);
		this.map = map;
		this.errorMsg = errorMsg;
	}
	
	public static ExceptionInternal fire(Code code) {
		return new ExceptionInternal(code);
	}
	
	public static ExceptionInternal fire(Code code, Map<String, Object> map) {
		return new ExceptionInternal(code, map);
	}
	
	public static ExceptionInternal fire(Code code, String errorMsg) {
		return new ExceptionInternal(code, errorMsg);
	}
	
	public static ExceptionInternal fire(Code code, Map<String, Object> map, String errorMsg) {
		return new ExceptionInternal(code, map, errorMsg);
	}
}
