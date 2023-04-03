package com.p1.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class DomainBoardContent {
	private Integer seq;
	private String id;

	private String title;
	private String content;
}
