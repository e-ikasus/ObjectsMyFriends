package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.CategoryManager;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * This class is used to test functionalities of the category manager.
 */

class CategoryManagerTest
{
	private static List<Category> categories;
	private static Category newCategory;

	private static TestSupport<Category> testSupport;
	private static ManagerFactory managerFactory;
	private static CategoryManager categoryManager;

	/**
	 * Instantiate test helper and CategoryManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Instantiate a manager factory object.
		managerFactory = new ManagerFactory();

		// Category manager instance.
		categoryManager = managerFactory.getCategoryManager();

		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase(managerFactory.getDaoFactory());
	}

	/**
	 * Free used resources.
	 */

	@AfterAll public static void afterAll()
	{
		// Close the manager factory object.
		managerFactory.close();
	}

	/**
	 * Populate database for each test.
	 */

	@BeforeEach void setUp()
	{
		testSupport.action("Populating the database");

		testSupport.populateDatabase(managerFactory.getDaoFactory());
	}

	/**
	 * Clear the database after each test.
	 */

	@AfterEach public void afterEach()
	{
		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase(managerFactory.getDaoFactory());
	}

	/* ************** */
	/* Tester methods */
	/* ************** */

	@Test void all()
	{
		testSupport.enterFunction();

		testSupport.action("Trying to find <<Informatique>> category");
		assertDoesNotThrow(() -> categories = categoryManager.find("Informatique"));
		categories.forEach(System.out::println);

		testSupport.action("Trying to find all categories");
		assertDoesNotThrow(() -> categories = categoryManager.find(null));
		categories.forEach(System.out::println);

		testSupport.action("Trying to insert a new category");
		assertDoesNotThrow(() -> newCategory = categoryManager.add("Ameublement"));
		System.out.println(newCategory);

		testSupport.action("Trying to update the newly created category");
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("label", newCategory.getLabel().toUpperCase());
		assertDoesNotThrow(() -> categoryManager.update(newCategory, properties));
		System.out.println(newCategory);

		//testSupport.action(String.format("Trying to delete <<%s>> category", categories.get(0).getLabel()));
		//assertThrows(ModelException.class, () -> categoryManager.delete(categories.get(0)));

		testSupport.action("Trying to delete newly created category");
		assertDoesNotThrow(() -> categoryManager.delete(newCategory));
	}
}