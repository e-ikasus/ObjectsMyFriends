package fr.eikasus.objectsmyfriends.misc;

import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Class used to simplify handling HTML form.
 * <p></p>
 * This class is used to minimize code repetition for managing HTML form and
 * then simplify the controllers code. For exemple, this class give access to
 * method for simplify error handling in forms.
 *
 * @see #getInstance()
 * @see #saveForm(HttpServletRequest, HashMap) saveForm()
 * @see #putFormError(ModelException, HttpServletRequest, HashMap)
 * putFormError()
 * @see #putFormError(ControllerException, HttpServletRequest, HashMap)
 * putFormError()
 */

public class ControllerSupport
{
	/* ************* */
	/* Class members */
	/* ************* */

	private static ControllerSupport instance = null;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Private constructor of the class.
	 */

	private ControllerSupport()
	{

	}

	/**
	 * Get the instance of the class.
	 * <p>
	 * This method instantiate the class and return-it. This is the only way to
	 * obtain such instance, because the class can't be instanced directly.
	 *
	 * @return Unique instance of the class.
	 */

	public static ControllerSupport getInstance()
	{
		if (instance == null) instance = new ControllerSupport();

		return instance;
	}

	/**
	 * Save an HTML form content.
	 * <p>
	 * This method copy the received form parameters form the supplied request to
	 * their corresponding attribute to allow the form to be populated with user
	 * entered data in case of problem. This will prevent the user to enter the
	 * same information when they are good.
	 *
	 * @param request    Request to deal with.
	 * @param parameters Parameters list.
	 */

	public void saveForm(@NotNull HttpServletRequest request, @NotNull HashMap<Object, String> parameters)
	{
		// Copy parameters to attributes.
		parameters.forEach((key, value) -> request.setAttribute(value, request.getParameter(value)));
	}

	/**
	 * Put an error in an HTML form.
	 * <p>
	 * This method read the error codes from the supplied exception and put their
	 * corresponding translated texts into HTML elements according to the
	 * parameters list. This list contains for each error code the HTML element
	 * name in which place the translated error message.
	 *
	 * @param controllerException Exception in which the error codes are.
	 * @param request             Request to deal with.
	 * @param parameters          List of HTML associated element error.
	 */

	public void putFormError(@NotNull ControllerException controllerException, @NotNull HttpServletRequest request, HashMap<Object, String> parameters)
	{
		String property;

		// For each error code.
		while (controllerException.hasError())
		{
			// Get the error code from the exception
			ControllerError code = controllerException.getLastError(false);

			// Get the HTML element name in which the error message goes.
			property = parameters.get(code);

			// Update the form and clear the invalid field.
			wrongAttribute(request, property, controllerException.getLastErrorMessage());
		}
	}

	/**
	 * Put an error in an HTML form.
	 * <p>
	 * This method read the error codes from the supplied exception and put their
	 * corresponding translated texts into HTML elements according to the
	 * parameters list. This list contains for each error code the HTML element
	 * name in which place the translated error message.
	 *
	 * @param modelException Exception in which the error codes are.
	 * @param request        Request to deal with.
	 * @param parameters     List of HTML associated element error.
	 */

	public void putFormError(@NotNull ModelException modelException, @NotNull HttpServletRequest request, HashMap<Object, String> parameters)
	{
		String property;

		// For each error code.
		while (modelException.hasError())
		{
			// Get the error code from the exception
			ModelError code = modelException.getLastError(false);

			// Get the HTML element name in which the error message goes.
			property = parameters.get(code);

			// Update the form and clear the invalid field.
			wrongAttribute(request, property, modelException.getLastErrorMessage());
		}
	}

	/**
	 * Put a UI field as invalid.
	 * <p></p>
	 * This method remove an attribute from the request which lead to emptying his
	 * corresponding field in an HTML form. AT the same time, it put an error
	 * message into the corresponding error field. If the property is null, this
	 * method do nothing.
	 *
	 * @param request  Request to deal with.
	 * @param property Field name to modify.
	 * @param message  Message to put in the error field.
	 */

	private void wrongAttribute(HttpServletRequest request, String property, String message)
	{
		// Do nothing if no known property is supplied.
		if (property == null) return;

		// Construct the label name.
		String label = "error" + Character.toString(property.charAt(0)).toUpperCase() + property.substring(1);

		// Put the message texte.
		request.setAttribute(label, message);

		// The field is not valid, so remove-it.
		request.removeAttribute(property);
	}
}
