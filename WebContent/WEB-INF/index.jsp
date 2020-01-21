<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
	Snapshot by TEMPLATED
	templated.co @templatedco
	Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
<head>
		<title>Snapshot by TEMPLATED</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/bootstrap.css" />
</head>
<body>
<div class="page-wrap">
			<!-- Main -->
			<section id="main">
				<!-- Contact -->
				<section id="contact">
					<!-- Social -->
					<div class="social column">
						<h3>CHAT WITH US</h3>
						<p>안녕하세요!</p>
						<p>WEBSOCKET을 이용한 채팅 테스트 사이트입니다.</p>						
						<p>아이디를 입력 후 채팅을 진행하시면 됩니다.</p>
						<p>springSocket으로 변경 및 파일  첨부까지 추가 예정입니다.</p>
						<h3>Follow Me</h3>
						<ul class="icons">
							<li><a href="#" class="icon fa-twitter"><span class="label">Twitter</span></a></li>
							<li><a href="#" class="icon fa-facebook"><span class="label">Facebook</span></a></li>
							<li><a href="#" class="icon fa-instagram"><span class="label">Instagram</span></a></li>
						</ul>
					</div>

					<!-- Form -->
					<div class="column">
						<h3>채팅!</h3>
						<div class="field half first joinChat">
								<label for="nikName">닉네임</label>
								<input name="nikName" id="nikName" type="text" placeholder="닉네임 입력">
						</div>						
						<div class="field half first text-right joinChat">							
								<input type="button" name="submit" id="join"  value="참가">
						</div>
						<div id="mainText">
						
						</div>
						<div class="field textChat hide">
								<label for="chatText"></label>
								<input name="chatText" id="chatText" type="text" placeholder="채팅을 입력하세요"  onkeyup="javascript:fn_enter()">
						</div>
						<div class="field text-right textChat hide">
								<input type="button" name="txSubmit" id="txSubmit"  value="전송">								
								<input type="button" name="closeChat" id="closeChat"  value="종료" >
						</div>						
						
					</div>
				</section>
				
				
				
				<!-- Footer -->
				<footer id="footer">
					<div class="copyright">
						&copy; Untitled Design: <a href="https://templated.co/">TEMPLATED</a>. images: <a href="https://unsplash.com/">Unsplash</a>.
					</div>
				</footer>
			</section>
		</div>

		<!-- Scripts -->
		<script src="js/jquery.min.js"></script>
		<script src="js/jquery.poptrox.min.js"></script>
		<script src="js/jquery.scrolly.min.js"></script>
		<script src="js/skel.min.js"></script>
		<script src="js/util.js"></script>
		<script src="js/main.js"></script>
</body>
<script type="text/javascript">

	var ws = null;
	var nickName = '';
	$(document).ready(function(){	
		
		$("#join").click(function(){
			//alert('connect호출!');
			nickName = $("#nikName").val();
			if(nickName != '' ){
				connect();				
			} else {
				alert('닉네임을 입력해 주세요!');
				nickName = '';
			}
		});		
		$("#txSubmit").click(function(){
			send();
		});	
		
		$("#closeChat").click(function(){			
			//ws.onclose();
			ws.close();
		});
		
	});
	
	function fn_enter(){
		//alert(1);
		if (window.event.keyCode == 13) {
			send();
        }		
	}
	
	function connect() {		
		// window.location.href = '<%=request.getContextPath() %>/createSession?userId=' + nickName;
		 
		 
		 
		 var target = "ws://localhost:8081/echo.do/" + nickName;		 
		 //var target = "ws://203.245.44.21:8073/echo.do/" + nickName;
		 		 
			 //?usr=홍길동"; //서버에서 파라미터를 	
		 if ('WebSocket' in window) {			
		     ws = new WebSocket(target);		     
		 } else if ('MozWebSocket' in window) {			 
		     ws = new MozWebSocket(target);
		 } else {
		     alert('WebSocket is not supported by this browser.');
		     return;
		 }	
		 ws.onopen = function () {
			 alert('서버연결!');
			 $(".joinChat").addClass("hide");
			 $(".textChat").removeClass("hide");
		    // document.getElementById("msg").innerText = 'Info: WebSocket connection opened.';
		 };	
		 //메시지 수신해주는 함수
	 	 ws.onmessage = function (event) {
			 console.log(event);
		//	 alert('event' + event.data.nickName);
			// alert('event' + event.data['nickName']);
			 //alert('data' + event.data);
			 //alert(event.keys('nickName'));
			 var message = event.data.split(':');
			 //alert(message);
			 var id = message[0];
			 var msg = message[1];
			 var align = message[2];
			 if(align != null && align != ''){
				 $("#mainText").append('<p class="text-right">' + id + ':' + msg + '</p>');	 
			 } else {
				 $("#mainText").append('<p>' + id + ':' + msg + '</p>');
			 }				
			 //document.getElementById("mainText").innerText = 'Received: '+event.data;
			 //document.getElementById("mainText").innerText = id + ':' + msg;
			 
		 };	
		 ws.onclose = function () {
			 alert('연결종료!');
			 $(".joinChat").removeClass("hide");
			 $(".textChat").addClass("hide");
		    // document.getElementById("msg").innerText = 'Info: WebSocket connection closed.';
		 }; 
	}
	
	function send() {// JSON 문자열을 서버로 전송한다
		//alert('메시지 전송!');
		var msg = $("#chatText").val();
	/* 	var msg = {
			"nickName":"홍길동", "content":"test"
		}; */
		//var jsonStr = JSON.stringify(msg);
	//	ws.onopen = () => ws.send('hello');
		//ws.send(jsonStr);
		ws.send(msg);
		$("#chatText").val('');
		$("#chatText").focus();		
		$(location).attr('href',"#chatText"); //입력 후 위치
		//console.log($("#chatText")[0]); //input태그 요소가 그대로 들어온다..
		//console.log($("#chatText").scrollHeight); //input태그 요소가 그대로 들어온다..
		//$("#chatText").scrollTop($("#chatText")[0].scrollHeight);
//		ws.send(message);
	}
	
	function sendFile() {
		// Sending file as Blob
		var file = document.querySelector('input[type="file"]').files[0];
		ws.send(file);
	}
		
</script>
</html>