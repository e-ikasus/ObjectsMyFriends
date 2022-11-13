package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ShowProfileServlet", value = "/show_profile")
public class ShowProfileServlet extends HttpServlet
{
	@Inject
	ManagerFactory managerFactory;

	/**
	 * Show user profile.
	 * <p></p>
	 * This method is used to display a user profile. If an identifier is supplied
	 * to the request, then the profile of the user whose identifier match is
	 * displayed. If none is supplied, then this is the current user profile which
	 * is shown. If no user is connected, the request is redirected to the welcome
	 * page.
	 */

	@Override protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long identifier;
		User user = null;

		// If an identifier is supplied, find the corresponding user.
		if ((identifier = ControllerSupport.parseLongParameter(request, "identifier")) != 0)
		{
			try
			{
				// Retrieve the user.
				user = managerFactory.getUserManager().find(identifier).get(0);
			}
			catch (ModelException me)
			{
				// Do nothing.
			}
		}
		else
		{
			// Take the current used.
			user = (User) request.getSession().getAttribute("user");
		}

		// Save the user to be used by the JSP.
		request.setAttribute("user", user);

		// If a user is found, display his profile
		if (user != null)
		{
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/showProfile.jsp");
			requestDispatcher.forward(request, response);
		}
		else
		{
			// None is found, so return to the welcome page.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}

	/**
	 * Handle actions from the user.
	 * <p></p>
	 * This method is used to handle actions like "return" and "modify" from the
	 * show profile form. A user can only modify his profile, that is when it is
	 * log in and the identifier supplied with the request is whose of the user or
	 * if the user is log in and no identifier is supplied.
	 */

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long identifier = ControllerSupport.parseLongParameter(request, "identifier");
		User currentUser = (User) request.getSession().getAttribute("user");

		if ((currentUser != null) && ((identifier == 0) || (identifier == currentUser.getIdentifier())) && (request.getParameter("modify") != null))
		{
			// The user want to modify his profile and has the right to do that.
			response.sendRedirect(request.getContextPath() + "/modify_profile");
		}
		else
		{
			// Return to the welcome page in others cases.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
