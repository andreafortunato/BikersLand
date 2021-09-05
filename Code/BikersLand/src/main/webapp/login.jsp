<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.bikersland.controller.application.LoginControllerApp" %>
<%@ page import="com.bikersland.exception.InvalidLoginException" %>
<%@ page import="com.bikersland.exception.InternalDBException" %>
<%@ page import="com.bikersland.bean.UserBean" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		
		<%
			if(request.getParameter("login") != null) {
				UserBean userBean;
				try {
					userBean = LoginControllerApp.askLoginWeb(request.getParameter("username"), request.getParameter("password"));
					
					session.setAttribute("logged-user-bean", userBean);
					
					response.sendRedirect("index.jsp");
				} catch (InvalidLoginException e) {
					session.removeAttribute("logged-user-bean");
		%>
					<script type="text/javascript">
					    alert("Wrong Username/Email and/or Password!\nPlease try again...");
					    document.getElementById("loginForm").reset();
					</script>
		<%
				} catch (InternalDBException idbe) {
		%>
					<script type="text/javascript">
					  alert("Internal Error, you will be redirected to the homepage");
	        </script>
	            <%
	          response.sendRedirect("index.jsp");
				} 
			}
		%>
		
		<title>BikersLand Homepage</title>
		<%@ include file="header.jsp"%> 
	</head>
	
	<body>
		
		<p class="headLabel">Login</p>
		<div class="mx-auto parent">
		  <form action="login.jsp" id="loginForm" name="loginForm" method="POST">
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
	       
	      <input type="submit" class="custom-btn" name="login" value="LOGIN" style="width: 100%;">
	     </form>
       <button class="img-btn" style="margin-top: 12px; width: 100%;" disabled>
		     <img class="img-in-btn" src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Facebook_icon_2013.svg/768px-Facebook_icon_2013.svg.png" width="27" height="27" style="margin-bottom: 3px; margin-right: 10px;" alt="" />
		     LOGIN WITH FACEBOOK
		   </button>
	   </div>
		
	</body>
</html>
