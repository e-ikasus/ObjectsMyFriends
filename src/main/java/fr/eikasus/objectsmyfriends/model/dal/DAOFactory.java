package fr.eikasus.objectsmyfriends.model.dal;

import fr.eikasus.objectsmyfriends.model.dal.annotations.*;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Class handling Data Access objects.
 */

@ApplicationScoped
public class DAOFactory
{
	/* ************* */
	/* Class members */
	/* ************* */

	@Inject @UserDAODB private UserDAO userDAO;

	@Inject @CategoryDAODB private CategoryDAO categoryDAO;

	@Inject @ItemDAODB private ItemDAO itemDAO;

	@Inject @PickupDAODB private PickupDAO pickupDAO;

	@Inject @ImageDAODB private ImageDAO imageDAO;

	@Inject @BidDAODB private BidDAO bidDAO;

	/* ****************************** */
	/* Getters for data access object */
	/* ****************************** */

	public UserDAO getUserDAO()
	{
		return userDAO;
	}

	public CategoryDAO getCategoryDAO()
	{
		return categoryDAO;
	}

	public ItemDAO getItemDAO()
	{
		return itemDAO;
	}

	public PickupDAO getPickupDAO()
	{
		return pickupDAO;
	}

	public ImageDAO getImageDAO()
	{
		return imageDAO;
	}

	public BidDAO getBidDAO()
	{
		return bidDAO;
	}
}