package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.UserManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class is used to test functionalities of the user manager.
 */

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({ManagerFactory.class, DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagerTest
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

	private static UserManager userManager;

	private static User user;

	/* ******************************* */
	/* Before and after tester methods */
	/* ******************************* */

	/**
	 * Instantiate test helper and UserManager objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique user manager instance.
		userManager = managerFactory.getUserManager();

		// Clean the database.
		afterEach();
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
	 * Test the ability to add a user.
	 */

	@Test void add()
	{
		testSupport.enterFunction();

		testSupport.action("Trying to create a user with no password");
		/* --------------------------------------------------------- */

		assertThrows(ModelException.class, () -> {
			user = userManager.add(
				"fuser",
				"first",
				"user",
				"first-user@address.fr",
				"0123456789",
				"1 rue en france",
				"12345",
				"Une ville en France",
				null,
				100,
				false);
		});

		testSupport.action("Creating a user");
		/* -------------------------------- */

		assertDoesNotThrow(() -> {
			user = userManager.add(
				"fuser",
				"first",
				"user",
				"first-user@address.fr",
				"0123456789",
				"1 rue en france",
				"12345",
				"Une ville en France",
				"P@ssw0rd",
				100,
				false);
		});

		System.out.println(user.toString());

		testSupport.action("Deleting the user");
		/* ------------------------------------------ */

		assertDoesNotThrow(() -> userManager.delete(user, false));
	}

	/**
	 * Test the ability to update a user.
	 */

	@Test void update()
	{
		testSupport.enterFunction();

		HashMap<String, Object> properties = new HashMap<>();

		testSupport.action("Creating a user");
		/* -------------------------------- */

		assertDoesNotThrow(() -> {
			user = userManager.add(
				"suser",
				"second",
				"user",
				"second-user@address.fr",
				"0123456789",
				"1 rue en france",
				"12345",
				"Une ville en France",
				"P@ssw0rd",
				100,
				false);
		});

		System.out.println(user.toString());

		testSupport.action("Trying to updating a user with wrong data");
		/* ---------------------------------------------------------- */

		properties.put("zipCode", "&é");
		assertThrows(ModelException.class, () -> userManager.update(user, properties));

		testSupport.action("Trying to updating a user with good data");
		/* --------------------------------------------------------- */

		properties.clear();
		properties.put("firstName", "SECOND");
		assertDoesNotThrow(() -> userManager.update(user, properties));
		System.out.println(user.toString());

		testSupport.action("Trying to delete the user");
		/* ------------------------------------------ */

		assertDoesNotThrow(() -> userManager.delete(user, false));
	}

	/**
	 * Test the ability to find a user.
	 */

	@Test void find()
	{
		testSupport.enterFunction();

		testSupport.action("Creating a user");
		/* -------------------------------- */

		assertDoesNotThrow(() -> {
			userManager.add(
				"suser",
				"second",
				"user",
				"second-user@address.fr",
				"0123456789",
				"1 rue en france",
				"12345",
				"Une ville en France",
				"P@ssw0rd",
				100,
				false);
		});

		testSupport.action("Searching a newly created user with a wrong name");
		/* ----------------------------------------------------------------- */

		assertThrows(ModelException.class, () -> user = userManager.find("S2USER", null, "P@ssw0rd"));

		testSupport.action("Searching a newly created user with a wrong password");
		/* --------------------------------------------------------------------- */

		assertThrows(ModelException.class, () -> user = userManager.find("suser", null, "P@zssw0rd"));

		testSupport.action("Searching a newly created user with a correct name");
		/* ------------------------------------------------------------------- */

		assertDoesNotThrow(() -> user = userManager.find("suser", null, "P@ssw0rd"));

		System.out.println(user.toString());

		testSupport.action("Trying to delete the user");
		/* ------------------------------------------ */

		assertDoesNotThrow(() -> userManager.delete(user, false));
	}

	/**
	 * Test the ability to delete a user.
	 */

	@Test void delete()
	{
		testSupport.enterFunction();

		testSupport.action("Populating the database");
		/* ---------------------------------------- */

		testSupport.populateDatabase(daoFactory);

		testSupport.action("Trying to find a user");
		/* -------------------------------------- */

		assertDoesNotThrow(() -> user = userManager.find("Fabien", null, "P@ssw0rd"));

		testSupport.action("Trying to archive the user");
		/* ------------------------------------------- */

		assertDoesNotThrow(() -> userManager.delete(user, true));

		testSupport.action("Trying to really delete the user");
		/* ------------------------------------------------- */

		assertDoesNotThrow(() -> userManager.delete(user, false));
	}
}