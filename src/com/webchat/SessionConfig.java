package com.webchat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionConfig implements HttpSessionListener{
	
	public static Set<HttpSession> sessionList = Collections.synchronizedSet(new HashSet<HttpSession>());
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("세션 생성");
		HttpSession session = arg0.getSession();
		sessionList.add(session);		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("세션 종료");
		HttpSession session = arg0.getSession();
		sessionList.remove(session);		
	}
	

}
