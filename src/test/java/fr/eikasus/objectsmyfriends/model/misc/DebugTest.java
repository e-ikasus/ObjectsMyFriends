package fr.eikasus.objectsmyfriends.model.misc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DebugTest
{
	private static TestSupport<?> testSupport;

	/**
	 * Instantiate test helper objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();
	}

	/**
	 * Populate database for testing.
	 */

	@Test public void populate()
	{
		testSupport.action("Populating the database");

		testSupport.populateDatabase();
	}

	/**
	 * Clear the database.
	 */

	@Test public void clear()
	{
		testSupport.action("Cleaning the database");

		// Empty the database.
		testSupport.clearDatabase();
	}
}
