<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.bikersland.exception.InternalDBException"%>
<%@ page import="com.bikersland.controller.application.EventCardControllerApp"%>
<%@ page import="com.bikersland.bean.UserBean"%>
<%@ page import="com.bikersland.controller.application.EventDetailsControllerApp" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.bean.EventBean" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.exception.NoEventParticipantsException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import ="java.io.*"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<title>Event Details</title>
    <%@ include file="header.jsp"%>
    
    <style type="text/css">
      th {
        text-align: right;
      }
      td {
        text-align: left;
      }
      body::before {
        content: "";
        position: absolute;
        top: 112px;
        left: 0;
        width: 100%;
        height: 0px;
        box-shadow: 0px 0 20px 20px rgb(18 18 18);
        z-index: 100;
      }
    </style>
	</head>
	<body>
	  <%
	    if(request.getParameter("event-id") == null || request.getParameter("event-id").strip().equals("")) {
	    	%>
          <script type="text/javascript">
               window.location.href = "index.jsp";
          </script>
        <%
	    } else {
	   	UserBean loggedUserBean = null;
      if(session.getAttribute("logged-user-bean") != null)
        loggedUserBean = (UserBean) session.getAttribute("logged-user-bean");
      
     if(request.getParameter("joinEvent") != null) {
         if(request.getParameter("join_or_remove") != null) {
           if(request.getParameter("join_or_remove").equals("join")) {
             EventCardControllerApp.addUserParticipation(loggedUserBean.getId(), Integer.valueOf(request.getParameter("event-id")));
           } else if (request.getParameter("join_or_remove").equals("remove")) {
             EventCardControllerApp.removeUserParticipation(loggedUserBean.getId(), Integer.valueOf(request.getParameter("event-id")));
           }
         }
       }
	    
	    EventBean eventBean = EventDetailsControllerApp.getEventById(Integer.valueOf(request.getParameter("event-id")));
	    if(eventBean == null) {
	    	%>
         <script type="text/javascript">
              window.location.href = "index.jsp";
         </script>
       <%
	    } else {
	    	Image eventImage = eventBean.getImage();
	    	
	    	if(eventImage == null)
	    		eventImage = EventDetailsControllerApp.getDefaultEventImage();
           
       	BufferedImage buffImg;
        ByteArrayOutputStream bts;
        String imageB64;
        String join_or_remove = "";
        String join_or_remove_hidden = "";
        
        buffImg = SwingFXUtils.fromFXImage(eventImage, null);
        bts = new ByteArrayOutputStream();
        ImageIO.write(buffImg, "png", bts);
        imageB64 = "linear-gradient(rgba(0,0,0,0.65), rgba(0,0,0,0.65)), url('data:image/png;base64," + Base64.getEncoder().encodeToString(bts.toByteArray()) + "')";
        
        %>
        <script type="text/javascript">
          document.body.style.background = "<% out.write(imageB64); %>";
          document.body.style.backgroundSize = "cover";
        </script>
        <%        
	    	
	    	List<String> participantsList;
	    	try {
	    		participantsList = EventDetailsControllerApp.getEventParticipants(eventBean.getId());
	    	} catch (NoEventParticipantsException nepe) {
	    		participantsList = new ArrayList<>();
	    		participantsList.add("Nobody joined this event!");
	    	}
	    	List<String> tagsList = eventBean.getTags();
	  %>
	  
	  <div class="mx-auto parent" style="margin-top: 50px;">
        <form action="event_details.jsp" method="POST">
          <input type="hidden" id="event-id" name="event-id" value="<% out.write(eventBean.getId().toString()); %>">
          
          <p class="headLabel" style="font-size: 45px;"><% out.write(eventBean.getTitle()); %></p>
          
          <table class="table" style="width: 800px;" aria-describedby="">
				  <tbody style="color: white;">
				    <tr>
				      <th scope="row">Departure date</th>
				      <td><% out.write(ConvertMethods.dateToLocalFormat(eventBean.getDepartureDate())); %></td>
				      <td rowspan="8" style="height:100%;">
					      <select multiple class="form-control" size="10" style="width: 250px;" disabled>
                  <%
                    for(String user:participantsList)
                      out.write("<option>" + user + "</option>");
                  %>
                </select>
              </td>
				    </tr>
				    <tr>
				      <th scope="row">Return date</th>
              <td><% out.write(ConvertMethods.dateToLocalFormat(eventBean.getReturnDate())); %></td>
				    </tr>
				    <tr>
				      <th scope="row">Departure city</th>
              <td><% out.write(eventBean.getDepartureCity().toString()); %></td>
				    </tr>
				    <tr>
              <th scope="row">Destination city</th>
              <td><% out.write(eventBean.getDestinationCity().toString()); %></td>
            </tr>
            <tr>
              <th scope="row">Number of participants</th>
              <td>
	              <% 
	                if(participantsList.get(0).equals("Nobody joined this event!"))
	                	out.write("0");
	                else
	                	out.write(String.valueOf(participantsList.size()));
                 %>
              </td>
            </tr>
            <tr>
              <th scope="row">Description</th>
              <td><% out.write(eventBean.getDescription().toString()); %></td>
            </tr>
            <tr>
              <th scope="row">Tags</th>
              <td>
                <select class="form-control" size="5" disabled>
                  <%
                    for(String tag:tagsList)
                    	out.write("<option>" + tag + "</option>");
                  %>
							  </select>
							</td>
            </tr>
            <tr>
              <th scope="row">Created on</th>
              <td><% out.write(ConvertMethods.dateToLocalFormat(eventBean.getCreateTime())); %></td>
            </tr>
            <tr>
              <th scope="row">Created by</th>
              <td><% out.write(eventBean.getOwnerUsername().toString()); %></td>
            </tr>
            <%
              if(loggedUserBean != null) {            	  
            	  out.write("<td colspan=\"3\"><form action=\"event_details.jsp\" method=\"POST\">");
                  try {
                     if(EventCardControllerApp.isJoinedEvent(loggedUserBean.getId(), eventBean.getId())) {
                       join_or_remove = "Remove participation";
                       join_or_remove_hidden = "remove";
                     } else {
                       join_or_remove = "Join";
                       join_or_remove_hidden = "join";
                     }
                        
                  } catch (InternalDBException idbe) {
                    %>
                      <script type="text/javascript">
                         alert("Internal Error, you will be redirected to the homepage");
                      </script>
               <%
                     response.sendRedirect("index.jsp");
                  }
                  out.write("<input type=\"submit\" class=\"custom-btn\" value=\"" + join_or_remove + "\" name=\"joinEvent\" style=\"width:100%\">");
                  out.write("<input type=\"hidden\" id=\"event-id\" name=\"event-id\" value=\"" + eventBean.getId().toString() + "\">");
                  out.write("<input type=\"hidden\" id=\"join_or_remove\" name=\"join_or_remove\" value=\"" + join_or_remove_hidden + "\">");
                  out.write("</form></td>");
              }
            %>
				  </tbody>
					</table>
          
          
        </form>
    </div>
    <% }} %>
	</body>
</html>