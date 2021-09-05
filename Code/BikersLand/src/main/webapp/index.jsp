<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page errorPage="error.jsp" %>

<%@ page import="com.bikersland.bean.UserBean"%>
<%@ page import="com.bikersland.controller.application.EventCardControllerApp"%>
<%@ page import="com.bikersland.controller.application.MainControllerApp" %>
<%@ page import="com.bikersland.controller.application.HomepageControllerApp" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.bean.EventBean" %>
<%@ page import="com.bikersland.exception.InternalDBException" %>
<%@ page import ="java.util.*"%>
<%@ page import ="java.io.*"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>

 
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">	
		<title>BikersLand Homepage</title>
		<%@ include file="header.jsp"%> 
	</head>
	
	<body>
	  <%
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
	  
	  if(request.getParameter("favoriteEvent") != null) {
      if(request.getParameter("join_or_remove") != null) {
        if(request.getParameter("join_or_remove").equals("join")) {
          EventCardControllerApp.addFavoriteEvent(loggedUserBean.getId(), Integer.valueOf(request.getParameter("event-id")));
        } else if (request.getParameter("join_or_remove").equals("remove")) {
          EventCardControllerApp.removeFavoriteEvent(loggedUserBean.getId(), Integer.valueOf(request.getParameter("event-id")));
        }
      }
    }
		  
	  %>
	  <div class="mx-auto parent">
	    <form action="index.jsp" method="POST">
				<div class="input-group mb-3" style="width: 600px">
				  <div class="input-group-prepend"  style="width: 200px">
				    <label class="input-group-text" for="departure-city" style="width: 200px">Departure City</label>
				  </div>
				  <select class="custom-select" id="departure-city" name="departure-city">
				    <option selected>All</option>
				    <% 
				      List<String> cities = null;
					    try {
					    	 cities = MainControllerApp.getCities(); 
					       for(String city:cities)
					         out.write("<option value=\"" + city + "\">" + city + "</option>");
					       } catch(InternalDBException idbe) {
				    %>
				           <script type="text/javascript">
				              alert("Internal Error, you will be redirected to the homepage");
		               </script>
		            <%
		                  response.sendRedirect("index.jsp");
				       }
				    %>
				    
				  </select>
			  </div>
		    <div class="input-group mb-3"  style="width: 600px">
				  <div class="input-group-prepend"  style="width: 200px">
				    <label class="input-group-text" for="destination-city" style="width: 200px">Destination City</label>
				  </div>
				  <select class="custom-select" id="destination-city" name="destination-city">
				    <option selected>All</option>
					    <%
						    for(String city:cities)
						    out.write("<option value=\"" + city + "\">" + city + "</option>");
					    %>
				  </select>
				</div>
				<div class="form-group">
			    <label style="text-align: center;">Tags (Hold CTRL to multiple select)</label>
			    <select multiple class="form-control" size="5" id="tags-list" name="tags-list">
			      <% 
	            List<String> tags = null;  
	            try{
	               tags = MainControllerApp.getTags(); 
	               for(String tag:tags)
	                 out.write("<option value=\"" + tag + "\">" + tag + "</option>");
	               }catch(InternalDBException idbe){
	          %>
	                 <script type="text/javascript">
	                   alert("Internal Error, you will be redirected to the homepage");
	                 </script>
	            <%
	                   response.sendRedirect("index.jsp");
	             }
	          %>
			    </select>
			  </div>
				<input type="submit" class="custom-btn" value="SEARCH" name="search" style="width:100%">
			</form>
	  </div>
	  
	  <%	  
        List<EventBean> searchedEventList = new ArrayList<>();
        
        try {
          if(request.getParameter("search") == null)
              searchedEventList = HomepageControllerApp.getEventByCitiesAndTags("All","All", new ArrayList<>());
          else{
            if(request.getParameterValues("tags-list") == null)
              searchedEventList = HomepageControllerApp.getEventByCitiesAndTags(request.getParameter("departure-city"),request.getParameter("destination-city"),new ArrayList<>());
            else
              searchedEventList = HomepageControllerApp.getEventByCitiesAndTags(request.getParameter("departure-city"),request.getParameter("destination-city"),Arrays.asList(request.getParameterValues("tags-list")));
          }
        } catch (InternalDBException idbe) {
        	%>
            <script type="text/javascript">
              alert("Internal Error, you will be redirected to the homepage");
            </script>
     <%
              response.sendRedirect("index.jsp");
        }
     
        Integer rows = (int)(searchedEventList.size() + 3 -1)/3;
        EventBean event;
        BufferedImage buffImg;
        ByteArrayOutputStream bts;
        String imageB64;
        Image eventImage;
        String join_or_remove = "";
        String join_or_remove_hidden = "";
        
        for(int i = 0;i < rows; i++){
        	out.write("<div class=\"card-group\">");
        	
        	for(int j = 0; j < 3; j++){
        		if(searchedEventList.size() == 0){
        			break;
        		}
        		
        		event = searchedEventList.remove(0);
        		eventImage = event.getImage();
        		if(eventImage == null)
        			eventImage = EventCardControllerApp.getDefaultEventImage();
        		
        		out.write("<div class=\"card card-border\" style=\"margin: 10px;\">");
        			
 	            buffImg = SwingFXUtils.fromFXImage(eventImage, null);
 	            bts = new ByteArrayOutputStream();
 	            ImageIO.write(buffImg, "png", bts);
 	            imageB64 = Base64.getEncoder().encodeToString(bts.toByteArray());
 	            
 	            out.write("<form action=\"event_details.jsp\" method=\"POST\" style=\"margin-bottom: 0px !important;\">");
              out.write("<input type=\"hidden\" id=\"event-id\" name=\"event-id\" value=\"" + event.getId().toString() + "\">");
 	            out.write("<input type=\"image\" class=\"card-img-top\" style=\"margin-bottom: -8px; max-height:300px; object-fit: cover; border-radius: 5px 5px 0px 0px;\" src=\"data:image/png;base64," + imageB64 + "\" alt=\"Card image cap\" />");
 	            out.write("</form>");
        		 
        		  out.write("<div class=\"card-body\" style=\"background-color: #121212; border-radius: 0px 0px 5px 5px; padding: 5px !important;\">");
        		    out.write("<h5 class=\"card-title\" style=\"text-align: center;\">" + event.getTitle() +"</h5>");
        		    
        		    out.write("<table class=\"table table-borderless\" style=\"color: white; text-align: left;\">");
	      	        out.write("<tbody>");
		      	        out.write("<tr>");
			      	        out.write("<td>" + event.getDepartureCity() + "</td>");
			      	        out.write("<td>" + ConvertMethods.dateToLocalFormat(event.getDepartureDate()) + "</td>");
		      	        out.write("</tr>");
			      	      out.write("<tr>");
                      out.write("<td>" + event.getDestinationCity() + "</td>");
                      out.write("<td>" + ConvertMethods.dateToLocalFormat(event.getReturnDate()) + "</td>");
                    out.write("</tr>");
                    out.write("<tr>");
	                    out.write("<td rowspan=\"2\" style=\"width: 50%;\">");
	                      if(event.getTags().isEmpty())
	                    	  out.write("No tags!");
	                      else
	                    	  out.write(String.join(", ", event.getTags()));
	                    out.write("</td>");
	                    out.write("<td rowspan=\"2\">Created by " + event.getOwnerUsername() + "</td>");
	                  out.write("</tr>");
	                  out.write("<tr>");
	                  out.write("</tr>");
	                  if(loggedUserBean != null) {
		                  out.write("<tr>");
	                      out.write("<td><form action=\"index.jsp\" method=\"POST\">");
	                      try {
                           if(EventCardControllerApp.isJoinedEvent(loggedUserBean.getId(), event.getId())) {
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
	                      out.write("<input type=\"hidden\" id=\"event-id\" name=\"event-id\" value=\"" + event.getId().toString() + "\">");
	                      out.write("<input type=\"hidden\" id=\"join_or_remove\" name=\"join_or_remove\" value=\"" + join_or_remove_hidden + "\">");
	                      out.write("</form></td>");
	                      
	                      out.write("<td><form action=\"index.jsp\" method=\"POST\">");
	                        try {
	                           if(EventCardControllerApp.isFavoriteEvent(loggedUserBean.getId(), event.getId())) {
	                             join_or_remove = "Remove from favorites";
	                             join_or_remove_hidden = "remove";
	                           } else {
	                             join_or_remove = "Add to favorites";
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
	                        out.write("<input type=\"submit\" class=\"custom-btn\" value=\"" + join_or_remove + "\" name=\"favoriteEvent\" style=\"width:100%\">");
	                        out.write("<input type=\"hidden\" id=\"event-id\" name=\"event-id\" value=\"" + event.getId().toString() + "\">");
	                        out.write("<input type=\"hidden\" id=\"join_or_remove\" name=\"join_or_remove\" value=\"" + join_or_remove_hidden + "\">");
	                        out.write("</form></td>");
	                    out.write("</tr>");
	                  }
                    
	      	        out.write("</tbody>");
       	        out.write("</table>");
        		    
        		  out.write("</div>");
        		out.write("</div>");
        	}
        	
        	out.write("</div>");
        }
        out.write("<p>"+ String.valueOf(rows) + "</p>");
      
    %>
	</body>
</html>
