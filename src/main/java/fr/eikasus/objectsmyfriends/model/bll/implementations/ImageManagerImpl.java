package fr.eikasus.objectsmyfriends.model.bll.implementations;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.ImageManager;
import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ImageDAO;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Image manager class.
 * <p></p>
 * This class is used to manage images according to the business logic. It
 * should be used by the controllers to handle images like adding, deleting and
 * so. The access of one of data access object method by the controllers is
 * strictly forbidden. There is no method to retrieve images from the database
 * because they are contained in the item itself. Retrieving an item allow then
 * access to its images.
 *
 * @see #add(Item, String) add()
 * @see #delete(Image) delete()
 */

public class ImageManagerImpl extends GenericManagerImpl implements ImageManager
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String VALIDATE_PATH = "^[0-9A-Za-z_-]+\\.[A-Za-z]{3}$";

	/* ************* */
	/* Class members */
	/* ************* */

	// Data access object instance.
	private final ImageDAO dao;

	private final Pattern pathCheck;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Constructor of the class.
	 */

	public ImageManagerImpl(ManagerFactory managerFactory)
	{
		super(managerFactory);

		// Data access object for user entity operations.
		dao = managerFactory.getDaoFactory().getImageDAO();

		// Property validators.
		pathCheck = Pattern.compile(VALIDATE_PATH);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create an item image.
	 * <p></p>
	 * This method create an image into the database and attach it to the item
	 * supplied in parameter. This method doesn't care about how the image is
	 * retrieve and how it is saved in the system storage.
	 *
	 * @param item Item that the new image belongs to.
	 * @param path Path to the image file.
	 *
	 * @return The newly created image.
	 *
	 * @throws ModelException In case of problem.
	 */

	public Image add(Item item, String path) throws ModelException
	{
		Image newImage = new Image(null, path);

		// This is necessary to not update the item before this image is created.
		newImage.setItem(item);

		try
		{
			// Check the validity of the image
			validate(newImage);

			// Save the image into the database.
			dao.save(newImage);

			// Add the image to the corresponding item.
			item.addImage(newImage);
		}
		catch (ModelException me)
		{
			// Throw a generic error too.
			throw me.add(ModelError.UNABLE_TO_CREATE_IMAGE);
		}

		// Return the newly created image.
		return newImage;
	}

	/**
	 * Delete an image.
	 * <p></p>
	 * This method delete an image from the database and remove it from the item.
	 *
	 * @param image Image to delete from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void delete(@NotNull Image image) throws ModelException
	{
		try
		{
			// Remove it from the item.
			image.getItem().removeImage(image);

			// Delete the image from the database.
			dao.delete(image);
		}
		catch (ModelException me)
		{
			// Throw a generic error too.
			throw me.add(ModelError.UNABLE_TO_DELETE_IMAGE);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify the validity of the supplied image.
	 * <p></p>
	 * This method check the validity of an image to be saved in the database. If
	 * one of his properties is invalid, an exception occur containing errors
	 * detected.
	 *
	 * @param image Image to verify
	 *
	 * @throws ModelException In case of problem.
	 */

	private void validate(@NotNull Image image) throws ModelException
	{
		ModelException exception = new ModelException();
		String string;

		// Check the name validity.
		if (((string = image.getPath()) == null) || (!pathCheck.matcher(string).matches()) || (string.length() > Image.MAX_LENGTH_PATH))
			exception.add(ModelError.INVALID_IMAGE_PATH);

		// If there is an error, throws an exception.
		if (exception.hasError()) throw exception;
	}
}
