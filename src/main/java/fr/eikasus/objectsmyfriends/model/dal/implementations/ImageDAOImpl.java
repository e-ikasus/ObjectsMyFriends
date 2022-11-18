package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.dal.annotations.ImageDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ImageDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the image data access object.
 * <p>
 * This class supplies all the necessary methods to handle image objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager.
 * <p>
 * This is the implementation for database.
 */

@ApplicationScoped @ImageDAODB
public class ImageDAOImpl extends GenericDAOImpl<Image, Long> implements ImageDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public ImageDAOImpl()
	{
		super(Image.class);
	}
}
