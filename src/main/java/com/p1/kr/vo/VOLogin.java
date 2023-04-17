package com.p1.kr.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class VOLogin {
	private String seq;
	private String id;
	private String pw;
	private String admin;
	private String level;
}
