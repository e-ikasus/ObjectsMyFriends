package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogInServlet", value = "/login")
public class LogInServlet extends HttpServlet
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	// Time of the cookie life in seconds.
	private static final int USERNAME_COOKIE_AGE = 60 * 60 * 24 * 7;

	@Inject
	ManagerFactory managerFactory;

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Display the login form.
	 * <p>
	 * This method display the login form. No parameter is required to do this
	 * operation. The state of the "Remember me" button is updated in the JSP
	 * according to the presence of the appropriate cookie. Due to the filter
	 * servlet, this method can't be called when a user is already connected.
	 */

	@Override protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/logIn.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Log in a user.
	 * <p>
	 * This method try to connect a user whose login information are supplied in
	 * parameter. If this user is found in the database, its instance is saved in
	 * the session, otherwise the form is displayed again by calling the doGet
	 * method. Once connected if the "Remember me" button is checked, his username
	 * is saved in a cookie. The state of this button always depends on the
	 * presence of the cookie, and vice versa. Due to the filter servlet, this
	 * method can't be called when a user is already connected.
	 */

	@Override protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
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
				User user = managerFactory.getUserManager().find(username, email, password);

				// Success, so save it to the user session.
				request.getSession().setAttribute("user", user);

				// Create a cookie with user entered identifier.
				Cookie cookie = new Cookie("username", input);

				// Life cookie should not exceed a few days or be null if not wanted.
				cookie.setMaxAge((request.getParameter("rememberMe") != null) ? (USERNAME_COOKIE_AGE) : (0));

				// Send the cookie to the browser.
				response.addCookie(cookie);

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
