package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ImageManagerTest
{
	private static TestSupport<Image> testSupport;
	private static ImageManager imageManager;

	/**
	 * Instantiate test helper and ImageManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique image manager instance.
		imageManager =ImageManager.getInstance();
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

		// Search for seller and its active items.
		HashMap<String, Object> sellerData = testSupport.searchItem(UserRole.SELLER, "Fabien", "P@ssw0rd", new Search().setMyCurrentSales());

		Item item = ((List<Item>) sellerData.get("items")).get(0);

		testSupport.action(String.format("Adding images to item <<%s>>", item.getName()));
		assertDoesNotThrow(() -> imageManager.add(item, "path1"));
		assertDoesNotThrow(() -> imageManager.add(item, "path2"));

		testSupport.action(String.format("Displaying all images of item <<%s>>", item.getName()));
		item.getImages().forEach(System.out::println);

		testSupport.action(String.format("Deleting recent created images of item <<%s>>", item.getName()));
		item.getImages().forEach((image) -> assertDoesNotThrow(() -> {if (image.getPath().compareTo("path1") == 0) imageManager.delete(image);}));
		item.getImages().forEach((image) -> assertDoesNotThrow(() -> {if (image.getPath().compareTo("path2") == 0) imageManager.delete(image);}));

		testSupport.action(String.format("Displaying all images of item <<%s>>", item.getName()));
		item.getImages().forEach(System.out::println);
	}
}
