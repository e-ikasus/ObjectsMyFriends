package fr.eikasus.objectsmyfriends.model.dal;

import fr.eikasus.objectsmyfriends.model.bo.*;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.*;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is used to test the basic functionalities of the data access
 * objects of the data access layer.
 */

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DAOFactoryTest
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Class used for test facilities.
	private TestSupport<Object> testSupport;

	// Injected DAO factory by weld junit extension.
	@Inject private DAOFactory daoFactory;

	// DAO objects for accessing database.
	private CategoryDAO categoryDAO;
	private UserDAO userDAO;
	private ImageDAO imageDAO;
	private BidDAO bidDAO;
	private PickupDAO pickupDAO;
	private ItemDAO itemDAO;

	// Lists for handling objects database.
	private List<Category> categories = new ArrayList<>();
	private List<User> users = new ArrayList<>();
	private List<Image> images = new ArrayList<>();
	private List<Bid> bids = new ArrayList<>();
	private List<PickupPlace> pickupPlaces = new ArrayList<>();
	private List<Item> items = new ArrayList<>();

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and data access objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Retrieve DAO objects.
		categoryDAO = daoFactory.getCategoryDAO();
		userDAO = daoFactory.getUserDAO();
		imageDAO = daoFactory.getImageDAO();
		bidDAO = daoFactory.getBidDAO();
		pickupDAO = daoFactory.getPickupDAO();
		itemDAO = daoFactory.getItemDAO();

		// Clean the database.
		afterEach();
	}

	/**
	 * Clear the database.
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
	 * Test the category data access object.
	 * <p></p>
	 * This method test the basic functionalities of the category data access
	 * object. It will create a list of categories into the database, after what
	 * the categories will be modified and read again to verify that the changes
	 * are correctly done. This method expect to find an empty database, otherwise
	 * unexpected results or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getCategoryDAO()
	{
		List<Category> changedCategories = new ArrayList<>();

		testSupport.enterFunction();

		// Create category list.
		categories = testSupport.createCategoryList();

		// Save categories into the database.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.save(category)));

		testSupport.action("Saved categories into the database");

		// Print the list.
		categories.forEach(System.out::println);

		// Modify the categories.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.update(category.setLabel(category.getLabel().toUpperCase()))));

		// Detach category objects from database to be able to compare them with
		// modified version.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.detach(category)));

		// Read all the categories again.
		categories.forEach(category -> assertDoesNotThrow(() -> changedCategories.add(categoryDAO.find(category.getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(categories, changedCategories);

		testSupport.action("Modified categories from the database");

		// Display all categories that are red from the database.
		changedCategories.forEach(System.out::println);
	}

	/**
	 * Test the user data access object.
	 * <p></p>
	 * This method test the basic functionalities of the user data access object.
	 * It will create a list of users into the database, after what the users will
	 * be modified and read again to verify that the changes are correctly done.
	 * This method expect to find an empty database, otherwise unexpected results
	 * or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getUserDAO()
	{
		testSupport.enterFunction();

		List<User> changedUsers = new ArrayList<>();

		// Create user list.
		users = testSupport.createUserList();

		// Save users into the database.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.save(user)));

		testSupport.action("Saved users into the database.");

		// Print the list.
		users.forEach(System.out::println);

		// Modify the users.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.update(user.setUsername(user.getUsername().toUpperCase()))));

		// Detach user objects from database to be able to compare them with
		// modified version.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.detach(user)));

		// Read all the users again.
		users.forEach(user -> assertDoesNotThrow(() -> changedUsers.add(userDAO.find(user.getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(users, changedUsers);

		testSupport.action("Modified users from the database.");

		// Display all users that are red from the database.
		changedUsers.forEach(System.out::println);
	}

	/**
	 * Test the item data access object.
	 * <p></p>
	 * This method test the basic functionalities of the item data access object.
	 * Because items belong to users and categories, they also need to be created.
	 * So, it is a good idea to test user and category data access objects before
	 * proceeding. It will create a list of items into the database, after what
	 * the items will be modified and read again to verify that the changes are
	 * correctly done. This method expect to find an empty database, otherwise
	 * unexpected results or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getItemDAO()
	{
		testSupport.enterFunction();

		List<Item> changedItems = new ArrayList<>();

		testSupport.action("Creating categories, users and items");

		// Create list of objects we will deal with.
		categories = testSupport.createCategoryList();
		users = testSupport.createUserList();
		items = testSupport.createItemList(users, categories);

		// Save them into database.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.save(category)));
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.save(user)));
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.save(item)));

		testSupport.action("Items saved into the database");

		// Print the item list.
		items.forEach(System.out::println);

		// Modify the items.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.update(item.setName(item.getName().toUpperCase()))));

		// Detach item objects from database to be able to compare them with
		// modified version.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.detach(item)));

		// Read all the items again.
		items.forEach(item -> assertDoesNotThrow(() -> changedItems.add(itemDAO.find(item.getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(items, changedItems);

		testSupport.action("Items modified from the database");

		// Display all items that are red from the database.
		changedItems.forEach(System.out::println);
	}

	/**
	 * Test the image data access object.
	 * <p></p>
	 * This method test the basic functionalities of the image data access object.
	 * Because images belong to items that belongs to users and categories, they
	 * also need to be created. So, it is a good idea to test user, category and
	 * item data access objects before proceeding. It will create a list of images
	 * into the database, after what the images will be modified and read again to
	 * verify that the changes are correctly done. This method expect to find an
	 * empty database, otherwise unexpected results or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getImageDAO()
	{
		testSupport.enterFunction();

		List<Image> changedImages = new ArrayList<>();

		testSupport.action("Creating categories, users, items and images");

		// Create list of objects we will deal with.
		categories = testSupport.createCategoryList();
		users = testSupport.createUserList();
		items = testSupport.createItemList(users, categories);
		images = testSupport.createImageList(items);

		// Save categories into database.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.save(category)));
		// Save users into database.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.save(user)));
		// Saving items implies images too.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.save(item)));

		testSupport.action("Images saved into the database");

		// Print the image list.
		images.forEach(System.out::println);

		// Modify the image.
		images.forEach(image -> assertDoesNotThrow(() -> imageDAO.update(image.setPath(image.getPath().toUpperCase()))));

		// Detach item objects from database to be able to compare images with
		// modified version as they are detached too.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.detach(item)));

		// Read all the images again.
		images.forEach(image -> assertDoesNotThrow(() -> changedImages.add(imageDAO.find(image.getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(images, changedImages);

		testSupport.action("Modified images from the database");

		// Display all images that are red from the database.
		changedImages.forEach(System.out::println);
	}

	/**
	 * Test the pickup place data access object.
	 * <p></p>
	 * This method test the basic functionalities of the pickup place data access
	 * object. Because pickup places belong to items that belongs to users and
	 * categories, they also need to be created. So, it is a good idea to test
	 * user, category and item data access objects before proceeding. It will
	 * create a list of pickup places into the database, after what the pickup
	 * places will be modified and read again to verify that the changes are
	 * correctly done. This method expect to find an empty database, otherwise
	 * unexpected results or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getPickupDAO()
	{
		testSupport.enterFunction();

		List<PickupPlace> changedPickupPlaces = new ArrayList<>();

		testSupport.action("Creating categories, users, items and pickup places");

		// Create list of objects we will deal with.
		categories = testSupport.createCategoryList();
		users = testSupport.createUserList();
		items = testSupport.createItemList(users, categories);
		pickupPlaces = testSupport.createPickupPlaceList(items);

		// Save categories into database.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.save(category)));
		// Save users into database.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.save(user)));
		// Save items into the database implies pickup places too.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.save(item)));

		testSupport.action("Pickup places saved into the database");

		// Print the pickup place list.
		pickupPlaces.forEach(System.out::println);

		// Modify the pickup places.
		pickupPlaces.forEach(pickupPlace -> assertDoesNotThrow(() -> pickupDAO.update(pickupPlace.setCity(pickupPlace.getCity().toUpperCase()))));

		// Detach item objects from database to be able to compare pickup places
		// with modified version as they are detached too.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.detach(item)));

		// Read all the pickup places again.
		pickupPlaces.forEach(pickupPlace -> assertDoesNotThrow(() -> changedPickupPlaces.add(pickupDAO.find(pickupPlace.getItem().getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(pickupPlaces, changedPickupPlaces);

		testSupport.action("Modified pickup places from the database");

		// Display all pickup places that are red from the database.
  	changedPickupPlaces.forEach(System.out::println);
	}

	/**
	 * Test the bid data access object.
	 * <p></p>
	 * This method test the basic functionalities of the bid data access object.
	 * Because bids belong to items that belongs to users and categories, they
	 * also need to be created. So, it is a good idea to test user, category and
	 * item data access objects before proceeding. It will create a list of bids
	 * into the database, after what the bids will be modified and read again to
	 * verify that the changes are correctly done. This method expect to find an
	 * empty database, otherwise unexpected results or errors could be generated.
	 * <p></p>
	 * Be aware that after the execution of the test, the database is completely
	 * emptied.
	 */

	@Test void getBidDAO()
	{
		testSupport.enterFunction();

		List<Bid> changedBids = new ArrayList<>();

		testSupport.action("Creating categories, users, items and bids");

		// Create list of objects we will deal with.
		categories = testSupport.createCategoryList();
		users = testSupport.createUserList();
		items = testSupport.createItemList(users, categories);
		bids = testSupport.createBidList(items, users);

		// Save categories into database.
		categories.forEach(category -> assertDoesNotThrow(() -> categoryDAO.save(category)));
		// Save users into database.
		users.forEach(user -> assertDoesNotThrow(() -> userDAO.save(user)));
		// Save items into the database.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.save(item)));
		// Save bids into database.
		bids.forEach(bid -> assertDoesNotThrow(() -> bidDAO.save(bid)));

		testSupport.action("Bids saved into the database");

		// Print the bid list.
		bids.forEach(System.out::println);

		// Modify the bids.
		bids.forEach(bid -> assertDoesNotThrow(() -> bidDAO.update(bid.setPrice(bid.getPrice() * 1000))));

		// Detach item objects from database to be able to compare bids with
		// modified version as they are detached too.
		items.forEach(item -> assertDoesNotThrow(() -> itemDAO.detach(item)));

		// Read all the pickup places again.
		bids.forEach(bid -> assertDoesNotThrow(() -> changedBids.add(bidDAO.find(bid.getIdentifier()))));

		// Compare the result against what it is attended.
		assertEquals(bids, changedBids);

		testSupport.action("Bids modified from the database");

		// Display all bids that are red from the database.
		changedBids.forEach(System.out::println);
	}
}