package com.p1.kr.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.p1.kr.domain.LoginDomain;

//수정시 Mapper.xml도 수정
@Mapper
public interface IMapperUser {
	public LoginDomain select(Map<String, String> map);
	
	public void create(LoginDomain domain);
	
	public List<LoginDomain> AllList(Map<String, Integer> map);
    
    public int getAll();
    
    public LoginDomain getId(Map<String, String> map);
    
    public int checkDuplication(Map<String, String> map);
    
    public void update(LoginDomain loginDomain);
    
    public void remove(Map<String, String> map);
}
