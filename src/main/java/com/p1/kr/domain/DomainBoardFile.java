package com.p1.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class DomainBoardFile {
	private Integer seq;
	private String id;
	
	private String originalname;
	private String newname;
	private String path;
	private Integer filesize;
}
