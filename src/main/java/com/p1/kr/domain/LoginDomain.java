package com.p1.kr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(builderMethodName="builder")
public class LoginDomain {

	private Integer seq;
	private String id;
	private String pw;
	private String level;
	private String ip;
	private String used;
	private String createat;
	private String updateat;
}