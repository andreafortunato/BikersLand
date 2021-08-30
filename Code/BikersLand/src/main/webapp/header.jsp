<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	
	<body>
		<div class="header container-fluid">
			<div class="row">
				<div class="col-2" style="margin-left: 40px;">
					<a href="index.jsp"><img src="resources/Logo_White.png" style="width:150px;height:100%;" alt="BikersLand Homepage"></a>
				</div>
				<div class="col-2 align-self-center">
				  <button onclick="location.href='create_event.jsp'" type="button" class="" id="btn_create_event">CREATE EVENT</button>
				</div>
				<div class="col align-self-center">
				  <div class="row justify-content-end" style="margin: 0px;">
				    <% if(/*utente loggato*/false) { %>
				    	<button onclick="location.href='profile.jsp'" type="button" class="">YOUR PROFILE</button>
				    	<button onclick="esegui logout" type="button" class="" style="margin-left: 10px; margin-right: 40px;">LOGOUT</button>
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