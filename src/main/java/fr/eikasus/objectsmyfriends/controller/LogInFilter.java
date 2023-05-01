package fr.eikasus.objectsmyfriends.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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

	@Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// URL in lower case.
		String url = httpServletRequest.getServletPath().toLowerCase();

		// Determine if the current user is connected or not.
		boolean connected = (httpServletRequest.getSession().getAttribute("user") != null);

		// Determine the target of the request.
		boolean resource = (url.lastIndexOf(".jsp") != -1);
		boolean modifyProfile = (url.contains("modify_profile") && resource);
		boolean subscribe = (url.contains("subscribe") && resource);
		boolean logout = (url.contains("logout") && resource);
		boolean login = (url.contains("login") && resource);
		boolean itemSell = (url.contains("item_sell") && resource);
		boolean showProfile = (url.contains("show_profile") && resource);

		// Is the target reachable.
		boolean redirect = (connected && (subscribe || login)) || ((!connected) && (logout || itemSell || showProfile || modifyProfile));

		if (redirect) httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/welcome");
		else chain.doFilter(request, response);
	}
}
