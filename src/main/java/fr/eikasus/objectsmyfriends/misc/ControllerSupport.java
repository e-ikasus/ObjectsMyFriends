package fr.eikasus.objectsmyfriends.misc;

import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Class used to simplify handling HTML form.
 * <p></p>
 * This class is used to minimize code repetition for managing HTML form and
 * then simplify the controllers code. For exemple, this class give access to
 * method for simplify error handling in forms.
 *
 * @see #getInstance()
 * @see #loadImage(HttpServletRequest, String) loadImage()
 * @see #getUrlImage(HttpServletRequest, String) getUrlImage()
 * @see #parseDateParameter(HttpServletRequest, String) parseDateParameter()
 * @see #parseIntegerParameter(HttpServletRequest, String)
 * parseIntegerParameter()
 * @see #parseLongParameter(HttpServletRequest, String) parseLongParameter()
 * @see #saveForm(HttpServletRequest, HashMap) saveForm()
 * @see #putFormError(ModelException, HttpServletRequest, HashMap)
 * putFormError()
 * @see #putFormError(ControllerException, HttpServletRequest, HashMap)
 * putFormError()
 */

public class ControllerSupport
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String IMAGES_PATH = "/item_images/";

	/* ************* */
	/* Class members */
	/* ************* */

	private static ControllerSupport instance = null;

	private static String imagePath = null;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Private constructor of the class.
	 */

	private ControllerSupport()
	{

	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Retrieve an image.
	 * <p></p>
	 * This method retrieve an image sent via the multipart/form-data request and
	 * store it in the location returned by the {@code getImagePath} method. Only
	 * jpg and png file formats are allowed.
	 *
	 * @param request   Request in which the file was received.
	 * @param paramName Name of the parameter containing the file
	 *
	 * @return Name of the file.
	 *
	 * @throws Exception In case of problem.
	 */

	public String loadImage(@NotNull HttpServletRequest request, @NotNull String paramName) throws Exception
	{
		int extPos, bytesRed;
		byte[] bytes = new byte[1024];

		Part filePart = request.getPart(paramName);

		String fileIdPart, destName, contentType;
		String fileName = filePart.getSubmittedFileName();

		// If no name is defined.
		if (fileName == null) throw new Exception();

		// Put the file name in lower case.
		fileName = fileName.toLowerCase();

		// Check if the file has an extension.
		if ((extPos = fileName.lastIndexOf('.')) == -1) throw new Exception();

		// The extension should be 3 characters long.
		if ((extPos + 4) != fileName.length()) throw new Exception();

		// Retrieve the type of the file
		contentType = filePart.getContentType();

		// Only jpg and png file formats are allowed.
		if ((contentType.compareTo("image/jpg") != 0) && (contentType.compareTo("image/png") != 0))
			throw new Exception();

		// The destination file name is formed with the current date.
		fileIdPart = String.format("%d", (new Date()).getTime());

		// Destination name of the image.
		destName = fileIdPart + fileName.substring(extPos);

		try (FileOutputStream fileOutputStream = new FileOutputStream(getImagePath(request) + destName); InputStream inputStream = filePart.getInputStream())
		{
			while ((bytesRed = inputStream.read(bytes)) != -1)
				fileOutputStream.write(bytes, 0, bytesRed);
		}

		// Return the file name crated.
		return destName;
	}

	/**
	 * Compute the url of an image.
	 * <p></p>
	 * This method is used to create the url used to access the image from the
	 * client. Because the image entities only store the image filename and the
	 * place where those images are stored is not hardcoded, this function should
	 * be used to allow the client browser to load them.
	 *
	 * @param request  Request needed to retrieve context.
	 * @param fileName Name of the image file.
	 *
	 * @return Url to the image.
	 */

	public String getUrlImage(@NotNull HttpServletRequest request, @NotNull String fileName)
	{
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.lastIndexOf(servletPath));

		return urlBase + IMAGES_PATH + fileName;
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
	 * Get a date parameter.
	 * <p></p>
	 * This method take a parameter from an HttpServletRequest request. This
	 * parameter is expected to be a date inside a String. If the content of that
	 * string doesn't conform to a date, no exception is generated but a null
	 * value is returned.
	 *
	 * @param request Request to deal with.
	 * @param name    Parameter name.
	 *
	 * @return Value of the parameter or null.
	 */

	public Date parseDateParameter(@NotNull HttpServletRequest request, @NotNull String name)
	{
		// Formatter for the date.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

		try
		{
			return dateFormat.parse(request.getParameter(name));
		}
		catch (Exception exc)
		{
			return null;
		}
	}

	/**
	 * Get an integer parameter.
	 * <p></p>
	 * This method take a parameter from an HttpServletRequest request. This
	 * parameter is expected to be an integer inside a String. If the content of
	 * that string doesn't conform to an integer, no exception is generated but a
	 * zero value is returned.
	 *
	 * @param request Request to deal with.
	 * @param name    Parameter name.
	 *
	 * @return Value of the parameter or zero.
	 */

	public int parseIntegerParameter(@NotNull HttpServletRequest request, @NotNull String name)
	{
		try
		{
			return Integer.parseInt(request.getParameter(name));
		}
		catch (Exception exc)
		{
			return 0;
		}
	}

	/**
	 * Get a long parameter.
	 * <p></p>
	 * This method take a parameter from an HttpServletRequest request. This
	 * parameter is expected to be a long inside a String. If the content of that
	 * string doesn't conform to a long, no exception is generated but a zero
	 * value is returned.
	 *
	 * @param request Request to deal with.
	 * @param name    Parameter name.
	 *
	 * @return Value of the parameter or zero.
	 */

	public long parseLongParameter(@NotNull HttpServletRequest request, @NotNull String name)
	{
		try
		{
			return Long.parseLong(request.getParameter(name));
		}
		catch (Exception exc)
		{
			return 0;
		}
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

	/* ************** */
	/* Helper methods */
	/* ************** */

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

	/**
	 * Determine the item image folder.
	 * <p></p>
	 * This method return the folder where the uploaded images will be stored.
	 * This location is determined by the "item_images_folder" parameter from the
	 * "web.xml" file. If it is not defined, then the default location point to a
	 * folder in the application deployment.
	 *
	 * @param request Request used to access context.
	 *
	 * @return Path to the item images.
	 */

	private String getImagePath(HttpServletRequest request)
	{
		if (imagePath == null)
		{
			// Folder where to put the item images in.
			imagePath = request.getServletContext().getInitParameter("item_images_folder");

			// Default folder location if none is defined.
			if (imagePath == null)
				imagePath = request.getServletContext().getRealPath(IMAGES_PATH);

			File imageFile = new File(imagePath);

			// Create folder if it doesn't exist.
			if ((!imageFile.exists()) && (!imageFile.mkdir())) imagePath = null;
		}

		// Location to put the uploaded item images.
		return imagePath;
	}
}
