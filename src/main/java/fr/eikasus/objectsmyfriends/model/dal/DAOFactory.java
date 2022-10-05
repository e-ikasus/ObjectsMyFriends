package fr.eikasus.objectsmyfriends.model.dal;

import fr.eikasus.objectsmyfriends.model.dal.implementations.*;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.*;

/**
 * Class handling Data Access objects.
 */

public class DAOFactory
{
	// Object instance for handling users data.
	private static UserDAO userDAO;

	// Object instance for handling items data.
	private static ItemDAO itemDAO;

	// Object instance for handling images data.
	private static ImageDAO imageDAO;

	// Object instance for handling bids data.
	private static BidDAO bidDAO;

	// Object instance for handling pickup places data.
	private static PickupDAO pickupDAO;

	// Object instance for handling categories data.
	private static CategoryDAO categoryDAO;

	/* ****************************** */
	/* Getters for data access object */
	/* ****************************** */

	public static UserDAO getUserDAO()
	{
		if (userDAO == null) userDAO = new UserDAOImpl();

		return userDAO;
	}

	public static CategoryDAO getCategoryDAO()
	{
		if (categoryDAO == null) categoryDAO = new CategoryDAOImpl();

		return categoryDAO;
	}

	public static BidDAO getBidDAO()
	{
		if (bidDAO == null) bidDAO = new BidDAOImpl();

		return bidDAO;
	}

	public static ItemDAO getItemDAO()
	{
		if (itemDAO == null) itemDAO = new ItemDAOImpl();

		return itemDAO;
	}

	public static PickupDAO getPickupDAO()
	{
		if (pickupDAO == null) pickupDAO = new PickupDAOImpl();

		return pickupDAO;
	}

	public static ImageDAO getImageDAO()
	{
		if (imageDAO == null) imageDAO = new ImageDAOImpl();

		return imageDAO;
	}
}