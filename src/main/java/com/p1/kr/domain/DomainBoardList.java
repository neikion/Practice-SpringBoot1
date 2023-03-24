package com.p1.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "builder")
public class DomainBoardList {
	private String seq;
	private String id;
	private String title;
	private String content;
	private String createat;
	private String updateat;
}
