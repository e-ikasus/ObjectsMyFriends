package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ImageDAO;

/**
 * Class used to implement the image data access object.
 */

public class ImageDAOImpl extends GenericDAOImpl<Image, Long> implements ImageDAO
{
	public ImageDAOImpl(DAOFactory daoFactory)
	{
		super(Image.class, daoFactory);
	}
}
