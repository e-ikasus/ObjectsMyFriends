package fr.eikasus.objectsmyfriends.misc;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.ImageManager;
import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class used to simplify handling HTML form.
 * <p>
 * This class is used to minimize code repetition for managing HTML form and
 * then simplify the controllers code. For exemple, this class give access to
 * method for simplify error handling in forms.
 *
 * @see #addUploadedImagesToItem(ManagerFactory, HttpServletRequest, Item, List)
 * @see #loadImage(HttpServletRequest, String)
 * @see #getUrlImage(HttpServletRequest, String)
 * @see #getUrlImageHandler(HttpServletRequest)
 * @see #getUrlServlet(HttpServletRequest, String)
 * @see #parseDateParameter(HttpServletRequest, String)
 * @see #parseIntegerParameter(HttpServletRequest, String)
 * @see #parseLongParameter(HttpServletRequest, String)
 * @see #saveForm(HttpServletRequest, HashMap)
 * @see #putFormError(ModelException, HttpServletRequest, HashMap)
 * @see #putFormError(ControllerException, HttpServletRequest, HashMap)
 * @see #getUserFromSession(ManagerFactory, HttpServletRequest)
 * @see #getItemFromSession(ManagerFactory, HttpServletRequest)
 */

public class ControllerSupport
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String IMAGES_PATH = "/item_images/";

	private static final String IMAGE_HANDLER_NAME = "image_handler";

	/* ************* */
	/* Class members */
	/* ************* */

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
	 * Add images to the item.
	 * <p>
	 * This method add the supplied images to the item. The images list only
	 * contain file names and those files should be present in the local storage.
	 * When each image is successfully added to the item, its corresponding image
	 * file is prefixed by the u letter to indicate that this file is pointed to.
	 * Then, in a maintenance process, all image file that aren't prefixed can
	 * then be deleted. If an error occur during the process, all remaining files
	 * are deleted. If something goes wrong during the process, no error is
	 * returned.
	 *
	 * @param managerFactory Instance of the manager factory to use.
	 * @param request        Request used to get context.
	 * @param item           Item to add image to.
	 * @param uploadedImages Images to add to the item.
	 */

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void addUploadedImagesToItem(@NotNull ManagerFactory managerFactory, @NotNull HttpServletRequest request, @NotNull Item item, List<String> uploadedImages)
	{
		ImageManager imageManager = managerFactory.getImageManager();
		String imagePath = getImagePath(request);
		Iterator<String> imagesIterator;
		File oldFile, newFile;
		Image image;
		String currentName;

		// If no image file is supplied, do nothing.
		if (uploadedImages == null) return;

		try
		{
			// Iterator used to parse the collection.
			imagesIterator = uploadedImages.iterator();

			// For each file in the list.
			while (imagesIterator.hasNext())
			{
				// File name to deal with.
				currentName = imagesIterator.next();

				// Add the image to the item.
				image = imageManager.add(item, "u" + currentName);

				// Remove the file to the list to avoid deletion later.
				imagesIterator.remove();

				// Current file name.
				oldFile = new File(imagePath + currentName);

				// File will be prefixed because it is now used.
				newFile = new File(imagePath + "u" + currentName);

				// Rename the file.
				if (!oldFile.renameTo(newFile))
				{
					// Remove the image from the item.
					imageManager.delete(image);

					// stop the process
					break;
				}
			}
		}
		catch (Exception e)
		{
			// Do nothing.
		}

		// Iterator used to parse the collection.
		imagesIterator = uploadedImages.iterator();

		// Delete the remaining files if they exist.
		while (imagesIterator.hasNext())
		{
			// File name to deal with.
			currentName = imagesIterator.next();

			// Current name.
			oldFile = new File(imagePath + currentName);

			// Delete the file from the local storage.
			oldFile.delete();

			// Done with this file.
			imagesIterator.remove();
		}
	}

	/**
	 * Retrieve an image.
	 * <p>
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

	public static @NotNull String loadImage(@NotNull HttpServletRequest request, @NotNull String paramName) throws Exception
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
		if ((contentType.compareTo("image/jpeg") != 0) && (contentType.compareTo("image/png") != 0))
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
	 * <p>
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

	public static @NotNull String getUrlImage(@NotNull HttpServletRequest request, @NotNull String fileName)
	{
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.lastIndexOf(servletPath));

		return urlBase + IMAGES_PATH + fileName;
	}

	/**
	 * Compute the url of the image handler.
	 * <p>
	 * This method is used to create the url used to access the image handler from
	 * the client. This is the address from which the client retrieves images item
	 * and to which images are uploaded.
	 *
	 * @param request Request needed to retrieve context.
	 *
	 * @return Url to the image handler.
	 */

	public static @NotNull String getUrlImageHandler(@NotNull HttpServletRequest request)
	{
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.lastIndexOf(servletPath));

		return urlBase + "/" + IMAGE_HANDLER_NAME;
	}

	/**
	 * Compute the url of a servlet.
	 * <p>
	 * This method is used to create the url needed to access a servlet from the
	 * client. The name of this servlet is supplied in parameter.
	 *
	 * @param request     Request needed to retrieve context.
	 * @param servletName Servlet name.
	 *
	 * @return Url to the servlet.
	 */

	public static @NotNull String getUrlServlet(@NotNull HttpServletRequest request, String servletName)
	{
		String url = request.getRequestURL().toString();
		String servletPath = request.getServletPath();
		String urlBase = url.substring(0, url.lastIndexOf(servletPath));

		return urlBase + "/" + servletName;
	}

	/**
	 * Get a date parameter.
	 * <p>
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

	public static @Nullable Date parseDateParameter(@NotNull HttpServletRequest request, @NotNull String name)
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
	 * <p>
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

	public static int parseIntegerParameter(@NotNull HttpServletRequest request, @NotNull String name)
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
	 * <p>
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

	public static long parseLongParameter(@NotNull HttpServletRequest request, @NotNull String name)
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

	public static void saveForm(@NotNull HttpServletRequest request, @NotNull HashMap<Object, String> parameters)
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

	public static void putFormError(@NotNull ControllerException controllerException, @NotNull HttpServletRequest request, HashMap<Object, String> parameters)
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

	public static void putFormError(@NotNull ModelException modelException, @NotNull HttpServletRequest request, HashMap<Object, String> parameters)
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
	 * Read the user stored in the session.
	 * <p>
	 * This method read the user that is stored in a session attribute. Because
	 * the hibernate session was closed after the user was saved in a previous
	 * http request, it is necessary to read it again from the database, because
	 * it is not in the persistence context anymore. No exception is generated and
	 * null is returned if none is found or in case of problem.
	 *
	 * @param managerFactory Instance of the manager factpry to use.
	 * @param request        Request to deal with.
	 *
	 * @return User found and present in the persistence context or null if none.
	 */

	public static User getUserFromSession(ManagerFactory managerFactory, @NotNull HttpServletRequest request)
	{
		// Retrieve the user from the request.
		User user = (User) request.getSession().getAttribute("user");

		try
		{
			// If a user is found, read-it again from the database and update his
			// session attribute or do nothing if it doesn't exist in the database
			// yet.
			if ( (user != null) && (user.getIdentifier() != 0) )
			{
				// Read the user by its identifier.
				user = managerFactory.getUserManager().find(user.getIdentifier()).get(0);

				// Update his corresponding session attribute.
				request.setAttribute("user", user);
			}
		}
		catch (ModelException me)
		{
			// Do nothing
		}

		// Return the found user.
		return user;
	}

	/**
	 * Read the item stored in the session.
	 * <p>
	 * This method read the item that is stored in a session attribute. Because
	 * the hibernate session was closed after the item was saved in a previous
	 * http request, it is necessary to read it again from the database, because
	 * it is not in the persistence context anymore. No exception is generated and
	 * null is returned if none is found or in case of problem.
	 *
	 * @param managerFactory Instance of the manager factpry to use.
	 * @param request        Request to deal with.
	 *
	 * @return Item found and present in the persistence context or null if none.
	 */

	public static Item getItemFromSession(ManagerFactory managerFactory, @NotNull HttpServletRequest request)
	{
		// Retrieve the item from the request.
		Item item = (Item) request.getSession().getAttribute("item");

		try
		{
			// If an item is found, read-it again from the database and update his
			// session attribute or do nothing if it doesn't exist in the database
			// yet.
			if ( (item != null) && (item.getIdentifier() != 0) )
			{
				// Read the item by its identifier.
				item = managerFactory.getItemManager().find(item.getIdentifier()).get(0);

				// Update his corresponding session attribute.
				request.setAttribute("item", item);
			}
		}
		catch (ModelException me)
		{
			// Do nothing
		}

		// Return the found item.
		return item;
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Put a UI field as invalid.
	 * <p>
	 * This method remove an attribute from the request which lead to emptying his
	 * corresponding field in an HTML form. AT the same time, it put an error
	 * message into the corresponding error field. If the property is null, this
	 * method do nothing.
	 *
	 * @param request  Request to deal with.
	 * @param property Field name to modify.
	 * @param message  Message to put in the error field.
	 */

	private static void wrongAttribute(HttpServletRequest request, String property, String message)
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
	 * <p>
	 * This method return the folder where the uploaded images will be stored.
	 * This location is determined by the "item_images_folder" parameter from the
	 * "web.xml" file. If it is not defined, then the default location point to a
	 * folder in the application deployment.
	 *
	 * @param request Request used to access context.
	 *
	 * @return Path to the item images.
	 */

	private static synchronized String getImagePath(HttpServletRequest request)
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
