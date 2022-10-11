package fr.eikasus.objectsmyfriends.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerException extends Exception
{
	/* ************* */
	/* Class members */
	/* ************* */

	// List of error codes.
	private List<ControllerError> errorCodes;

	// Resource containing localized error messages.
	private static final ResourceBundle resourceBundle;

	/* *********** */
	/* static code */
	/* *********** */

	static
	{
		resourceBundle = ResourceBundle.getBundle("controller");
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public ControllerException()
	{
		// Create the errors list.
		errorCodes = new ArrayList<>();
	}

	/**
	 * Create a ControllerException object with the given error code. If an
	 * exception is supplied , the display the stack trace.
	 *
	 * @param exc  Previous generated exception.
	 * @param code Error code to add.
	 */

	public ControllerException(Exception exc, ControllerError code)
	{
		this();

		// Add a new error code.
		add(code);

		// If an exception is supplied, display his stack trace.
		if (exc != null) exc.printStackTrace();
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Add an error code to the exception object list.
	 *
	 * @param exc Previously generated exception.
	 * @param num Error code to add.
	 */

	public void add(Exception exc, ControllerError num)
	{
		errorCodes.add(num);

		// If an exception is supplied, display his stack trace.
		if (exc != null) exc.printStackTrace();
	}

	/**
	 * Add an error code to the exception object list.
	 *
	 * @param num Error code to add.
	 *
	 * @return ModelException instance
	 */

	public ControllerException add(ControllerError num)
	{
		errorCodes.add(num);

		// To throw this exception just after adding an error code.
		return this;
	}

	/**
	 * Determine if the object has errors or not.
	 *
	 * @return True if it contains error codes, false otherwise.
	 */

	public boolean hasError()
	{
		return (errorCodes.size() != 0);
	}

	/**
	 * Determine if the error code supplied in parameter was generated.
	 *
	 * @param code Error code to deal with.
	 *
	 * @return True if the error was generated, false otherwise.
	 */

	public boolean hasError(ControllerError code)
	{
		return (errorCodes.contains(code));
	}

	/**
	 * Return the last generated error code and remove it from the list.
	 *
	 * @return Last error code or null if nothing.
	 */

	public ControllerError getLastError()
	{
		if (hasError()) return errorCodes.remove(errorCodes.size() - 1);
		else return null;
	}

	/**
	 * Return the last generated error code.
	 *
	 * @param remove True if that error code should be removed from the list.
	 *
	 * @return Last error code or null if nothing.
	 */

	public ControllerError getLastError(boolean remove)
	{
		if (remove) return getLastError();

		if (hasError()) return errorCodes.get(errorCodes.size() - 1);
		else return null;
	}

	/**
	 * Retrieve the last error message and remove it from the list.
	 *
	 * @return Last generated error message or null if nothing.
	 */

	public String getLastErrorMessage()
	{
		ControllerError code = getLastError();

		return getMessageFromCode((code != null) ? (code) : (ControllerError.UNKNOWN_ERROR_CODE));
	}

	/**
	 * Convert an error code to a localised string.
	 *
	 * @return Error message corresponding to the supplied error code.
	 */

	static public String getMessageFromCode(ControllerError code)
	{
		String key;

		key = String.format("%05x", code.getValue());

		return (resourceBundle != null) ? (resourceBundle.getString(key)) : (String.format("[%s]", key));
	}

	/**
	 * Get the error messages.
	 * <p></p>
	 * Get all the error messages containing in the exception.
	 *
	 * @return Error messages
	 */

	@Override public String getMessage()
	{
		StringBuffer sb = new StringBuffer();

		// Convert each code to ext.
		for (ControllerError code : errorCodes)
			sb.append("\n").append(getMessageFromCode(code));

		sb.append("\n");

		// Return message list.
		return sb.toString();
	}
}
