package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.model.bo.User;
import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ShowProfileServlet", value = "/show_profile")
public class ShowProfileServlet extends HttpServlet
{
	@Override
	protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = (User) request.getSession().getAttribute("user");

		if (user == null) user = (User) request.getAttribute("user");

		request.setAttribute("user", user);

		if (user != null)
		{
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/showProfile.jsp");
			requestDispatcher.forward(request, response);
		}
		else
		{
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
