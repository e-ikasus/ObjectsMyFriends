package fr.eikasus.objectsmyfriends.controller;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LogInFilter", urlPatterns = {"/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class LogInFilter implements Filter
{
	public void init(FilterConfig config) throws ServletException
	{
	}

	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// Determine if the current user is connected or not.
		boolean connected = (httpServletRequest.getSession().getAttribute("user") != null);

		// Url in lower case.
		String url = httpServletRequest.getServletPath().toLowerCase();

		if ( (url.lastIndexOf(".css") != -1) || (url.lastIndexOf(".js") != -1) || (url.lastIndexOf(".html") != -1) || (url.lastIndexOf(".jsp") != -1) )
			chain.doFilter(request, response);
		else if ((url.contains("modify_profile")) && (!connected))
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else if ((url.contains("subscribe")) && (connected))
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else if ((url.contains("logout")) && (!connected))
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else if ((url.contains("login")) && (connected))
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else if ((url.contains("item_sell")) && (!connected))
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else
			chain.doFilter(request, response);
	}
}
