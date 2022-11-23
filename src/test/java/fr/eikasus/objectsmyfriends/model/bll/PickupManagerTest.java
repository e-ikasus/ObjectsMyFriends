package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.PickupManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({ManagerFactory.class, DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PickupManagerTest
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

	private PickupManager pickupManager;

	private Item item;
	private User user;

	private List<Item> items;

	private PickupPlace newPickupPlace;

	HashMap<String, Object> properties = new HashMap<>();

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and PickupManager objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique category manager instance.
		pickupManager = managerFactory.getPickupManager();

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

	@SuppressWarnings("SameParameterValue")
	private void searchIem(String name, String password, Search criteria)
	{
		testSupport.action(String.format("Searching for a particular item owned by %s", name));

		// Search a particular user.
		assertDoesNotThrow(() -> {user = managerFactory.getUserManager().find(name, null, password);});
		assertNotNull(user);

		// Search a particular item.
		assertDoesNotThrow(() -> {items = managerFactory.getItemManager().findByCriteria(user, UserRole.SELLER, criteria, null, null);});
		assertNotNull(items);
		assertNotEquals(0, items.size());
		item = items.get(0);
	}
}
