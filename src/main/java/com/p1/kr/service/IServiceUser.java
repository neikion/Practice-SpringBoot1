package com.p1.kr.service;

import java.util.List;
import java.util.Map;

import com.p1.kr.domain.LoginDomain;

public interface IServiceUser {
	public LoginDomain Select(Map<String, String> map);
    
    public List<LoginDomain> AllList(Map<String, Integer> map);
    
    public int getAll();
    
    public void create(LoginDomain loginDomain);
    
    public LoginDomain getId(Map<String, String> map);
    
    public int checkDuplication(Map<String, String> map);
    
    public void update(LoginDomain loginDomain); 
    
    public void remove(Map<String, String> map); 
}
