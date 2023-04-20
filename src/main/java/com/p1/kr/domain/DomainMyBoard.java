package com.p1.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "builder")
public class DomainMyBoard {
	int boardid;
	String author;
	String title;
	String content;
	String createat;
}
