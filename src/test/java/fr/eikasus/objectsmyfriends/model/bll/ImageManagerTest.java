package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.ImageManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({ManagerFactory.class, DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageManagerTest
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

	private ImageManager imageManager;

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and ImageManager objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique image manager instance.
		imageManager = managerFactory.getImageManager();

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

	@SuppressWarnings("unchecked")
	@Test void all()
	{
		testSupport.enterFunction();

		// Search for seller and its active items.
		HashMap<String, Object> sellerData = testSupport.searchItem(managerFactory, UserRole.SELLER, "Fabien", "P@ssw0rd", new Search().setMyCurrentSales());

		Item item = ((List<Item>) sellerData.get("items")).get(0);

		testSupport.action(String.format("Adding images to item <<%s>>", item.getName()));
		assertDoesNotThrow(() -> imageManager.add(item, "image1.jpg"));
		assertDoesNotThrow(() -> imageManager.add(item, "image2.jpg"));

		testSupport.action(String.format("Displaying all images of item <<%s>>", item.getName()));
		item.getImages().forEach(System.out::println);

		testSupport.action(String.format("Deleting recent created images of item <<%s>>", item.getName()));
		item.getImages().forEach((image) -> assertDoesNotThrow(() -> {if (image.getPath().compareTo("image1.jpg") == 0) imageManager.delete(image);}));
		item.getImages().forEach((image) -> assertDoesNotThrow(() -> {if (image.getPath().compareTo("image1.jpg") == 0) imageManager.delete(image);}));

		testSupport.action(String.format("Displaying all images of item <<%s>>", item.getName()));
		item.getImages().forEach(System.out::println);
	}
}
