package com.p1.kr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder(builderMethodName = "builder")
public class DomainMyBoardList {
	int boardid;
	String author;
	String title;
	String content;
	String createat;
	String newname;
	
	public DomainMyBoardList(int boardid, String author, String title, String content, String createat) {
		this.boardid=boardid;
		this.author=author;
		this.title=title;
		this.content=content;
		this.createat=createat;
	}
}
