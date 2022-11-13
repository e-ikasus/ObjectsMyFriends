package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.annotations.*;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ManagerFactory
{
	/* ************* */
	/* Class members */
	/* ************* */

	@Inject @UserManagerDB private UserManager userManager;

	@Inject @CategoryManagerDB private CategoryManager categoryManager;

	@Inject @ItemManagerDB private ItemManager itemManager;

	@Inject @PickupManagerDB private PickupManager pickupManager;

	@Inject @ImageManagerDB private ImageManager imageManager;

	@Inject @BidManagerDB private BidManager bidManager;

	/* ************ */
	/* Constructors */
	/* ************ */

	@PostConstruct
	public void initialize()
	{
		userManager.setManagerFactory(this);
		categoryManager.setManagerFactory(this);
		itemManager.setManagerFactory(this);
		pickupManager.setManagerFactory(this);
		imageManager.setManagerFactory(this);
		bidManager.setManagerFactory(this);
	}

	/* *************************** */
	/* Getters for entity managers */
	/* *************************** */

	public UserManager getUserManager()
	{
		return userManager;
	}

	public CategoryManager getCategoryManager()
	{
		return categoryManager;
	}

	public ItemManager getItemManager()
	{
		return itemManager;
	}

	public PickupManager getPickupManager()
	{
		return pickupManager;
	}

	public ImageManager getImageManager()
	{
		return imageManager;
	}

	public BidManager getBidManager()
	{
		return bidManager;
	}
}
