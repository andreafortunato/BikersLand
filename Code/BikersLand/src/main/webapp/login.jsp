<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.bikersland.controller.application.LoginControllerApp" %>
<%@ page import="com.bikersland.exception.InvalidLoginException" %>
<%@ page import="com.bikersland.exception.InternalDBException" %>


<html>
	<head>
		<meta charset="UTF-8">
		
		<%
			if(request.getParameter("login") != null) {
				try {
					LoginControllerApp.askLogin(request.getParameter("username"), request.getParameter("password"));
					
					response.sendRedirect("loginOk.jsp");
				} catch (InvalidLoginException e) {
		%>
					<script type="text/javascript">
					    alert("Login sbagliato");
					    document.getElementById("loginForm").reset();
					</script>
		<%
				} catch (InternalDBException idbe) {
		%>
					<script type="text/javascript">
					    alert("C'è stato un errore a caso");
					</script>
		<%
				} 
			}
		%>
		
		<title>BikersLand Homepage</title>
		<!-- <link href="css/style.css" rel="stylesheet"> -->
		<%@ include file="header.jsp"%> 
	</head>
	
	<body>
		
		<p class="headLabel">Login</p>
		<div class="mx-auto parent">
		  <form action="index.jsp" id="loginForm" name="loginForm" method="POST">
			  <div class="input-group mb-3">
				  <div class="input-group-prepend">
				    <span class="input-group-text" id="basic-addon1">Username</span>
				  </div>
				  <input type="text" class="form-control" placeholder="Username" id="username" name="username" aria-label="Username" aria-describedby="basic-addon1">
				</div>
				 
			  <div class="input-group mb-3">
	        <div class="input-group-prepend">
	          <span class="input-group-text" id="basic-addon1">Password</span>
	        </div>
	        <input type="password" class="form-control" placeholder="Password" id="password" name="password" aria-label="Password" aria-describedby="basic-addon1">
	      </div>
	       
	      <input type="submit" class="custom-btn" name="login" value="login">
	     </form>
	   </div>
		
	</body>
</html>
