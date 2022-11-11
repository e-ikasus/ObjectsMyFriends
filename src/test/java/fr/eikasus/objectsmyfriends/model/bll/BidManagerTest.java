package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bll.interfaces.BidManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.TestSupport;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BidManagerTest
{
	private static User buyer1, buyer2;

	private final List<String> tableHeader = new ArrayList<>();
	private final List<List<String>> tableLines = new ArrayList<>();

	HashMap<String, Object> sellerData = new HashMap<>();

	private static TestSupport<Bid> testSupport;
	private static ManagerFactory managerFactory;
	private static BidManager bidManager;

	/**
	 * Instantiate test helper and PickupManager objects.
	 */

	@BeforeAll public static void beforeAll()
	{
		// Class used for testing purposes.
		testSupport = new TestSupport<>();

		// Instantiate a manager factory object.
		managerFactory = new ManagerFactory();

		// Unique category manager instance.
		bidManager = managerFactory.getBidManager();

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

		// Search for seller and its active items.
		sellerData = testSupport.searchItem(managerFactory, UserRole.SELLER, "Fabien", "P@ssw0rd", new Search().setMyCurrentSales());

		testSupport.action("Searching for the first buyer");
		assertDoesNotThrow(() -> buyer1 = managerFactory.getUserManager().find("Willy", null, "P@ssw0rd"));

		testSupport.action("Searching for the second buyer");
		assertDoesNotThrow(() -> buyer2 = managerFactory.getUserManager().find("AnneC", null, "P@ssw0rd"));

		List<Item> items = (List<Item>) sellerData.get("items");

		doBid(buyer1, items.get(0), 200, false);

		doBid(buyer1, items.get(0), 500, true);

		doBid(buyer1, items.get(0), 600, false);

		doBid(buyer2, items.get(0), 600, true);

		doBid(buyer2, items.get(1), 700, false);

		doBid(buyer1, items.get(0), 900, true);

		testSupport.displayTable(tableHeader, tableLines);
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Try to make a bid.
	 * <p></p>
	 * This method try to make a bid on an item made by a user at a price which
	 * are supplied in parameter. the Attended parameter indicate if the operation
	 * is excepted to be successful or not.
	 *
	 * @param user     User who made the bid.
	 * @param item     Item on which the bid is made.
	 * @param myPrice  Price of the bid.
	 * @param attended WHat is attended (success or fail).
	 */

	private void doBid(User user, Item item, int myPrice, boolean attended)
	{
		StringBuilder sb = new StringBuilder();
		boolean success;
		int beforeBid, afterBid, oldItemPrice, newItemPrice;
		String oldOwner, newOwner;

		try
		{
			// Search the actual max bid.
			Bid maxBid = managerFactory.getDaoFactory().getBidDAO().findBestBid(item);

			// Information message of what is doing.
			sb.append(String.format("Trying to register a bid of <<%d>> made by <<%s>> on item <<%s>> currently at <<%d>> price. This operation should%s succeed",
				myPrice,
				user.getUsername(),
				item.getName(),
				(maxBid == null) ? (item.getInitialPrice()) : (maxBid.getPrice()),
				(attended) ? ("") : ("n't")));

			// If a bid is defined, display the actual winner.
			if (maxBid != null)
				sb.append(String.format(" currently owned by %s", maxBid.getUser().getUsername()));

			// Retrieve the "old" state information.
			beforeBid = user.getCredit();
			oldOwner = (maxBid == null) ? (item.getSeller().getUsername()) : (maxBid.getUser().getUsername());
			oldItemPrice = (maxBid == null) ? (item.getInitialPrice()) : (maxBid.getPrice());

			// Display action message.
			testSupport.action(sb.toString());

			bidManager.add(user, item, myPrice);

			// Retrieve the "new" state information.
			afterBid = user.getCredit();
			newOwner = user.getUsername();
			newItemPrice = myPrice;

			// The operation succeed.
			success = true;

			// If not already done, create the table header.
			if (tableHeader.size() == 0)
			{
				tableHeader.add("Username");
				tableHeader.add("Credit before");
				tableHeader.add("Credit after");
				tableHeader.add("Bid");
				tableHeader.add("Item name");
				tableHeader.add("Old item price");
				tableHeader.add("New item price");
				tableHeader.add("Old item owner");
				tableHeader.add("New item owner");
			}

			// Add a line in the table for the new bid.
			tableLines.add(new ArrayList<>());
			int index = tableLines.size() - 1;
			tableLines.get(index).add(user.getUsername());
			tableLines.get(index).add(Integer.toString(beforeBid));
			tableLines.get(index).add(Integer.toString(afterBid));
			tableLines.get(index).add(Integer.toString(myPrice));
			tableLines.get(index).add(item.getName());
			tableLines.get(index).add(Integer.toString(oldItemPrice));
			tableLines.get(index).add(Integer.toString(newItemPrice));
			tableLines.get(index).add(oldOwner);
			tableLines.get(index).add(newOwner);
		}
		catch (Exception e)
		{
			// Display the error.
			System.out.println(e.getMessage());

			// The operation failed.
			success = false;
		}

		// Display the result.
		System.out.println((attended ^ success) ? ("Failed") : ("Passed"));

		// Is the result expected ?
		assertEquals(attended, success);
	}
}
