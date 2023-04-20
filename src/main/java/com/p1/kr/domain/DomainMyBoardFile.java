package com.p1.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "builder")
public class DomainMyBoardFile {
	int boardid;
	String originname;
	String newname;
	String path;
	int filesize;
}
