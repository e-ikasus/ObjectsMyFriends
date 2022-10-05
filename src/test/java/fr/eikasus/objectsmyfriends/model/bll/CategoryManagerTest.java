package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	private static CategoryManager categoryManager;

	/**
	 * Instantiate test helper and CategoryManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique category manager instance.
		categoryManager = CategoryManager.getInstance();
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

		testSupport.action(String.format("Trying to delete <<%s>> category", categories.get(0).getLabel()));
		assertDoesNotThrow(() -> categoryManager.delete(categories.get(0)));

		testSupport.action("Trying to delete newly created category");
		assertDoesNotThrow(() -> categoryManager.delete(newCategory));
	}
}