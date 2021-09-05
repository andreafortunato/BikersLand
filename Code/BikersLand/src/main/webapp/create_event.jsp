<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%@ page import="com.bikersland.controller.application.NewEventControllerApp" %>
<%@ page import="com.bikersland.controller.application.MainControllerApp" %>
<%@ page import="com.bikersland.utility.ConvertMethods" %>
<%@ page import="com.bikersland.bean.EventBean" %>
<%@ page import="com.bikersland.exception.*" %>
<%@ page import="com.bikersland.exception.event.*" %>
<%@ page import = "java.util.*" %>
<%@ page import ="java.io.*"%>
<%@ page import ="java.awt.image.BufferedImage"%>
<%@ page import ="javafx.embed.swing.SwingFXUtils"%>
<%@ page import ="javax.imageio.ImageIO"%>
<%@ page import ="javafx.scene.image.Image"%>
<%@ page import ="java.time.LocalDate"%>
<%@ page import = "java.time.format.DateTimeFormatter" %>
<%@ page import = "java.sql.Date" %>

<html lang="en">
	<head>
		<meta charset="UTF-8">  
		    <title>Create New Event</title>
		    <%@ include file="header.jsp"%>
	</head>
	<body>
	 <div class="mx-auto parent">
      <form action="create_event.jsp" method="POST">
        <p class="headLabel">Create New Event</p>
        <div class="input-group mb-3" style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <span class="input-group-text" id="basic-addon1"  style="width: 200px">Title</span>
          </div>
          <input type="text" class="form-control" placeholder="Title" id="title" name="title" aria-label="Title" aria-describedby="basic-addon1">
        </div>
        <div class="input-group mb-3" style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <label class="input-group-text" for="departure-city" style="width: 200px">Departure City</label>
          </div>
          <select class="custom-select" id="departure-city" name="departure-city">
            <option selected>Abano Terme (PD)</option>
            <% 
              List<String> cities = null;  
              try{
                 cities = MainControllerApp.getCities(); 
                 for(String city:cities)
                   out.write("<option value=\"" + city + "\">" + city + "</option>");
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
        <div class="input-group mb-3"  style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <label class="input-group-text" for="destination-city" style="width: 200px">Destination City</label>
          </div>
          <select class="custom-select" id="destination-city" name="destination-city">
            <option selected>Abano Terme (PD)</option>
            <%
              for(String city:cities)
                out.write("<option value=\"" + city + "\">" + city + "</option>");
            %>
          </select>
        </div>
        <div class="input-group mb-3"  style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <label class="input-group-text" for="departure-date" style="width: 200px">Departure Date</label>
          </div>
          <%
          LocalDate date =  LocalDate.now();
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          String minDate = date.format(formatter);
      
          out.write("<input type=\"date\" value=\"" + minDate + "\" min=\"" + minDate + "\" id=\"departure-date\" name=\"departure-date\" onchange=\"manageReturnDate(event);\">");
          %>
          
        </div>
        <div class="input-group mb-3"  style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <label class="input-group-text" for="return-date" style="width: 200px">Return Date</label>
          </div>
          <%
            out.write("<input type=\"date\" value=\"" + minDate + "\" min=\"" + minDate + "\" id=\"return-date\" name=\"return-date\">");
          %>
          <script type="text/javascript">
			      function manageReturnDate(e){
			        document.getElementById("return-date").setAttribute("min", e.target.value);
			        document.getElementById('return-date').value = e.target.value;
			      }
			    </script>
          
        </div>
        <div class="input-group mb-3" style="width: 600px">
          <div class="input-group-prepend"  style="width: 200px">
            <span class="input-group-text" id="basic-addon1"  style="width: 200px">Description</span>
          </div>
          <textarea class="form-control" rows="3" placeholder="Insert description here..." id="description" name="description" aria-label="description" aria-describedby="basic-addon1" maxlength="250" required></textarea>
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
				<input type="submit" class="custom-btn" value="CREATE EVENT" name="createEvent" style="width:100%">
      </form>
   </div>
   
   <%
     EventBean eventBean = new EventBean();
   
     if(request.getParameter("createEvent") != null) {
		   try {
		       eventBean.setTitle(request.getParameter("title"));
	         eventBean.setDescription(request.getParameter("description"));
	         eventBean.setOwnerUsername("Galaxy");
	         eventBean.setDepartureCity(request.getParameter("departure-city"));
	         eventBean.setDestinationCity(request.getParameter("destination-city"));
	         eventBean.setDepartureDate(java.sql.Date.valueOf(request.getParameter("departure-date")));
	         eventBean.setReturnDate(java.sql.Date.valueOf(request.getParameter("return-date")));
	         if(request.getParameterValues("tags-list") == null)
	        	 eventBean.setTags(new ArrayList<>());
	         else
	        	 eventBean.setTags(Arrays.asList(request.getParameterValues("tags-list")));
	         
	         EventBean createdEventBean = NewEventControllerApp.createNewEvent(eventBean);
	         
	         %>
	           <form action="event_details.jsp" method="POST" id="form_details">
	             <input type="hidden" id="event-id" name="event-id" value="<% out.write(createdEventBean.getId().toString()); %>">
	           </form>
	           
		         <script type="text/javascript">
	                alert("<% out.write("You will be redirected on event details of your event " + createdEventBean.getTitle()); %>");
	                
	                document.getElementById("form_details").submit();
	           </script>
	         <%
		   } catch (TitleException te) {
			   %>
         <script type="text/javascript">
             alert("<% out.write(te.getMessage()); %>");
         </script>
         <%
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