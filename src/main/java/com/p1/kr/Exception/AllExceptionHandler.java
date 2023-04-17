package com.p1.kr.Exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;

import com.p1.kr.util.CommonUtils;

@ControllerAdvice
public class AllExceptionHandler {
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public HttpEntity<ExceptionResponse> handlerBindingResultException(ExceptionRequest exception){
		
		// 콘솔에 뿌리기
		if(exception.getException() != null) {
			Exception ex = exception.getException();
			StackTraceElement [] steArr = ex.getStackTrace();
			for(StackTraceElement ste : steArr) {
				System.out.println(ste.toString());
			}
		}
		
		// response로 보내기
		ExceptionResponse errRes = ExceptionResponse.builder()
				.result(exception.getCode().getResult())
				.resultDesc(exception.getCode().getResultDesc())
				.resDate(CommonUtils.currentTime())
				.reqNo(exception.getReqNo())
				.httpStatus(exception.getHttpStatus())
				.build();
		exception.printStackTrace();
		return new ResponseEntity<ExceptionResponse>(errRes, errRes.getHttpStatus());
	}
	
	
	//db error
	@ExceptionHandler(InternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpEntity<ExceptionResponse> handelerInternalServerError(ExceptionInternal exception) {
		exception.printStackTrace();
		ExceptionResponse errRes = ExceptionResponse.builder()
				.result(exception.getCode().getResult())
				.resultDesc(exception.getCode().getResultDesc())
				.resDate(CommonUtils.currentTime())
				.reqNo(CommonUtils.currentTime())
				.build();
		return new ResponseEntity<ExceptionResponse>(errRes, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// error page
	@ExceptionHandler(Exception.class)
	public ModelAndView commonException(Exception e) {
		e.printStackTrace();
		e.getStackTrace();
		ModelAndView mv = new ModelAndView();
		mv.addObject("exception", e.getStackTrace());
		mv.setViewName("commons/commonErr.html");
		return mv;
	}
	
}