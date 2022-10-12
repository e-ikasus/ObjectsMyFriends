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

@WebServlet(name = "ModifyProfileServlet", value = "/modify_profile")
public class ModifyProfileServlet extends HttpServlet
{
	HashMap<Object, String> formParameters = new HashMap<>();

	@Override public void init() throws ServletException
	{
		super.init();

		formParameters.put(ModelError.UNABLE_TO_UPDATE_USER, "genericError");
		formParameters.put(ModelError.INVALID_USER_PSEUDO, "username");
		formParameters.put(ModelError.INVALID_USER_LASTNAME, "lastName");
		formParameters.put(ModelError.INVALID_USER_FIRSTNAME, "firstName");
		formParameters.put(ModelError.INVALID_USER_EMAIL, "email");
		formParameters.put(ModelError.INVALID_USER_PHONE_NUMBER, "phoneNumber");
		formParameters.put(ModelError.INVALID_USER_STREET, "street");
		formParameters.put(ModelError.INVALID_USER_ZIPCODE, "zipCode");
		formParameters.put(ModelError.INVALID_USER_CITY, "city");
		formParameters.put(ModelError.INVALID_USER_PASSWORD, "newPassword");
		formParameters.put(ModelError.INVALID_USER_PASSWORD_TO_LIGHT, "newPassword");
		formParameters.put(ControllerError.PASSWORD_DOESNT_MATCH, "confirmPassword");
		formParameters.put(null, "credit");
	}

	@Override
	protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Retrieve the connected user. At this stage, there is always one because
		// of the filter servlet.
		User user = (User) request.getSession().getAttribute("user");

		// Fill the form with user information.
		request.setAttribute("username", user.getUsername());
		request.setAttribute("lastName", user.getLastName());
		request.setAttribute("firstName", user.getFirstName());
		request.setAttribute("email", user.getEmail());
		request.setAttribute("phoneNumber", user.getPhoneNumber());
		request.setAttribute("street", user.getStreet());
		request.setAttribute("zipCode", user.getZipCode());
		request.setAttribute("city", user.getCity());
		request.setAttribute("credits", user.getCredit());

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/modifyProfile.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Retrieve the connected user. At this stage, there is always one because
		// of the filter servlet.
		User user = (User) request.getSession().getAttribute("user");

		request.setCharacterEncoding("UTF-8");

		// Check if the user confirm the update.
		if (request.getParameter("update") != null)
		{
			// Retrieve received user information.
			String lastName = request.getParameter("lastName");
			String firstName = request.getParameter("firstName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");
			String street = request.getParameter("street");
			String zipCode = request.getParameter("zipCode");
			String city = request.getParameter("city");
			String newPassword = request.getParameter("newPassword");
			String confirmPassword = request.getParameter("confirmPassword");

			try
			{
				if (newPassword.compareTo(confirmPassword) != 0)
					throw new ControllerException(null, ControllerError.PASSWORD_DOESNT_MATCH);

				// Create list that will receive new properties.
				HashMap<String, Object> newProperties = new HashMap<>();

				// Find the modified properties to put into the list.
				if (user.getLastName().compareTo(lastName) != 0) newProperties.put("lastName", lastName);
				if (user.getFirstName().compareTo(firstName) != 0) newProperties.put("firstName", firstName);
				if (user.getEmail().compareTo(email) != 0) newProperties.put("email", email);
				if (user.getPhoneNumber().compareTo(phoneNumber) != 0) newProperties.put("phoneNumber", phoneNumber);
				if (user.getStreet().compareTo(street) != 0) newProperties.put("street", street);
				if (user.getZipCode().compareTo(zipCode) != 0) newProperties.put("zipCode", zipCode);
				if (user.getCity().compareTo(city) != 0) newProperties.put("city", city);
				if (newPassword.length() != 0) newProperties.put("plainPassword", newPassword);

				// Try to update the user.
				UserManager.getInstance().update(user, newProperties);

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

				// Return to the profile update page and display errors.
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/modifyProfile.jsp");
				requestDispatcher.forward(request, response);
			}
		}
		else if (request.getParameter("delete") != null)
		{
			try
			{
				// Try to delete the user by archiving it.
				UserManager.getInstance().delete(user, true);

				// Return to the logout page as the user is disconnected.
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/logOut.jsp");
				requestDispatcher.forward(request, response);
			}
			catch (ModelException exc)
			{
				ControllerSupport controllerSupport = ControllerSupport.getInstance();

				// Transform parameters to attributes for saving the form.
				controllerSupport.saveForm(request, formParameters);

				// Put the error in the form.
				controllerSupport.putFormError((ModelException) exc, request, formParameters);

				// Return to the profile update page and display errors.
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/modifyProfile.jsp");
				requestDispatcher.forward(request, response);
			}
		}
		else
		{
			// Return to the welcome page because the user doesn't create a profile.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
