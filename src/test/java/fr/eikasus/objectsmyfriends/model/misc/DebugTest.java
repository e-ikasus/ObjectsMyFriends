package fr.eikasus.objectsmyfriends.model.misc;

import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DebugTest
{
	private static TestSupport<?> testSupport;

	private static DAOFactory daoFactory;

	/**
	 * Instantiate test helper objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Instantiate a dao factory object;
		daoFactory = new DAOFactory();
	}

	/**
	 * Free used resources.
	 */

	@AfterAll	public static void afterAll()
	{
		// Close dao factory object.
		daoFactory.close();
	}

	/**
	 * Clear the database.
	 */

	@Test public void clear()
	{
		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase(daoFactory);
	}

	/**
	 * Populate database for testing.
	 */

	@Test public void populate()
	{
		testSupport.action("Populating the database");

		testSupport.populateDatabase(daoFactory);
	}
}
