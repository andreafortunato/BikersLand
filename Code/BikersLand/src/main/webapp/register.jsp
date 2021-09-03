<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@ page import="com.bikersland.controller.application.RegisterControllerApp" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.bean.UserBean" %>
<%@ page import="com.bikersland.exception.*" %>
<%@ page import="com.bikersland.exception.user.*" %>
<%@ page import ="java.util.*"%>
<%@ page import ="java.io.*"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>
<%@ page import ="java.time.LocalDate"%>
<%@ page import = "java.time.format.DateTimeFormatter" %>

<html lang="en">
 <head>
    <meta charset="UTF-8">  
        <title>Register User</title>
        <%@ include file="header.jsp"%>
  </head>
	<body>
		<div class="mx-auto parent">
	      <form action="register.jsp" method="POST">
	        <p class="headLabel">Create New Account</p>
	        <div class="input-group mb-3" style="width: 600px">
	          <input type="text" class="form-control" style="margin-right: 10px" placeholder="Name" id="name" name="name" aria-label="name" aria-describedby="basic-addon1" <% if(request.getParameter("name") != null) out.write("value=\"" + request.getParameter("name") + "\""); %>>
	          <input type="text" class="form-control" placeholder="Surname" id="surname" name="surname" aria-label="surname" aria-describedby="basic-addon1" <% if(request.getParameter("surname") != null) out.write("value=\"" + request.getParameter("surname") + "\""); %>>
	        </div>
	        <div class="input-group mb-3" style="width: 600px">
	          <div class="input-group-prepend"  style="width: 200px">
	            <span class="input-group-text" id="basic-addon1"  style="width: 200px">Username</span>
	          </div>
	          <input type="text" class="form-control" placeholder="Username" id="username" name="username" aria-label="username" aria-describedby="basic-addon1" <% if(request.getParameter("username") != null) out.write("value=\"" + request.getParameter("username") + "\""); %>>
          </div>
          <div class="input-group mb-3" style="width: 600px; margin-top: 15px;">
            <div class="input-group-prepend"  style="width: 200px">
              <span class="input-group-text" id="basic-addon1"  style="width: 200px">Email</span>
            </div>
            <input type="text" class="form-control" placeholder="Email" id="email" name="email" aria-label="email" aria-describedby="basic-addon1" <% if(request.getParameter("email") != null) out.write("value=\"" + request.getParameter("email") + "\""); %>>
          </div>
          <div class="input-group mb-3" style="width: 600px">
            <div class="input-group-prepend"  style="width: 200px">
              <span class="input-group-text" id="basic-addon1"  style="width: 200px">Email Confirmation</span>
            </div>
            <input type="text" class="form-control" placeholder="Re-enter the mail please..." id="email-confirmation" name="email-confirmation" aria-label="email-confirmation" aria-describedby="basic-addon1" <% if(request.getParameter("email-confirmation") != null) out.write("value=\"" + request.getParameter("email-confirmation") + "\""); %>>
          </div>
          <div class="input-group mb-3" style="width: 600px">
	          <div class="input-group-prepend" style="width: 200px">
	            <span class="input-group-text" id="basic-addon1" style="width: 200px">Password</span>
	          </div>
	          <input type="password" class="form-control" placeholder="Password" id="password" name="password" aria-label="Password" aria-describedby="basic-addon1">
	        </div>
	        <div class="input-group mb-3" style="width: 600px">
            <div class="input-group-prepend" style="width: 200px">
              <span class="input-group-text" id="basic-addon1" style="width: 200px">Confirm Password</span>
            </div>
            <input type="password" class="form-control" placeholder="Re-enter the Password please..." id="confirmation-password" name="confirmation-password" aria-label="Confirmation Password" aria-describedby="basic-addon1">
          </div>
          <input type="submit" class="custom-btn" value="REGISTER" name="registerUser" style="width:100%">
	      </form>
	  </div>
	  
	  <%
	      UserBean userBean = new UserBean();
	      
	      if(request.getParameter("registerUser") != null) {
	    	  if(request.getParameter("password") != null && request.getParameter("confirmation-password") != null) {
		    	  if(!request.getParameter("password").equals(request.getParameter("confirmation-password"))) {
		    		  %>
		    		  <script type="text/javascript">
                alert("<% out.write("The two passwords you entered do not match.\\n\\nPlease try again..."); %>");
              </script>
              <%
		    	  }
	    	  }
	    	  
	    	  try {
	    	      userBean.setName(request.getParameter("name"));
	    	      userBean.setSurname(request.getParameter("surname"));
	    	      userBean.setUsername(request.getParameter("username"));
	    	      userBean.setEmail(request.getParameter("email"));
	    	      userBean.setPassword(request.getParameter("password"));
	    	      
	    	      RegisterControllerApp.register(userBean);
	    	      
	    	      response.sendRedirect("index.jsp");
    	    } catch (DuplicateUsernameException due) {
    	    	%>
    	    	<script type="text/javascript">
                alert("<% out.write(due.getMessage()); %>");
            </script>
            <%
    	    } catch (DuplicateEmailException dee) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(dee.getMessage()); %>");
            </script>
            <%
    	    } catch (NameException ne) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(ne.getMessage()); %>");
            </script>
            <%
    	    } catch (SurnameException se) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(se.getMessage()); %>");
            </script>
            <%
    	    } catch (UsernameException ue) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(ue.getMessage()); %>");
            </script>
            <%
    	    } catch (EmailException ee) {
            %>
            <script type="text/javascript">
                alert("<% out.write(ee.getMessage()); %>");
            </script>
            <%
          } catch (PasswordException pe) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(pe.getMessage()); %>");
            </script>
            <%
    	    } catch (AutomaticLoginException ale) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(ale.getMessage()); %>");
            </script>
            <%
    	      
            response.sendRedirect("login.jsp");
    	    } catch (InternalDBException idbe) {
    	    	%>
            <script type="text/javascript">
                alert("<% out.write(idbe.getMessage()); %>");
            </script>
            <%
	    	  }
	      }
    %>
	</body>
</html>