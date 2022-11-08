package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.BidDAO;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.CategoryDAO;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ItemDAO;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.UserDAO;
import fr.eikasus.objectsmyfriends.model.misc.ItemState;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * This class is used to test functionalities of the item data access object.
 */

class ItemDAOImplTest
{
	private static DAOFactory daoFactory;
	private static CategoryDAO categoryDAO;
	private static UserDAO userDAO;
	private static BidDAO bidDAO;
	private static ItemDAO itemDAO;

	private static TestSupport<Item> testSupport;
	private static List<Category> categories = new ArrayList<>();
	private static List<User> users = new ArrayList<>();
	private static List<Bid> bids = new ArrayList<>();
	private static List<Item> items = new ArrayList<>();

	private static final List<Item> attendedItems = new ArrayList<>();

	/**
	 * Instantiate test helper and DAO objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Instantiate a dao factory object;
		daoFactory = new DAOFactory();

		// Retrieve DAO objects.
		categoryDAO = daoFactory.getCategoryDAO();
		userDAO = daoFactory.getUserDAO();
		bidDAO = daoFactory.getBidDAO();
		itemDAO = daoFactory.getItemDAO();

		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase(daoFactory);
	}

	/**
	 * Free used resources.
	 */

	@AfterAll public static void afterAll()
	{
		// Close the dao factory object.
		daoFactory.close();
	}

	/**
	 * Populate database for each test.
	 */

	@BeforeEach public void setUp()
	{
		testSupport.action("Populating the database");

		// Populate the database.
		testSupport.populateDatabase(daoFactory);

		testSupport.action("Retrieving all images");
		assertDoesNotThrow(() -> items = itemDAO.find());

		testSupport.action("Retrieving all users");
		assertDoesNotThrow(() -> users = userDAO.find());

		testSupport.action("Retrieving all bids");
		assertDoesNotThrow(() -> bids = bidDAO.find());

		testSupport.action("Retrieving all categories");
		assertDoesNotThrow(() -> categories = categoryDAO.find());
	}

	/**
	 * Clear the database after each test.
	 */

	@AfterEach public void afterEach()
	{
		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase(daoFactory);
	}

	/* ************** */
	/* Tester methods */
	/* ************** */

	/**
	 * Test the ability to find items by criteria.
	 */

	@Test void findByCriteria()
	{
		testSupport.enterFunction();

		itemsBoughtBySpecificUser(users.get(2));

		itemsCurrentlyInSelling();

		itemsSpecificUserBid(users.get(0));

		for (int index = 2; index < users.size(); index++) itemsRelatedToUser(users.get(index));
	}

	/**
	 * Test the ability to find items by property.
	 */

	@Test void findByProperty()
	{
		testSupport.enterFunction();

		// Deal with each category.
		categories.forEach(this::itemsRelatedToCategory);
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify the success of retrieving items bought by a user.
	 *
	 * @param user User do deal with.
	 */

	private void itemsBoughtBySpecificUser(@NotNull User user)
	{
		testSupport.executeAndCompare(String.format("Searching items bought by user %s", user.getUsername()), attendedItems, () ->
		{
			items.forEach(item -> { if (item.getBuyer() == user) attendedItems.add(item); });
			return itemDAO.findByCriteria(user, UserRole.BUYER, new Search().setMyWonBids(), null, null);
		});
	}

	/**
	 * Verify the success of retrieving items currently in selling.
	 */

	private void itemsCurrentlyInSelling()
	{
		testSupport.executeAndCompare("Searching items currently in selling", attendedItems, () ->
		{
			items.forEach(item -> { if (item.isActive()) attendedItems.add(item); });
			return itemDAO.findByCriteria(null, UserRole.BUYER, new Search().setOpenedBids(), null, null);
		});
	}

	/**
	 * Verify the success of retrieving items on which user bid.
	 *
	 * @param user User do deal with.
	 */

	private void itemsSpecificUserBid(User user)
	{
		testSupport.executeAndCompare("Searching items on which a specific user bid", attendedItems, () ->
		{
			items.forEach(item ->
			{
				if (item.isActive())
				{
					for (Bid bid : bids)
					{
						if ((bid.getItem() == item) && (bid.getUser() == user))
						{
							attendedItems.add(item);
							break;
						}
					}
				}
			});

			return itemDAO.findByCriteria(users.get(0), UserRole.BUYER, new Search().setMyCurrentBids(), null, null);
		});
	}

	/**
	 * Verify the success of retrieving items related to a user.
	 *
	 * @param user User do deal with.
	 */

	private void itemsRelatedToUser(@NotNull User user)
	{
		testSupport.executeAndCompare(String.format("Searching items sold by user %s", user.getUsername()), attendedItems, () ->
		{
			items.forEach(item -> {if ((item.getSeller() == user) && (item.getState() != ItemState.CA)) attendedItems.add(item);});
			return itemDAO.findByCriteria(user, UserRole.SELLER, new Search().setMyWaitingSales().setMyCurrentSales().setMyEndedSales(), null, null);
		});

		testSupport.executeAndCompare(String.format("Searching items currently sold by user %s", user.getUsername()), attendedItems, () ->
		{
			items.forEach(item -> {if ((item.getSeller() == user) && (item.getState() == ItemState.AC)) attendedItems.add(item);});
			return itemDAO.findByCriteria(user, UserRole.SELLER, new Search().setMyCurrentSales(), null, null);
		});

		testSupport.executeAndCompare(String.format("Searching items waiting to be sold by user %s", user.getUsername()), attendedItems, () ->
		{
			items.forEach(item -> {if ((item.getSeller() == user) && (item.getState() == ItemState.WT)) attendedItems.add(item);});
			return itemDAO.findByCriteria(user, UserRole.SELLER, new Search().setMyWaitingSales(), null, null);
		});
	}

	/**
	 * Verify the success of retrieving items of a specific category.
	 *
	 * @param category Category do deal with.
	 */

	private void itemsRelatedToCategory(@NotNull Category category)
	{
		testSupport.executeAndCompare(String.format("Searching items of category %s", category.getLabel()), attendedItems, () ->
		{
			items.forEach(item -> { if (item.getCategory() == category) attendedItems.add(item); });
			return itemDAO.findByProperty("category", category);
		});
	}
}