package fr.eikasus.objectsmyfriends.model.misc;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.*;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.CategoryDAO;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ItemDAO;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.UserDAO;
import fr.eikasus.objectsmyfriends.model.dal.misc.ResultList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to simplify test purpose.
 * <p>
 * This class is used to facilitate the debug and test processes for the
 * application. It contains method that create data to save into the database
 * for this purpose. Data created can't be used to test application running.
 *
 * @see #createCategoryList()
 * @see #createPickupPlaceList(List)
 * @see #createImageList(List)
 * @see #createBidList(List, List)
 * @see #createUserList()
 * @see #createItemList(List, List)
 * @see #enterFunction()
 * @see #action(String)
 * @see #populateDatabase(DAOFactory)
 * @see #clearDatabase(DAOFactory)
 * @see #executeAndCompare(String, List, ResultList)
 * @see #daysAfter(Date, int)
 * @see #daysBefore(Date, int)
 * @see #searchItem(ManagerFactory, UserRole, String, String, Search)
 * @see #displayTable(List, List)
 */

public class TestSupport<T>
{
	private static class SmallItem
	{
		public String name;
		public String desc;
		public int cat;
		public int price;
		public int imageNbr;
		public String imageName;

		public SmallItem(String name, String desc, int cat, int price, int imageNbr, String imageName)
		{
			this.name = name;
			this.desc = desc;
			this.cat = cat;
			this.price = price;
			this.imageNbr = imageNbr;
			this.imageName = imageName;
		}
	}

	private static final String separation = "********************************************************************************";

	/* ************* */
	/* Class members */
	/* ************* */

	private static final List<SmallItem> defaultItems = new ArrayList<>();

	private static List<Item> foundItems;
	private static User foundUser;

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class.
	 * <p>
	 * After object instanced, default items are generated to be used later for
	 * appropriate methods of the class.
	 */

