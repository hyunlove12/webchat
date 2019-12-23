package com.webchat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/echo.do/{userId}")
public class WebSocketServerCu {
	 /*https://www.oracle.com/technical-resources/articles/java/jsr356.html 참고 페이지*/
	 private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	 private static Set<String> userId = Collections.synchronizedSet(new HashSet<String>());	
	    @OnMessage
	    public void onMessage(String message, Session session) throws IOException {	  
	    	System.out.println(message);
	    	String userId = session.getPathParameters().get("userId");
	    	String align = "right";
	    	synchronized(clients) {
	            for(Session client : clients) {
	                if(!client.equals(session)) {	                	
	                    client.getBasicRemote().sendText(userId + " : " + message);
	                }else {
	                	client.getBasicRemote().sendText(userId + " : " + message + " : " + align);
	                }
	            }
	        }
	    }
	    
	    @OnOpen
	    public void onOpen(Session session) {
	    	//request못 받네...?
	    	// HttpServletRequest req, HttpServletResponse res
	    	//System.out.println(req.getContextPath());
	    	String pathUserId = session.getPathParameters().get("userId");
	    	for(String id : userId) {
	    		//System.out.println(id);
	    		//System.out.println(session.getPathParameters().get("userId"));
                if(id.equals(session.getPathParameters().get("userId"))) {	                	
                   System.out.println("중복된 아이디 입니다.");
                   return;
                }
            }
	        System.out.println("이것도?");
	    	//System.out.println(userId);
	    	//System.out.println(session.getPathParameters().get("userId"));
	    	//System.out.println(session.getUserProperties().keySet() + "뭐냐");
	    	//System.out.println(session.getUserProperties().values().toString() + "뭐냐");
	    	//System.out.println(session.getUserProperties().get("pathParams") + "뭐냐");
	    	//System.out.println(session.getId());	  
	    	userId.add(pathUserId);
	        clients.add(session);
	    }
	    
	    @OnClose
	    public void onClose(Session session) {
	        clients.remove(session);
	    }
}
