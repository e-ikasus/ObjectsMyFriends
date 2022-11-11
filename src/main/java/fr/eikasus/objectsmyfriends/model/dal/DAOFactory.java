package fr.eikasus.objectsmyfriends.model.dal;

import fr.eikasus.objectsmyfriends.model.dal.implementations.*;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class handling Data Access objects.
 */

public class DAOFactory
{
	/* ************* */
	/* Class members */
	/* ************* */

	private static EntityManagerFactory entityManagerFactory = null;

	private EntityManager entityManager;

	private UserDAO userDAO = null;

	private CategoryDAO categoryDAO = null;

	private ItemDAO itemDAO = null;

	private PickupDAO pickupDAO = null;

	private ImageDAO imageDAO = null;

	private BidDAO bidDAO = null;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	/* *********** */
	/* Constructor */
	/* *********** */

	/**
	 * Constructor of the classe that instantiate the persistence context.
	 */

	public DAOFactory()
	{
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("ObjectsMyFriends");

		entityManager = entityManagerFactory.createEntityManager();
	}

	/* ****************************** */
	/* Getters for data access object */
	/* ****************************** */

	public UserDAO getUserDAO()
	{
		if (userDAO == null) userDAO = new UserDAOImpl(this);

		return userDAO;
	}

	public CategoryDAO getCategoryDAO()
	{
		if (categoryDAO == null) categoryDAO = new CategoryDAOImpl(this);

		return categoryDAO;
	}

	public ItemDAO getItemDAO()
	{
		if (itemDAO == null) itemDAO = new ItemDAOImpl(this);

		return itemDAO;
	}

	public PickupDAO getPickupDAO()
	{
		if (pickupDAO == null) pickupDAO = new PickupDAOImpl(this);

		return pickupDAO;
	}

	public ImageDAO getImageDAO()
	{
		if (imageDAO == null) imageDAO = new ImageDAOImpl(this);

		return imageDAO;
	}

	public BidDAO getBidDAO()
	{
		if (bidDAO == null) bidDAO = new BidDAOImpl(this);

		return bidDAO;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Close all resources that need to be closed.
	 */

	public synchronized void close()
	{
		// Close the entity manager if opened.
		if ( (entityManager != null) && (entityManager.isOpen()) ) entityManager.close();

		// No entity manager opened.
		entityManager = null;
	}
}