package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Image;

/**
 * Interface used for implementing the image data access object.
 * <p>
 * This interface defines all the specific methods to handle image objects
 * within the data access layer. It is supplied by the DAO factory object and
 * used by its corresponding manager. The generic dao interface, that this
 * interface implements too, implements the methods commun to all data access
 * objects.
 */

public interface ImageDAO extends GenericDAO<Image, Long>
{
}
