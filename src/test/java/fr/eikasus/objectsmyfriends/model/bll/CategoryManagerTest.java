package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.CategoryManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * This class is used to test functionalities of the category manager.
 */

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({ManagerFactory.class, DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryManagerTest
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

	private CategoryManager categoryManager;

	private List<Category> categories;
	private Category newCategory;

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and CategoryManager objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Category manager instance.
		categoryManager = managerFactory.getCategoryManager();

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