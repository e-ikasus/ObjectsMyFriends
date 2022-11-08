package fr.eikasus.objectsmyfriends.controller;

import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogOutServlet", value = "/logout")
public class LogOutServlet extends HttpServlet
{
	@Override
	protected void doGet(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException
	{
		// Invalidate the session to log out the user.
		request.getSession().invalidate();

		// Return to the welcome page.
		response.sendRedirect(request.getContextPath() + "/welcome");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
