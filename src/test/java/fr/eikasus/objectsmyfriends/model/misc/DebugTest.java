package fr.eikasus.objectsmyfriends.model.misc;

import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@EnableAutoWeld
@ActivateScopes({RequestScoped.class})
@AddPackages({DAOFactory.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DebugTest
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Class used for test facilities.
	private TestSupport<?> testSupport;

	// Injected DAO factory by weld junit extension.
	@Inject private DAOFactory daoFactory;

	/* ******************************* */
	/* Before and after helper methods */
	/* ******************************* */

	/**
	 * Instantiate test helper objects.
	 */

	@BeforeAll public void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

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

		// Populate the database.
		testSupport.populateDatabase(daoFactory);
	}
}
