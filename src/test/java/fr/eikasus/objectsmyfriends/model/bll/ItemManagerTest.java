package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.*;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test functionalities of the item manager.
 */

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({ManagerFactory.class, DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemManagerTest
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Class used for test facilities.
	private TestSupport<Bid> testSupport;

	// Injected DAO factory by weld junit extension.
	@Inject private DAOFactory daoFactory;

	// Injected manager factory by weld junit extension.
	@Inject private ManagerFactory managerFactory;

	private ItemManager itemManager;

	private Item item;
	private User user;

	private List<Category> categories;
	private List<Item> items;

	private	String itemName;
	private	String itemDescription;
	private	Date itemStart;
	private	Date itemEnd;
	private	int itemPrice;
	private	User itemUser;
	private	Category itemCategory;

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and ItemManager objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique item manager instance.
		itemManager = managerFactory.getItemManager();

		// Clean the database.
		afterEach();
	}

	/**
	 * Populate database for each test.
	 */

	@BeforeEach void setUp()
	{
		testSupport.action("Populating the database");

		testSupport.populateDatabase(daoFactory);
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
	 * Test the ability to add an item.
	 */

	@Test void add()
	{
		testSupport.enterFunction();

		Date current = new Date();

		// Search a particular user.
		assertDoesNotThrow(() -> {user = managerFactory.getUserManager().find("Fabien", null, "P@ssw0rd");});
		assertNotNull(user);

		// Search a particular category.
		assertDoesNotThrow(() -> {categories = managerFactory.getCategoryManager().find("Informatique");});
		assertNotNull(categories);

		addItem("name", "/", "wrong name");

		addItem("description", "/", "wrong description");

		addItem("start", testSupport.daysBefore(current, 5), "wrong start date");

		addItem("end", testSupport.daysBefore(current, 5), "wrong end date");

		addItem("price", -10, "wrong price");

		addItem("seller", null, "no seller");

		addItem("category", null, "no category");

		addItem("", null, "good data");

		System.out.println(item);
	}

	/**
	 * Test the ability to update an item.
	 */

	@Test void update()
	{
		testSupport.enterFunction();

		Date current = new Date();

		searchIem("Fabien", "P@ssw0rd", new Search().setMyCurrentSales());

		updateItem("name", item.getName().toUpperCase(), true);

		updateItem("description", item.getDescription().toUpperCase(), true);

		updateItem("initialPrice", item.getInitialPrice() * 100, false);

		// It is not possible to change the state of an item like this.
		updateItem("state", ItemState.CA, false);

		// Dates can't be modified when item is active.
		updateItem("biddingStart", testSupport.daysBefore(current, 5), false);

		// Dates can't be modified when item is active.
		updateItem("biddingEnd", testSupport.daysBefore(current, 5), false);

		searchIem("Manu", "P@ssw0rd", new Search().setMyWaitingSales());

		// Changing date start will change item state from waiting to active.
		updateItem("biddingStart", testSupport.daysBefore(current, 1), true);

		// Changing date start is then not possible.
		updateItem("biddingStart", testSupport.daysBefore(current, 0), false);
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Try to add a default item with one property changed.
	 *
	 * @param property Property to modify.
	 * @param value    Value of the property.
	 * @param action   Action performed.
	 */

	@SuppressWarnings("cast")
	private void addItem(@NotNull String property, Object value, String action)
	{
		testSupport.action(String.format("Trying to add item with %s", action));

		itemName = (property.compareTo("name") == 0) ? ((String) value) : ("Tablette");
		itemDescription = (property.compareTo("description") == 0) ? ((String) value) : ("Tablette samsung Galaxy tab S");
		itemStart = (property.compareTo("start") == 0) ? ((Date) value) : (new Date());
		itemEnd = (property.compareTo("end") == 0) ? ((Date) value) : (testSupport.daysAfter(new Date(), 10));
		itemPrice = (property.compareTo("price") == 0) ? ((int) value) : (10);
		itemUser = (property.compareTo("seller") == 0) ? ((User) value) : (user);
		itemCategory = (property.compareTo("category") == 0) ? ((Category) value) : (categories.get(0));

		if (property.length() != 0)
			assertThrows(ModelException.class, () -> item = itemManager.add(itemName, itemDescription, itemStart, itemEnd, itemPrice, itemUser, itemCategory));
		else
			assertDoesNotThrow(() -> item = itemManager.add(itemName, itemDescription, itemStart, itemEnd, itemPrice, itemUser, itemCategory));

		System.out.println("Passed");
	}

	/**
	 * Try to modify an item property.
	 *
	 * @param property Property to modify.
	 * @param value    Value of the property.
	 * @param attended Attended result of the operation
	 */

	private void updateItem(@NotNull String property, Object value, boolean attended)
	{
		HashMap<String, Object> properties = new HashMap<>();

		properties.put(property, value);

		testSupport.action(String.format("Trying to change <<%s>> property to <<%s>> value %s success attended", property, value, (attended) ? ("with") : ("without")));

		if (attended) assertDoesNotThrow(()-> itemManager.update(item, properties));
		else assertThrows(ModelException.class, ()-> itemManager.update(item, properties));

		System.out.println((attended) ? (item) : ("Passed"));
	}

	/**
	 * Search an item related to a user.
	 *
 	 * @param name     User name
	 * @param password User password
	 * @param criteria Search criteria
	 */

	@SuppressWarnings({"unchecked", "SameParameterValue"})
	private void searchIem(String name, String password, Search criteria)
	{
		// Search for seller and its items.
		HashMap<String, Object> sellerData = testSupport.searchItem(managerFactory, UserRole.SELLER, name, password, criteria);

		user = (User) sellerData.get("user");
		items = (List<Item>) sellerData.get("items");

		assertNotEquals(0, items.size());
		item = items.get(0);
	}
}