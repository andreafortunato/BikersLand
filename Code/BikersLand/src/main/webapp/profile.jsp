<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.bikersland.controller.application.EventCardControllerApp"%>
<%@ page import="com.bikersland.bean.UserBean"%>
<%@ page import="javafx.scene.image.Image"%>
<%@ page import="com.bikersland.controller.application.ProfileControllerApp"%>
<%@ page import ="java.io.*"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>
<%@ page import ="java.util.List"%>
<%@ page import ="java.util.ArrayList"%>
<%@ page import="java.util.Base64" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.bean.EventBean" %>
<%@ page import="com.bikersland.exception.InternalDBException" %>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>



<!DOCTYPE html>
<html lang="en">
	<head>
	  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	  
		<meta charset="UTF-8">
		<title>Your profile</title>
    <%@ include file="header.jsp"%>
	</head>
    
	<body>
    <%
      if(session.getAttribute("logged-user-bean") == null) {
        %>
          <script type="text/javascript">
               window.location.href = "index.jsp";
          </script>
        <%
      } else {
      
      UserBean userBean = (UserBean) session.getAttribute("logged-user-bean");
      
      if(userBean == null) {
        %>
         <script type="text/javascript">
              window.location.href = "index.jsp";
         </script>
       <%
      } else {
    	  
        if(request.getParameter("joinEvent") != null) {
          if(request.getParameter("join_or_remove") != null) {
            if(request.getParameter("join_or_remove").equals("join")) {
              EventCardControllerApp.addUserParticipation(userBean.getId(), Integer.valueOf(request.getParameter("event-id")));
            } else if (request.getParameter("join_or_remove").equals("remove")) {
              EventCardControllerApp.removeUserParticipation(userBean.getId(), Integer.valueOf(request.getParameter("event-id")));
            }
          }
        }
        
        if(request.getParameter("favoriteEvent") != null) {
          if(request.getParameter("join_or_remove") != null) {
            if(request.getParameter("join_or_remove").equals("join")) {
              EventCardControllerApp.addFavoriteEvent(userBean.getId(), Integer.valueOf(request.getParameter("event-id")));
            } else if (request.getParameter("join_or_remove").equals("remove")) {
              EventCardControllerApp.removeFavoriteEvent(userBean.getId(), Integer.valueOf(request.getParameter("event-id")));
            }
          }
        }
    	  
        Image eventImage = userBean.getImage();
        
        if(eventImage == null)
          eventImage = ProfileControllerApp.getDefaultUserImage();
           
        BufferedImage buffImg;
        ByteArrayOutputStream bts;
        String imageB64;
        
        buffImg = SwingFXUtils.fromFXImage(eventImage, null);
        bts = new ByteArrayOutputStream();
        ImageIO.write(buffImg, "png", bts);
        imageB64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bts.toByteArray());
    %>
    <div class="mx-auto parent" style="margin-top: 50px;">
      <p class="headLabel"><% out.write(userBean.getUsername()); %>'s profile</p>
      <table class="table table-borderless" style="color: white; text-align: left;" aria-describedby="">
			  <tbody style="font-size: 20px;">
			    <tr>
			      <td rowspan="3" style="height: 100%; text-align: center; vertical-align: middle;">
			        <img src="<% out.write(imageB64); %>" width="100px" height="100px" alt="Profile image">
			      </td>
			      <th scope="row" style="padding: 0px !important;"><% out.write(userBean.getName()); %></th>
			    </tr>
			    <tr>
			      <th scope="row" style="padding: 0px !important;"><% out.write(userBean.getSurname()); %></th>
			    </tr>
			    <tr>
			      <th scope="row" style="padding: 0px !important;"><% out.write(userBean.getEmail()); %></th>
			    </tr>
			  </tbody>
			</table>
		</div>
			
			<br><br><br>
			
			<ul class="tabs">
			  <li data-tab-target="#joined_events" class="active tab">Joined events</li>
			  <li data-tab-target="#favorite_events" class="tab">Favorite events</li>
			</ul>
			
			<div class="tab-content">
			  <div id="joined_events" data-tab-content class="active">
			    <%   
		        List<EventBean> joinedEventBeanList = new ArrayList<>();
		        
		        try {
		        	joinedEventBeanList = ProfileControllerApp.getJoinedEventsByUser(userBean.getId());
		        } catch (InternalDBException idbe) {
		          %>
		            <script type="text/javascript">
		              alert("Internal Error, you will be redirected to the homepage");     
		            </script>
	            <%
	                response.sendRedirect("index.jsp");
		        }
		     
		        Integer rows = (int)(joinedEventBeanList.size() + 3 -1)/3;
		        EventBean event;
		        String join_or_remove = "";
		        String join_or_remove_hidden = "";
		        
		        for(int i = 0;i < rows; i++){
		          out.write("<div class=\"card-group\">");
		          
		          for(int j = 0; j < 3; j++){
		            if(joinedEventBeanList.size() == 0){
		              break;
		            }
		            
		            event = joinedEventBeanList.remove(0);
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
		                    out.write("<tr>");
		                      out.write("<td><form action=\"profile.jsp\" method=\"POST\">");
		                      try {
                                if(EventCardControllerApp.isJoinedEvent(userBean.getId(), event.getId())) {
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
		                      
		                      out.write("<td><form action=\"profile.jsp\" method=\"POST\">");
                          try {
                             if(EventCardControllerApp.isFavoriteEvent(userBean.getId(), event.getId())) {
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
		                  out.write("</tbody>");
		                out.write("</table>");
		                
		              out.write("</div>");
		            out.write("</div>");
		          }
		          
		          out.write("</div>");
		        }		      
		    %>
			  </div>
			  <div id="favorite_events" data-tab-content>
			    <%   
            List<EventBean> favoriteEventBeanList = new ArrayList<>();
            
            try {
            	favoriteEventBeanList = ProfileControllerApp.getFavoriteEventsByUser(userBean.getId());
            } catch (InternalDBException idbe) {
              %>
                <script type="text/javascript">
                  alert("Internal Error, you will be redirected to the homepage");
                </script>
         <%
                  response.sendRedirect("index.jsp");
            }
         
            rows = (int)(favoriteEventBeanList.size() + 3 -1)/3;
            
            for(int i = 0;i < rows; i++){
              out.write("<div class=\"card-group\">");
              
              for(int j = 0; j < 3; j++){
                if(favoriteEventBeanList.size() == 0){
                  break;
                }
                
                event = favoriteEventBeanList.remove(0);
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
                        out.write("<tr>");
                          out.write("<td><form action=\"profile.jsp\" method=\"POST\">");
                          try {
	                              if(EventCardControllerApp.isJoinedEvent(userBean.getId(), event.getId())) {
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
                          
                          out.write("<td><form action=\"profile.jsp\" method=\"POST\">");
                          try {
                             if(EventCardControllerApp.isFavoriteEvent(userBean.getId(), event.getId())) {
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
                      out.write("</tbody>");
                    out.write("</table>");
                    
                  out.write("</div>");
                out.write("</div>");
              }
              
              out.write("</div>");
            }          
        %>
			  </div>
			</div>
    <% }} %>
    
    <script type="text/javascript">
	    const tabs = document.querySelectorAll('[data-tab-target]')
	    const tabContents = document.querySelectorAll('[data-tab-content]')
	
	    tabs.forEach(tab => {
	      tab.addEventListener('click', () => {
	        const target = document.querySelector(tab.dataset.tabTarget)
	        tabContents.forEach(tabContent => {
	          tabContent.classList.remove('active')
	        })
	        tabs.forEach(tab => {
	          tab.classList.remove('active')
	        })
	        tab.classList.add('active')
	        target.classList.add('active')
	      })
	    })
    </script>
	</body>
</html>