	public TestSupport()
	{
		defaultItems.add(new SmallItem("Serveur SuperMicro", "Serveur SuperMicro 8047R-7RFT+", 0, 500, 1, "server_sys-8047r-7rft+.jpg"));
		defaultItems.add(new SmallItem("Amiga", "Amiga 4000 Desktop", 0, 100, 1, "amiga_a4000d.jpg"));
		defaultItems.add(new SmallItem("Atari", "Atari 1040 STF", 0, 100, 1, "atari_1040stf.jpg"));
		defaultItems.add(new SmallItem("Cyberpunk 2077", "Jeu Cyberpunk 2077 sur Steam", 0, 70, 1, "cyberpunk_ 2077.jpg"));
		defaultItems.add(new SmallItem("The Witcher 3", "Jeu The Witcher 3 sur Steam", 2, 70, 1, "the_witcher 3.jpg"));
		defaultItems.add(new SmallItem("Carte Graphique", "Carte graphique Nvidia GTX 780TI", 0, 200, 1, "nvidia_gtx780ti.jpg"));
		defaultItems.add(new SmallItem("Processeur Xeon", "Processeur Xeon 7041 paxville SL8MA", 0, 50, 1, "xeon.jpg"));
		defaultItems.add(new SmallItem("Oscilloscope", "Oscilloscope PHILIPS PM3233 2x 10 MHz collection / vintage", 1, 60, 1, "oscilloscope.jpg"));
		defaultItems.add(new SmallItem("Tomb Raider", "Jeu Raise of the Tomb Raider sur Steam", 2, 70, 1, "tomb_raider.jpg"));
		defaultItems.add(new SmallItem("Fallout 4", "Jeu Fallout 4 sur Steam", 2, 70, 1, "fallout4.jpg"));
		defaultItems.add(new SmallItem("DOOM", "Jeu DOOM sur Steam", 2, 70, 1, "doom.jpg"));
		defaultItems.add(new SmallItem("Dark Souls III", "Jeu Dark Souls III sur Steam", 2, 70, 1, "darksouls_iii.jpg"));
		defaultItems.add(new SmallItem("Scie plongeante", "Scie plongeante FESTOOL TS55 FEBQ-PLUS FS", 4, 200, 1, "scie_plongeante_festool.jpg"));
		defaultItems.add(new SmallItem("Lenovo T480 20L5", "Vends Lenovo T480 20L05 datant de fin 2019. C'est un PC portable de gamme professionnelle, extrêmement robuste. Il dispose de deux batteries internes qui lui donnent une très grande autonomie, Windows affiche plus de 5h. Equipé d'un processeur Intel i5 récent et puissant.", 0, 150, 3, "lenovo.png"));
		defaultItems.add(new SmallItem("Acer Aspire R3-131t", "Acer Aspire R3-131T 1 1.6 » (500 Go, Intel Celeron Dual-Core, 1.6 GHz, 4 Go)", 0, 400, 3, "acer.jpg"));
		defaultItems.add(new SmallItem("Corsair RM650", "Corsair RM650 - 650w - 80 plus Gold", 0, 85, 1, "corsair_rm650.png"));
		defaultItems.add(new SmallItem("Carte Graphique AMD", "Carte Graphique AMD Radeon R9 285. fonctionnelle", 0, 20, 5, "radeon_r9_285.jpg"));
		defaultItems.add(new SmallItem("Carte Graphique Nvidia", "ends Carte GraphiqueNVIDIA GeForce RTX 3060 Ti Founders Edition 8Go GDDR6", 0, 550, 1, "nvidia_rtx_3060ti.jpg"));
		defaultItems.add(new SmallItem("Disque dur SSD", "Disque dur SSD interne SAMSUNG 870 EVO 500Go", 0, 50, 3, "ssd_samsung.jpg"));
		defaultItems.add(new SmallItem("RAM Crucial DDR3", "RAM Crucial 8GB DDR3L 1600 1.35v CT102464BF160B Laptop neuve", 0, 30, 4, "ram_crucial.jpg"));
		defaultItems.add(new SmallItem("Call of duty modern warfare 2", "Vends Call of duty modern warfare 2 ps5. Le jeu est comme neuf", 2, 54, 1, "call_of_duty.jpg"));
		defaultItems.add(new SmallItem("Final Fantasy Vii 7 Ps1", "Final Fantasy Vii 7 Ps1.Complet, bon état, version française", 2, 40, 4, "ff7_ps1.jpg"));
		defaultItems.add(new SmallItem("The Elder Scrolls V: Skyrim", "The Elder Scrolls V: Skyrim VR (Sony PlayStation 4, 2017)", 2, 10, 2, "skyrim.jpg"));
		defaultItems.add(new SmallItem("Station de soudage", "Station de soudage,JBC CD-2BQF numérique 130 W 90 - 450 °C", 1, 500, 1, "station_soudage_jbc.jpg"));
		defaultItems.add(new SmallItem("METRIX AX 322", "Alimentation de laboratoire réglable 2 x 30V 2 x 2.5A", 1, 150, 3, "ax322.jpg"));
		defaultItems.add(new SmallItem("Analyseur logique", "Analyseur logique Tektronix 1240 avec pack de communication d'imprimante Tektronix 1200C11", 1, 90, 5, "tektronix_1240.jpg"));
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a predefined list of categories used for debug purpose.
	 *
	 * @return List of categories.
	 */

	public List<Category> createCategoryList()
	{
		List<Category> categories = new ArrayList<>();

		categories.add(new Category("Informatique"));
		categories.add(new Category("Électronique"));
		categories.add(new Category("Jeux"));
		categories.add(new Category("Consoles"));
		categories.add(new Category("Bricolage"));
		categories.add(new Category("Musique"));

		return categories;
	}

	/**
	 * Create a list of pickup places.
	 * <p>
	 * Create a predefined list of pickup places used for debug purpose. Because a
	 * pickup place belongs to an item, a list of items need to be supplied in
	 * parameters.
	 *
	 * @param items List of items to use for creating pickup places.
	 *
	 * @return List of pickup places.
	 */

	public List<PickupPlace> createPickupPlaceList(@NotNull List<Item> items)
	{
		List<PickupPlace> pickupPlaces = new ArrayList<>();
		int index = items.size() - 1;

		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index--), "273 rue Pierre Legrand", "59800", "Lille"));
		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index--), "96 rue du Buisson", "59800", "Lille"));
		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index--), "13 rue Nationale", "59800", "Lille"));
		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index--), "8 place de la république", "59000", "Lille"));
		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index--), "87 rue Esquermoise", "59000", "Lille"));
		if (index >= 0)
			pickupPlaces.add(new PickupPlace(items.get(index), "20 rue Saint Nicolas", "59800", "Lille"));

		return pickupPlaces;
	}

	/**
	 * Create a list of images.
	 * <p>
	 * Create a predefined list of images used for debug purpose. Because an image
	 * belongs to an item, a list of items need to be supplied in parameters.
	 *
	 * @param items List of items to use for creating images.
	 *
	 * @return List of images.
	 */

	public List<Image> createImageList(@NotNull List<Item> items)
	{
		List<Image> images = new ArrayList<>();
		String[] baseName;

		// For each item defined.
		for (int i = 0; i < defaultItems.size(); i++)
		{
			// Separate the name and the extension.
			baseName = defaultItems.get(i).imageName.split("\\.");

			// Attach images to the current item.
			for (int j = 0; j < defaultItems.get(i).imageNbr; j++)
				images.add(new Image(items.get(i), String.format("%s_%d.%s",baseName[0], j + 1, baseName[1])));
		}

		// Return the newly created images list.
		return images;
	}

	/**
	 * Create a list of bids.
	 * <p>
	 * Create a predefined list of bids used for debug purpose. Because a bid
	 * belongs to an item owned by someone and made by someone, a list of items
	 * and user need to be supplied in parameters.
	 *
	 * @param items List of items to use for creating bids.
	 * @param users List of users to use for creating bids.
	 *
	 * @return List of bids.
	 */

	public List<Bid> createBidList(@NotNull List<Item> items, @NotNull List<User> users)
	{
		List<Bid> bids = new ArrayList<>();
		Random rnd = new Random();
		Item item;
		int price;

		Calendar calendar = Calendar.getInstance();

		// For each item except the first, create bids.
		for (int i = 1; i < items.size(); i++)
		{
			// Item to deal with.
			item = items.get(i);

			// Initial price.
			price = item.getInitialPrice();

			// Initial day
			calendar.setTime(item.getBiddingStart());

			// Some random user will make bids on it.
			for (int j = 0; j < rnd.nextInt(10); j++)
			{
				// Create a bid.
				bids.add(new Bid(users.get(rnd.nextInt(users.size())), item, calendar.getTime(), price));

				// Next possible price for the current item.
				price += rnd.nextInt(9) + 1;

				// A few minutes later.
				calendar.add(Calendar.MINUTE, rnd.nextInt(10) + 1);
			}
		}

		// Return the list of bids.
		return bids;
	}

	/**
	 * Create a list of users.
	 * <p>
	 * Create a predefined list of users used for debug purpose.
	 *
	 * @return List of users.
	 */

	public List<User> createUserList()
	{
		List<User> users = new ArrayList<>();

		users.add(new User("willy", "wilfried", "catteau", "wilfried.catteau@email.fr", "0123456789", "10 rue de la formation", "59000", "Lille", "P@ssw0rd", 1000, false));
		users.add(new User("AnneC", "Anne-Chloé", "Rioux", "anne-chloe.rioux@email.fr", "0123456789", "11 rue de la formation", "59000", "Lille", "P@ssw0rd", 900, false));
		users.add(new User("Fabien", "Fabien", "Chaudesaigue", "fabien-chaudesaigue@email.fr", "0123456789", "12 rue de la formation", "59000", "Lille", "P@ssw0rd", 100, false));
		users.add(new User("Manu", "Emmanuel", "Bodin", "ammanuel.bodin@email.fr", "0123456789", "13 rue de la formation", "59000", "Lille", "P@ssw0rd", 100, false));

		users.forEach(user -> {
			try
			{
				user.hashPassword();
			}
			catch (ModelException e)
			{
				// Nothing to do.
			}
		});

		return users;
	}

	/**
	 * Create a list of items.
	 * <p>
	 * Create a predefined list of items used for debug purpose. Because an item
	 * should belong to a buyer and is contained in a category, this information
	 * need to be supplied in parameters.
	 *
	 * @param users      List of users to use.
	 * @param categories List of categories to use.
	 *
	 * @return List of items.
	 */

	public List<Item> createItemList(List<User> users, List<Category> categories)
	{
		List<Item> items = new ArrayList<>();
		Date dateStart = new Date();
		Date dateEnd = new Date();
		ItemState itemState = ItemState.AC;
		User buyer;
		int sellerIndex = 2;
		int buyerIndex = 0;

		// Create the item list.
		for (SmallItem defaultItem : defaultItems)
		{
			computeDates(itemState, dateStart, dateEnd);

			// Buyer of the current item.
			buyer = (itemState == ItemState.SD) ? (users.get(buyerIndex++ & 0x01)) : (null);

			// Now, create the item.
			items.add(new Item(defaultItem.name,
				defaultItem.desc,
				new Date(dateStart.getTime()),
				new Date(dateEnd.getTime()),
				defaultItem.price,
				itemState,
				users.get(sellerIndex++),
				buyer,
				categories.get(defaultItem.cat)));

			// Go to the next state.
			if (itemState == ItemState.CA) itemState = ItemState.WT;
			else if (itemState == ItemState.WT) itemState = ItemState.AC;
			else if (itemState == ItemState.AC) itemState = ItemState.SD;
			else itemState = ItemState.CA;

			// Next seller.
			if (sellerIndex >= users.size()) sellerIndex = 2;
		}

		// Return the newly created item list.
		return items;
	}

	/**
	 * Compute the dates of an item sell.
	 *
	 * @param state     State of the item.
	 * @param dateStart Reference that will receive the start date.
	 * @param dateEnd   Reference that will receive the end date.
	 */

	private void computeDates(ItemState state, Date dateStart, Date dateEnd)
	{
		Random rnd = new Random();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		Date start, end;

		// Current date in ms.
		date.setTime(date.getTime() - date.getTime() % 1000);
		calendar.setTime(date);

		// For an item in the canceled state (...[]..C...) or (...[C].....).
		if (state == ItemState.CA)
		{
			// For a canceled item, the start date is always before the current date.
			// Here, a day before.
			calendar.add(Calendar.HOUR, Math.negateExact(rnd.nextInt(24 * 10) + 1));
			start = calendar.getTime();

			// The end date can be in the past, or the future.
			calendar.add(Calendar.HOUR, rnd.nextInt(24 * 10));
			end = calendar.getTime();
		}
		// For an item ready to be sold (..C.[].....).
		else if (state == ItemState.WT)
		{
			// The start date is always in the future.
			calendar.add(Calendar.HOUR, rnd.nextInt(24 * 10) + 1);
			start = calendar.getTime();

			// The end date is always after the start date.
			calendar.add(Calendar.HOUR, rnd.nextInt(24 * 10) + 1);
			end = calendar.getTime();
		}
		// For an item currently selling (...[C].....).
		else if (state == ItemState.AC)
		{
			// The end date is in a few minutes.
			calendar.add(Calendar.MINUTE, rnd.nextInt(10) + 1);
			end = calendar.getTime();

			// The sell began a few hours ago.
			calendar.add(Calendar.HOUR, Math.negateExact(rnd.nextInt(10) + 1));
			start = calendar.getTime();
		}
		// For an item sold (...[]..C...).
		else // if (state == ItemState.SD)
		{
			// The end date is in the past.
			calendar.add(Calendar.DATE, Math.negateExact(rnd.nextInt(5) + 1));
			end = calendar.getTime();

			// The sell began before the end date.
			calendar.add(Calendar.DATE, Math.negateExact(rnd.nextInt(5) + 1));
			start = calendar.getTime();
		}

		// Return results via objects supplied in parameters.
		dateStart.setTime(start.getTime());
		dateEnd.setTime(end.getTime());
	}

	/**
	 * Display a text header containing the calling name function.
	 */

	public void enterFunction()
	{
		StringBuilder stringBuilder = new StringBuilder();

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];

		String methodName = stackTraceElement.getMethodName();
		String fullClassName = stackTraceElement.getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);

		// Construct the string containing the function name do display.
		String functionName = String.format("Entering %s.%s() function", className, methodName);

		// Compute the number of characters to better display.
		int length = separation.length() - (functionName.length() + 4);

		stringBuilder.append("* ").append(functionName);
		while (length-- > 0) stringBuilder.append(" ");
		stringBuilder.append(" *");

		System.out.printf("\n%s\n", separation);
		System.out.printf("%s\n", stringBuilder);
		System.out.printf("%s\n\n", separation);
	}

	/**
	 * Display a text header containing the action that will be executed.
	 *
	 * @param message Message to display.
	 */

	public void action(@NotNull String message)
	{
		StringBuilder sb = new StringBuilder();

		// Create the separation line.
		for (int i = 0; i < message.length(); i++) sb.append("-");

		// Display the action.
		System.out.printf("\n%s\n", message);
		System.out.printf("%s\n\n", sb);
	}

	/**
	 * Fill the database.
	 * <p>
	 * This method fill the database with data. It will add categories, user,
	 * items, images, bids and pickup places. All data are in good associated.
	 * See {@code clearDatabase()} to clear the database.
	 *
	 * @param daoFactory DaoFactory used to access the database.
	 *
	 * @see #clearDatabase(DAOFactory) clearDatabase()
	 */

	public void populateDatabase(DAOFactory daoFactory)
	{
		List<Category> categories = createCategoryList();
		categories.forEach(category -> assertDoesNotThrow(() -> daoFactory.getCategoryDAO().save(category)));

		List<User> users = createUserList();
		users.forEach(user -> assertDoesNotThrow(() -> daoFactory.getUserDAO().save(user)));

		List<Item> items = createItemList(users, categories);
		items.forEach(item -> assertDoesNotThrow(() -> daoFactory.getItemDAO().save(item)));

		List<Image> images = createImageList(items);
		images.forEach(image -> assertDoesNotThrow(() -> daoFactory.getImageDAO().save(image)));

		List<PickupPlace> pickupPlaces = createPickupPlaceList(items);
		pickupPlaces.forEach(pickupPlace -> assertDoesNotThrow(() -> daoFactory.getPickupDAO().save(pickupPlace)));

		List<Bid> bids = createBidList(items, users);
		bids.forEach(bid -> assertDoesNotThrow(() -> daoFactory.getBidDAO().save(bid)));
	}

	/**
	 * Clear the database.
	 * <p>
	 * This method will remove all data in the database with the respect of all
	 * associations, which means images, bids and pickup places will be removed
	 * before items, and items before users and categories. See
	 * {@code populateDatabase()} to fill the database.
	 *
	 * @param daoFactory DaoFactory used to access the database.
	 *
	 * @see #populateDatabase(DAOFactory) populateDatabase()
	 */

	public void clearDatabase(DAOFactory daoFactory)
	{
		try
		{
			// Delete items which imply images, bids, pickup places.
			ItemDAO itemDAO = daoFactory.getItemDAO();
			for (Item item : itemDAO.find()) itemDAO.delete(item);

			// Delete categories.
			CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
			for (Category category : categoryDAO.find()) categoryDAO.deleteById(category.getIdentifier());

			// Delete users.
			UserDAO userDAO = daoFactory.getUserDAO();
			for (User user : userDAO.find()) userDAO.deleteById(user.getIdentifier());
		}
		catch (Exception exception)
		{
			// Nothing to do but draw a message.
			System.out.println(exception.getMessage());
		}
	}

	/**
	 * Display an action message and execute the supplied function.
	 *
	 * @param attended List of attended objects after operation.
	 * @param message  Message to display
	 * @param action   Function to execute
	 */

	public void executeAndCompare(String message, @NotNull List<T> attended, @NotNull ResultList<T> action)
	{
		// Display message
		action(message);

		// Empty the list to accept new results.
		attended.clear();

		// Execute the action.
		List<T> result = assertDoesNotThrow(action::execute);

		// Compare the results with what is attended.
		assertEquals(attended, result);

		// Display the result.
		result.forEach(System.out::println);
	}

	/**
	 * Compute a date that is days after.
	 * <p>
	 * This method compute a date that is {@code Nbr} days after the supplied
	 * date. If this date is null, then the current date is used.
	 *
	 * @param date Date reference or null for the current
	 * @param nbr  Nbr of days
	 *
	 * @return Computed date.
	 */

	public Date daysAfter(Date date, int nbr)
	{
		Calendar calendar = Calendar.getInstance();

		// If no date is supplied, take the current.
		if (date == null) date = new Date();

		// Initialise the calendar with the appropriate date.
		calendar.setTime(date);

		// Compute the new date.
		calendar.add(Calendar.DATE, nbr);

		// Return the result.
		return calendar.getTime();
	}

	/**
	 * Compute a date that is days before.
	 * <p>
	 * This method compute a date that is {@code Nbr} days before the supplied
	 * date. If this date is null, then the current date is used.
	 *
	 * @param date Date reference or null for the current
	 * @param nbr  Nbr of days
	 *
	 * @return Computed date.
	 */

	public Date daysBefore(Date date, int nbr)
	{
		Calendar calendar = Calendar.getInstance();

		// If no date is supplied, take the current.
		if (date == null) date = new Date();

		// Initialise the calendar with the appropriate date.
		calendar.setTime(date);

		// Compute the new date.
		calendar.add(Calendar.DATE, nbr * -1);

		// Return the result.
		return calendar.getTime();
	}

	/**
	 * Search items related to a user.
	 * <p>
	 * This method search items owen by u user according to criteria supplied in
	 * parameter. The result is a HashMap containing in the form of key/value
	 * pairs the result, one for the user found name "user", and the other for the
	 * items named "items".
	 *
	 * @param managerFactory Instance of the manager factory to use.
	 * @param role           User role (seller or buyer).
	 * @param name           User name.
	 * @param password       User password.
	 * @param criteria       Search criteria.
	 *
	 * @return Found user and items.
	 */

	public HashMap<String, Object> searchItem(ManagerFactory managerFactory, UserRole role, String name, String password, Search criteria)
	{
		HashMap<String, Object> result = new HashMap<>();

		action(String.format("Searching for particular items owned by %s", name));

		// Search a particular user.
		assertDoesNotThrow(() -> {foundUser = managerFactory.getUserManager().find(name, null, password);});
		assertNotNull(foundUser);

		// Search particular items.
		assertDoesNotThrow(() -> {foundItems = managerFactory.getItemManager().findByCriteria(foundUser, role, criteria, null, null);});
		assertNotNull(foundItems);

		// Put the result in the collection.
		result.put("user", foundUser);
		result.put("items", foundItems);

		// return the result.
		return result;
	}

	/**
	 * Display a table of data
	 *
	 * @param labels Labels used in the header.
	 * @param lines  Content of the table body
	 */

	public void displayTable(@NotNull List<String> labels, List<List<String>> lines)
	{
		List<Integer> headers = new ArrayList<>();
		StringBuilder separation = new StringBuilder();
		StringBuilder tableLine;
		String tablePattern, content;
		int tableWidth;

		// Initialise the length for each table column.
		for (String label : labels) headers.add(label.length());

		// Compute each column width according to each line content.
		for (List<String> line : lines)
		{
			for (int col = 0; col < Math.min(headers.size(), line.size()); col++)
			{
				// Take the maximum width between current width column and content.
				headers.set(col, Math.max(headers.get(col), line.get(col).length()));
			}
		}

		// Computer the table width.
		tableWidth = 1;
		for (Integer header : headers)  tableWidth += header + 2 + 1;

		// Construct the separation line between header and body, and start and end.
		while (tableWidth-- != 0) separation.append("-");

		// Display the first table line.
		System.out.printf("\n%s\n", separation);

		// Create a StringBuilder for the table header.
		tableLine = new StringBuilder();

		// Create header table.
		for (int col = 0; col < headers.size(); col++)
		{
			// Construct the format string.
			tablePattern = String.format("| %s%ds ", "%", headers.get(col));

			// Add the label into the table.
			tableLine.append(String.format(tablePattern, labels.get(col)));
		}

		// Finish the line and display it.
		System.out.println(tableLine.append("|"));

		// Display a separation between, header and body.
		System.out.printf("%s\n", separation);

		// Display each line of the table.
		for (List<String> line : lines)
		{
			// Create a StringBuilder for the current line.
			tableLine = new StringBuilder();

			// For each column, display its aligned contain according to column size.
			for (int col = 0; col < headers.size(); col++)
			{
				// Content of the current column which is empty outside the line.
				content = (col < line.size()) ? (line.get(col)) : ("");

				// Construct the format string.
				tablePattern = String.format("| %s%ds ", "%", headers.get(col));

				// Put the content into the table.
				tableLine.append(String.format(tablePattern, content));
			}

			// Finish the line and display it.
			System.out.println(tableLine.append("|"));
		}

		// Display the last table line.
		System.out.printf("%s\n\n", separation);
	}
}
