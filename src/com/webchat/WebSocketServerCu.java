package com.webchat;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/echo/{roomName}/{userId}")
public class WebSocketServerCu {
	 	/*https://www.oracle.com/technical-resources/articles/java/jsr356.html 참고 페이지*/
	 	
		//접속 채팅방 이름
		private String roomName = "";
		
		//채팅방 목록
		private static Set<String> room = Collections.synchronizedSet(new HashSet<String>());
		
		//채팅방 당 참가 인원 -> 중복 방 생성 못함
		private static Map<String, Set<Session>> roomList = Collections.synchronizedMap(new HashMap<String, Set<Session>>());
		
		private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
		
	 	private static Set<String> userId = Collections.synchronizedSet(new HashSet<String>());	
	 	
	 	// 메인 페이지에서 사용할 방 목록
		public static Set<String> publicRoomList = Collections.synchronizedSet(new HashSet<String>());
	 
	    @OnMessage
	    public void onMessage(String message, Session session) throws IOException {	  
	    	System.out.println(message);
	    	String userId = session.getPathParameters().get("userId");
	    	String roomName = session.getPathParameters().get("roomName");
	    	String align = "right";	    	
	    	System.out.println(roomList.get(roomName));
	    	System.out.println(roomList.get(roomName).toString());
	    	System.out.println(roomList.get(roomName).size());
	    	synchronized(roomList.get(roomName)) {
	            for(Session client : roomList.get(roomName)) {	            	
	                if(!client.equals(session)) {
	                    client.getBasicRemote().sendText(userId + " : " + message);
	                }else {
	                	client.getBasicRemote().sendText(userId + " : " + message + " : " + align);
	                }
	            }
	        }
		/*
		 * synchronized(clients) { for(Session client : clients) {
		 * if(!client.equals(session)) { client.getBasicRemote().sendText(userId + " : "
		 * + message); }else { client.getBasicRemote().sendText(userId + " : " + message
		 * + " : " + align); } } }
		 */
	    }
	    
	    @OnOpen
	    public void onOpen(Session session, EndpointConfig config) {
	    	//여기서 session이 무엇인지...id + session id 로 값 저장하여 중복 체크
	    	//request못 받네...?
	    	// HttpServletRequest req, HttpServletResponse res
	    	//System.out.println(req.getContextPath());
	    	String pathUserId = session.getPathParameters().get("userId");
	    	roomName = session.getPathParameters().get("roomName");
	    	//System.out.println("roomName" + roomName);
		/*
		 * for(String id : userId) { //System.out.println(id);
		 * //System.out.println(session.getPathParameters().get("userId"));
		 * if(id.equals(session.getPathParameters().get("userId"))) {
		 * System.out.println("중복된 아이디 입니다."); return; } }
		 */
	       
	    	//System.out.println(userId);
	    	//System.out.println(session.getPathParameters().get("userId"));
	    	//System.out.println(session.getUserProperties().keySet() + "뭐냐");
	    	//System.out.println(session.getUserProperties().values().toString() + "뭐냐");
	    	//System.out.println(session.getUserProperties().get("pathParams") + "뭐냐");
	    	//System.out.println(session.getId());	  
	    	userId.add(pathUserId);
	        clients.add(session);
	        
	        if(roomList.get(roomName) == null) {
	        	System.out.println("방 생성 없음");
	        	//기존에 생성된 방이 없을 경우
	        	Set<Session> temp = new HashSet<Session>();
	        	temp.add(session);
	        	roomList.put(roomName, temp);
	        	publicRoomList.add(roomName);
	        } else {
	        	System.out.println("방 생성 있음");
	        	//기존에 생성된 방이 있을경우
	        	roomList.get(roomName).add(session);
	        }	        
	        
	        // httpSession
	        //HttpSession httpSession = (HttpSession) config.getUserProperties().get("Session");	        
	    }
	    
	    @OnClose
	    public void onClose(Session session) {
	    	//브라우저 종료되면 자동으로 소켓접속 종료
	    	System.out.println("종료!");
	    	//해당 방에 해당하는 session만 종료
	    	//System.out.println("종료!");
	    	//System.out.println(session.getPathParameters().get("userId"));
	        clients.remove(session);
	        roomList.get(roomName).remove(session);
	        userId.remove(session.getPathParameters().get("userId"));
	        System.out.println(roomList.get(roomName).size() + "size");
	        //접속자 수 0명일 경우 map 삭제
	        if(roomList.get(roomName) == null || roomList.get(roomName).size() == 0) {
	        	roomList.remove(roomName);
	        	publicRoomList.remove(roomName);
	        }
	    }
}
