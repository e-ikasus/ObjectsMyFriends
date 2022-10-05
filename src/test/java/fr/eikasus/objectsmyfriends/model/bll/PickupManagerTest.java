package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PickupManagerTest
{
	private static Item item;
	private static User user;
	private static List<Item> items;

	private static List<PickupPlace> pickupPlaces;
	private static PickupPlace newPickupPlace;
	HashMap<String, Object> properties = new HashMap<>();

	private static TestSupport<PickupPlace> testSupport;
	private static PickupManager pickupManager;

	/**
	 * Instantiate test helper and PickupManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique category manager instance.
		pickupManager = PickupManager.getInstance();
	}

	/**
	 * Populate database for each test.
	 */

	@BeforeEach void setUp()
	{
		testSupport.action("Populating the database");

		testSupport.populateDatabase();
	}

	/**
	 * Clear the database after each test.
	 */

	@AfterEach public void afterEach()
	{
		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase();
	}

	/* ************** */
	/* Tester methods */
	/* ************** */

	@Test void all()
	{
		testSupport.enterFunction();

		searchIem("Manu", "P@ssw0rd", new Search().setMyWaitingSales());

		testSupport.action(String.format("Trying to add a pickup place for item names <<%s>>", item.getName()));
		assertDoesNotThrow(() -> newPickupPlace = pickupManager.add(item, "1 rue de la formation", "123456", "FormationCity"));
		System.out.println(newPickupPlace);

		testSupport.action(String.format("Trying to modify the pickup place for item names <<%s>>", item.getName()));
		properties.put("street", newPickupPlace.getStreet().toUpperCase());
		assertDoesNotThrow(() -> pickupManager.update(newPickupPlace, properties));
		System.out.println(newPickupPlace);

		testSupport.action(String.format("Trying to delete the pickup place for item names <<%s>>", item.getName()));
		assertDoesNotThrow(() -> pickupManager.delete(newPickupPlace));
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Search an item related to a user.
	 *
	 * @param name     User name
	 * @param password User password
	 * @param criteria Search criteria
	 */

	private void searchIem(String name, String password, Search criteria)
	{
		testSupport.action(String.format("Searching for a particular item owned by %s", name));

		// Search a particular user.
		assertDoesNotThrow(() -> {user = UserManager.getInstance().find(name, null, password);});
		assertNotNull(user);

		// Search a particular item.
		assertDoesNotThrow(() -> {items = ItemManager.getInstance().findByCriteria(user, UserRole.SELLER, criteria, null, null);});
		assertNotNull(items);
		assertNotEquals(0, items.size());
		item = items.get(0);
	}
}
