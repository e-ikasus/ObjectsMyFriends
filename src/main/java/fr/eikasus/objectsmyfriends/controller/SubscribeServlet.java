package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerError;
import fr.eikasus.objectsmyfriends.misc.ControllerException;
import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.UserManager;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "SubscribeServlet", value = "/subscribe")
public class SubscribeServlet extends HttpServlet
{
	HashMap<Object, String> formParameters = new HashMap<>();

	@Override public void init() throws ServletException
	{
		super.init();

		formParameters.put(ModelError.UNABLE_TO_CREATE_USER, "username");
		formParameters.put(ModelError.INVALID_USER_PSEUDO, "username");
		formParameters.put(ModelError.INVALID_USER_LASTNAME, "lastName");
		formParameters.put(ModelError.INVALID_USER_FIRSTNAME, "firstName");
		formParameters.put(ModelError.INVALID_USER_EMAIL, "email");
		formParameters.put(ModelError.INVALID_USER_PHONE_NUMBER, "phoneNumber");
		formParameters.put(ModelError.INVALID_USER_STREET, "street");
		formParameters.put(ModelError.INVALID_USER_ZIPCODE, "zipCode");
		formParameters.put(ModelError.INVALID_USER_CITY, "city");
		formParameters.put(ModelError.INVALID_USER_PASSWORD, "password");
		formParameters.put(ModelError.INVALID_USER_PASSWORD_TO_LIGHT, "password");
		formParameters.put(ControllerError.PASSWORD_DOESNT_MATCH, "confirmPassword");
	}

	@Override
	protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/subscribe.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");

		// Check if the user confirm the connexion or not.
		if (request.getParameter("confirm") != null)
		{
			// Retrieve received user information.
			String username = request.getParameter("username");
			String lastName = request.getParameter("lastName");
			String firstName = request.getParameter("firstName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String street = request.getParameter("street");
			String zipCode = request.getParameter("zipCode");
			String city = request.getParameter("city");
			String password = request.getParameter("password");
			String confirmPassword = request.getParameter("confirmPassword");

			try
			{
				if (password.compareTo(confirmPassword) != 0)
					throw new ControllerException(null, ControllerError.PASSWORD_DOESNT_MATCH);

				// Try to create a user.
				User user = UserManager.getInstance().add(username, firstName, lastName, email, phoneNumber, street, zipCode, city, password, 0, false);

				// Success, so save it to the user session.
				HttpSession session = request.getSession();
				session.setAttribute("user", user);

				// Return to the welcome page with a connected user.
				response.sendRedirect(request.getContextPath() + "/welcome");
			}
			catch (ModelException | ControllerException exc)
			{
				ControllerSupport controllerSupport = ControllerSupport.getInstance();

				// Transform parameters to attributes for saving the form.
				controllerSupport.saveForm(request, formParameters);

				// Put the error in the form.
				if (exc instanceof ModelException)
					controllerSupport.putFormError((ModelException) exc, request, formParameters);
				else
					controllerSupport.putFormError((ControllerException) exc, request, formParameters);

				// Return to the profile creation page and display errors.
				doGet(request, response);
			}
		}
		else
		{
			// Return to the welcome page because the user doesn't create a profile.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
