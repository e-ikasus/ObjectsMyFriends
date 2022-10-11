package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.model.bll.UserManager;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogInServlet", value = "/login")
public class LogInServlet extends HttpServlet
{
	// Time of the cookie life in seconds.
	private static final int USERNAME_COOKIE_AGE = 60 * 60 * 24 * 7;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/logIn.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");

		// Check if the user confirm the connexion or not.
		if (request.getParameter("confirm") != null)
		{
			// Retrieve received user information.
			String input = request.getParameter("pseudo");
			String password = request.getParameter("password");
			String username = null;
			String email = null;

			// Determine how the user want to log in (username or email).
			if (input.indexOf('@') >= 0) email = input;
			else username = input;

			try
			{
				// Try to find the user in the database.
				User user = UserManager.getInstance().find(username, email, password);

				// Success, so save it to the user session.
				HttpSession session = request.getSession();
				session.setAttribute("user", user);

				// Because of success, store the username in a cookie if asked.
				if (request.getParameter("rememberMe") != null)
				{
					// Create a cookie with user entered identifier.
					Cookie cookie = new Cookie("username", input);

					// Life cookie should not exceed a few days.
					cookie.setMaxAge(USERNAME_COOKIE_AGE);

					// Send the cookie to the browser.
					response.addCookie(cookie);
				}

				// Return to the welcome page with a connected user.
				response.sendRedirect(request.getContextPath() + "/welcome");
			}
			catch (ModelException me)
			{
				request.setAttribute("error", "error");

				// Return to the connexion page and display error.
				doGet(request, response);
			}
		}
		else
		{
			// Return to the welcome page because the user doesn't connect to.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
