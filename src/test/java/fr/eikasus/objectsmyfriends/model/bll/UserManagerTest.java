package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class is used to test functionalities of the user manager.
 */

class UserManagerTest
{
	private static User user;

	private static TestSupport<User> testSupport;
	private static UserManager userManager;

	/**
	 * Instantiate test helper and UserManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Unique user manager instance.
		userManager = UserManager.getInstance();
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

		testSupport.populateDatabase();

		testSupport.action("Trying to find a user");
		/* -------------------------------------- */

		assertDoesNotThrow(() -> user = userManager.find("Fabien", null, "P@ssw0rd"));

		testSupport.action("Trying to archive the user");
		/* ---------------------------------------- */

		assertDoesNotThrow(() -> userManager.delete(user, true));

		testSupport.action("Trying to really delete the user");
		/* ------------------------------------------------- */

		assertDoesNotThrow(() -> userManager.delete(user, false));

		testSupport.action("Emptying the database");
		/* -------------------------------------- */

		testSupport.clearDatabase();
	}
}