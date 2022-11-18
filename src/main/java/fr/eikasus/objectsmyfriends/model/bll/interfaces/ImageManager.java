package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used for implementing image manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage images on items. Methods present in the interface implementation are
 * allowed to use as many as data access objects required to accomplish their
 * goal. The access of one of data access object method by the controllers is
 * strictly forbidden. There is no method to retrieve images because they are
 * contained in the item itself. Retrieving an item allow then access to its
 * images.
 *
 * @see #add(Item, String)
 * @see #delete(Image)
 */

public interface ImageManager
{
	/**
	 * Set the manager factory.
	 * <p>
	 * This method retrieves the manager factory to allow the instance methods to
	 * access others managers.
	 *
	 * @param managerFactory Manager factory instance.
	 */

	void setManagerFactory(ManagerFactory managerFactory);

	/**
	 * Create an item image.
	 * <p>
	 * This method create an image into the persistence unit and attach it to the
	 * item supplied in parameter. This method doesn't care about how the image
	 * file is retrieved and how it is saved in the system storage.
	 *
	 * @param item Item that the new image belongs to.
	 * @param path Path to the image file.
	 *
	 * @return The newly created image.
	 *
	 * @throws ModelException In case of problem.
	 */

	Image add(Item item, String path) throws ModelException;

	/**
	 * Delete an image.
	 * <p>
	 * This method delete an image from the persistence unit and remove it from
	 * the item.
	 *
	 * @param image Image to delete.
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(@NotNull Image image) throws ModelException;
}
