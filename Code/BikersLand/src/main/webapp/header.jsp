<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<%
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache"); // HTTP 1.0
			response.setHeader("Expires", "0"); // Proxies
	  %>
	</head>
	
	<body>
	  <%
	    if(request.getParameter("logout") != null)
	    	session.removeAttribute("logged-user-bean");
	  %>
		<div class="header container-fluid">
			<div class="row">
				<div class="col-2" style="margin-left: 40px;">
					<a href="index.jsp"><img src="resources/Logo_White.png" style="width:150px;height:100%;" alt="BikersLand Homepage"></a>
				</div>
				<div class="col-2 align-self-center">
				  <button onclick="location.href='create_event.jsp'" type="button" class="" id="btn_create_event">CREATE EVENT</button>
				</div>
				<div class="col align-self-center">
				  <div class="row justify-content-end" style="margin: 0px; height: 40px;">
				    <% if(session.getAttribute("logged-user-bean") != null) { %>
				    	<button onclick="location.href='profile.jsp'" type="button" style="height: 100%;">YOUR PROFILE</button>
				    	<form action="index.jsp" method="POST" style="height: 100%;">
				    	  <input type="submit" class="custom-btn" value="LOGOUT" name="logout" style="margin-left: 10px; margin-right: 40px; height: 100%;">
				    	</form>
				    <% } else { %>
				      <button onclick="location.href='register.jsp'" type="button" class="">REGISTER</button>
              <button onclick="location.href='login.jsp'" type="button" class="" style="margin-left: 10px; margin-right: 40px;">LOGIN</button>
              <script type="text/javascript">
                document.getElementById("btn_create_event").style.display = 'none';
              </script>
				    <% } %>
		      </div>
				</div>
			</div>
		</div>
	</body>
</html>