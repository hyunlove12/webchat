package com.webchat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 테스트를 위한 클래스
@WebServlet("/main.do")
public class MainPage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public MainPage() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 현재 접속자 수		
		//int joinCount = SessionConfig.sessionList.size();
		// 현재 개설 방 수
		int openRoom = WebSocketServerCu.publicRoomList.size();
		// 개설 방 리스트로 반환
		List<String> roomList = new ArrayList<String>(WebSocketServerCu.publicRoomList);
		
		request.setAttribute("openRoom", openRoom);
		//request.setAttribute("joinCount", joinCount);
		request.setAttribute("roomList", roomList);
		System.out.println(roomList.toString());
		System.out.println(openRoom);
		//System.out.println(joinCount);
		forward(request, response, "/WEB-INF/main.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static void forward(
			HttpServletRequest request,
			HttpServletResponse response,
			String path) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);		
	}
	
	public static void redirect(
			HttpServletRequest request,
			HttpServletResponse response,			
			String url) throws ServletException, IOException {		
		request.setAttribute("result",request.getAttribute("result"));
		//파라미터 post전달하는 방법
		response.sendRedirect(url);
	}
	


	
	

	
}