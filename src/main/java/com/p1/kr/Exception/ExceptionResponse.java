package com.p1.kr.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.p1.kr.type.Code;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(builderMethodName = "builder")
public class ExceptionResponse {
	private Integer result;
	private String resultDesc;
	private HttpStatus httpStatus;
	private String resDate;
	private String reqNo;
}
