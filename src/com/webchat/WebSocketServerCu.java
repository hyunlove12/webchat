package com.webchat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/echo.do")
public class WebSocketServerCu {
	 /*https://www.oracle.com/technical-resources/articles/java/jsr356.html 참고 페이지*/
	 private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	    
	    @OnMessage
	    public void onMessage(String message, Session session) throws IOException {
	        System.out.println(message);
	        synchronized(clients) {
	            for(Session client : clients) {
	                if(!client.equals(session)) {	                	
	                    client.getBasicRemote().sendText(message);
	                }
	            }
	        }
	    }
	    
	    @OnOpen
	    public void onOpen(@PathParam("userId")String userId, Session session) {	    	
	    	System.out.println(userId);
	    	System.out.println(session.getId());	    	
	        clients.add(session);
	    }
	    
	    @OnClose
	    public void onClose(Session session) {
	        clients.remove(session);
	    }
}
