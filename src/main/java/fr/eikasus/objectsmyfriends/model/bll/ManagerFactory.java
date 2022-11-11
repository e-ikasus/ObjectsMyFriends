package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.implementations.*;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.*;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;

public class ManagerFactory
{
	/* ************* */
	/* Class members */
	/* ************* */

	private final DAOFactory daoFactory;

	private UserManager userManager = null;

	private CategoryManager categoryManager = null;

	private ItemManager itemManager = null;

	private PickupManager pickupManager = null;

	private ImageManager imageManager = null;

	private BidManager bidManager = null;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public DAOFactory getDaoFactory()
	{
		return daoFactory;
	}

	/* *********** */
	/* Constructor */
	/* *********** */

	/**
	 * Constructor of the class that also create the DAOFactory object.
	 */

	public ManagerFactory()
	{
		daoFactory = new DAOFactory();
	}

	/* *************************** */
	/* Getters for entity managers */
	/* *************************** */

	public UserManager getUserManager()
	{
		if (userManager == null) userManager = new UserManagerImpl(this);

		return userManager;
	}

	public CategoryManager getCategoryManager()
	{
		if (categoryManager == null) categoryManager = new CategoryManagerImpl(this);

		return categoryManager;
	}

	public ItemManager getItemManager()
	{
		if (itemManager == null) itemManager = new ItemManagerImpl(this);

		return itemManager;
	}

	public PickupManager getPickupManager()
	{
		if (pickupManager == null) pickupManager = new PickupManagerImpl(this);

		return pickupManager;
	}

	public ImageManager getImageManager()
	{
		if (imageManager == null) imageManager = new ImageManagerImpl(this);

		return imageManager;
	}

	public BidManager getBidManager()
	{
		if (bidManager == null) bidManager = new BidManagerImpl(this);

		return bidManager;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Close all resources that need to be closed.
	 */

	public void close()
	{
		daoFactory.close();
	}
}
