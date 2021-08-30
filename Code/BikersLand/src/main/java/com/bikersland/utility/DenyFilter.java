package com.bikersland.utility;

//Import required java libraries
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

//Implements Filter class
public class DenyFilter implements Filter  {
	
	public void  init(FilterConfig config) throws ServletException {}
	
	public void  doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
	   throws java.io.IOException, ServletException {
	   
	  /*
	   HttpServletResponse resp = (HttpServletResponse)response;
	   resp.sendRedirect("index.jsp");
	   
	   System.out.println("Prova ok");
	  */
		   
	   // Pass request back down the filter chain
	 //  chain.doFilter(request,response);
	}
	
	public void destroy( ) {}
}
