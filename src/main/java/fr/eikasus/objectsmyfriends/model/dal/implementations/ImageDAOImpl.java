package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.dal.annotations.ImageDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ImageDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the image data access object.
 */

@ApplicationScoped @ImageDAODB
public class ImageDAOImpl extends GenericDAOImpl<Image, Long> implements ImageDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	public ImageDAOImpl()
	{
		super(Image.class);
	}
}
