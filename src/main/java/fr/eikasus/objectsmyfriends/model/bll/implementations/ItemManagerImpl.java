package fr.eikasus.objectsmyfriends.model.bll.implementations;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ItemDAO;
import fr.eikasus.objectsmyfriends.model.misc.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Item manager class.
 * <p></p>
 * This class is used to manage items according to the business logic. It should
 * be used by the controllers to handle items like adding, deleting and so. The
 * access of one of data access object method by the controllers is strictly
 * forbidden.
 *
 * @see #add(String, String, Date, Date, int, User, Category) add()
 * @see #find(Long) find()
 * @see #findByCriteria(User, UserRole, Search, Category, String)
 * findByCriteria()
 * @see #delete(List) delete()
 * @see #deleteByCriteria(User, UserRole, Search, Category, String)
 * deleteByCriteria()
 */

public class ItemManagerImpl extends GenericManagerImpl implements ItemManager
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String VALIDATE_NAME = "^[0-9A-Za-zéèëêùàÉÈËÊÙÀ'+ -]+$";
	private static final String VALIDATE_DESCRIPTION = "^[0-9A-Za-zéèëêùàÉÈËÊÙÀ'+ -]+$";
	private static final long SCALE_FACTOR = 60 * 1000;

	/* ********************** */
	/* Methods used as macros */
	/* ********************** */

	private static Date SCALE_DATE(Date d) {return new Date(d.getTime() - d.getTime() % SCALE_FACTOR);}

	/* ************* */
	/* Class members */
	/* ************* */

	// Data access object instance.
	private final ItemDAO dao;

	private final Pattern nameCheck;
	private final Pattern descriptionCheck;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Constructor of the class.
	 */

	public ItemManagerImpl(ManagerFactory managerFactory)
	{
		super(managerFactory);

		// Data access object for user entity operations.
		dao = managerFactory.getDaoFactory().getItemDAO();

		// Property validators.
		nameCheck = Pattern.compile(VALIDATE_NAME);
		descriptionCheck = Pattern.compile(VALIDATE_DESCRIPTION);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a full qualified item.
	 * <p></p>
	 * This method create and save an item into the database. All parameters are
	 * required to complete the action. If one of that parameters is wrong, an
	 * exception occurs and nothing is saved into the database.
	 *
	 * @param name         Short name of the item.
	 * @param description  Description of the item.
	 * @param biddingStart Start of the bidding.
	 * @param biddingEnd   End of the bidding.
	 * @param initialPrice Initial price of the idem.
	 * @param seller       Seller of the item.
	 * @param category     Category of the item.
	 *
	 * @return Newly created item and added to the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public Item add(String name, String description, Date biddingStart, Date biddingEnd, int initialPrice, User seller, Category category) throws ModelException
	{
		Item item = new Item(name, description, biddingStart, biddingEnd, initialPrice, null, seller, null, category);

		try
		{
			// Check the validity of the item to be created
			validate(item);

			// Add the item to the database.
			dao.save(item);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_CREATE_ITEM);
		}

		// Return the newly created item.
		return item;
	}

	/**
	 * Search a specific item.
	 * <p></p>
	 * Search a specific item in the database whose identifier is the one supplied
	 * in parameter. If this supplied parameter is null, then all items are
	 * returned by this method.
	 *
	 * @param identifier Item identifier or null for all.
	 *
	 * @return List of items found.
	 *
	 * @throws ModelException In case of pb.
	 */

	public List<Item> find(Long identifier) throws ModelException
	{
		List<Item> items = new ArrayList<>();

		try
		{
			// Find a specific item or all.
			if (identifier == null) items = dao.find();
			else items.add(dao.find(identifier));
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.ITEM_NOT_FOUND);
		}

		// Return the result
		return items;
	}

	/**
	 * Search items using criteria.
	 * <p></p>
	 * Search items in the database thar are related to the supplied user. His
	 * role determine the nature of the search criteria.
	 *
	 * @param user     User to search for.
	 * @param role     Role of the supplied user (seller or buyer)
	 * @param search   Searching criteria.
	 * @param category Category of items searched.
	 * @param keywords Keywords on item name.
	 *
	 * @return List of items found.
	 *
	 * @throws ModelException In case of pb.
	 */

	public List<Item> findByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException
	{
		try
		{
			// Find items that belong to supplied criteria.
			return dao.findByCriteria(user, role, search, category, keywords);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.ITEM_NOT_FOUND);
		}
	}

	/**
	 * Update item properties.
	 * <p>
	 * This method update the properties of the supplied item. Each property new
	 * value should be supplied as a pair of key/value, a key representing the
	 * exact name or the user property, including case. If a key as an undefined
	 * property name, an exception is generated.
	 *
	 * @param item       Item to be updated.
	 * @param properties Item properties to update.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void update(Item item, HashMap<String, Object> properties) throws ModelException
	{
		Item updatedItem = new Item(item);
		String string;
		Integer integer;
		Date date;
		User user;
		Category category;
		PickupPlace pickupPlace;

		// Name, description and category can only be modified if the item is not
		// sold or canceled.
		if ( (updatedItem.getState() == ItemState.AC) || (updatedItem.getState() == ItemState.WT) )
		{
			if ((string = (String) properties.get("name")) != null)
			{
				properties.remove("name");
				updatedItem.setName(string);
			}

			if ((string = (String) properties.get("description")) != null)
			{
				properties.remove("description");
				updatedItem.setDescription(string);
			}

			if ((category = (Category) properties.get("category")) != null)
			{
				properties.remove("category");
				updatedItem.setCategory(category);
			}
		}

		// Owner, dates and initial price can only be modified if the item is
		// waiting for selling.
		if (updatedItem.getState() == ItemState.WT)
		{
			if ((date = (Date) properties.get("biddingStart")) != null)
			{
				properties.remove("biddingStart");
				updatedItem.setBiddingStart(date);
			}

			if ((date = (Date) properties.get("biddingEnd")) != null)
			{
				properties.remove("biddingEnd");
				updatedItem.setBiddingEnd(date);
			}

			if ((integer = (Integer) properties.get("initialPrice")) != null)
			{
				properties.remove("initialPrice");
				updatedItem.setInitialPrice(integer);
			}

			if ((user = (User) properties.get("seller")) != null)
			{
				properties.remove("seller");
				updatedItem.setSeller(user);
			}
		}

		// pickup PLace, buyer and final price can only be modified if the item is
		// sold.
		if (updatedItem.getState() == ItemState.SD)
		{
			if ((pickupPlace = (PickupPlace) properties.get("pickupPlace")) != null)
			{
				properties.remove("pickupPlace");
				updatedItem.setPickupPlace(pickupPlace);
			}

			if ((user = (User) properties.get("buyer")) != null)
			{
				properties.remove("buyer");
				updatedItem.setBuyer(user);
			}

			if ((integer = (Integer) properties.get("finalPrice")) != null)
			{
				properties.remove("finalPrice");
				updatedItem.setFinalPrice(integer);
			}
		}

		// If properties always exist, it means there is a problem.
		if (!properties.isEmpty())
			throw new ModelException(null, ModelError.UNABLE_TO_UPDATE_ITEM);

		try
		{
			// Check the user validity.
			validate(updatedItem);

			// Update the user into the database.
			dao.update(updatedItem);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_UPDATE_ITEM);
		}
	}

	/**
	 * Delete items.
	 * <p></p>
	 * This method delete the supplied items from the database. This should be
	 * used even if one item has to te deleted. Be aware that all associated
	 * information will be deleted to, like images, bids and pickup place.
	 *
	 * @param items List of item to delete.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void delete(@NotNull List<Item> items) throws ModelException
	{
		try
		{
			// Delete each items.
			for (Item item : items) dao.delete(item);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_DELETE_ITEM);
		}
	}

	/**
	 * Delete items using criteria.
	 * <p></p>
	 * This method delete items according to criteria supplied in parameter. The
	 * relation between each criterion is a logical and. Except the user and its
	 * role, all other parameters can be null.
	 *
	 * @param user     User to search for.
	 * @param role     Role of the supplied user (seller or buyer)
	 * @param search   Searching criteria.
	 * @param category Category of items searched.
	 * @param keywords Keywords on item name.
	 *
	 * @throws ModelException In case of pb.
	 */

	public void deleteByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException
	{
		try
		{
			// Delete items that belong to supplied criteria.
			dao.deleteByCriteria(user, role, search, category, keywords);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_DELETE_ITEM);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify the validity of the supplied item.
	 * <p></p>
	 * This method check the validity of an item to be inserted in the database.
	 * If one of his properties is invalid, an exception occur containing errors
	 * detected. If errors occur, the item state is not defined. If the state is
	 * already defined (that is not null), the start and end dates are not
	 * checked again: This case can append if the item is updated.
	 *
	 * @param item Item to verify
	 *
	 * @throws ModelException In case of problem.
	 */

	private void validate(@NotNull Item item) throws ModelException
	{
		ModelException exception = new ModelException();
		String string;
		Date current = SCALE_DATE(new Date());
		Date start, end;
		ItemState newState;
		boolean stateCondition, propertyCondition1, propertyCondition2;

		// Check the name validity.
		if (((string = item.getName()) == null) || (!nameCheck.matcher(string).matches()) || (string.length() > Item.MAX_LENGTH_NAME))
			exception.add(ModelError.INVALID_ITEM_NAME);

		// Check the description validity.
		if (((string = item.getDescription()) == null) || (!descriptionCheck.matcher(string).matches()) || (string.length() > Item.MAX_LENGTH_DESCRIPTION))
			exception.add(ModelError.INVALID_ITEM_DESCRIPTION);

		// Check if the seller is defined.
		if (item.getSeller() == null) exception.add(ModelError.INVALID_ITEM_SELLER);

		// Check if the category is defined.
		if (item.getCategory() == null)
			exception.add(ModelError.INVALID_ITEM_CATEGORY);

		// Check if the validity of the credit.
		if (item.getInitialPrice() <= 0)
			exception.add(ModelError.INVALID_ITEM_PRICE);

		// If the item state is not define, this means the dates need to be checked
		// and the state determine in regard to them.
		if (item.getState() == null)
		{
			// Change the start date scaling if defined.
			if (item.getBiddingStart() != null)
				item.setBiddingStart(SCALE_DATE(item.getBiddingStart()));

			// Change the end date scaling if defined.
			if (item.getBiddingEnd() != null)
				item.setBiddingEnd(SCALE_DATE(item.getBiddingEnd()));

			// The bidding start date can't be in the past if it is defined.
			if (item.getBiddingStart() == null)
				item.setBiddingStart(current);
			else if (item.getBiddingStart().compareTo(current) < 0)
				exception.add(ModelError.INVALID_ITEM_START_DATE);

			// The bidding end date can't be before the bidding start date.
			if ((item.getBiddingEnd() == null) || (item.getBiddingEnd().compareTo(item.getBiddingStart()) <= 0))
				exception.add(ModelError.INVALID_ITEM_END_DATE);

			// If there is an error, don't do the remainder.
			if (exception.hasError()) throw exception;

			// At this point, the start date can be equal to the current or in the futur
			// which will determine the state of the item.
			item.setState((item.getBiddingStart().compareTo(current) > 0) ? (ItemState.WT) : (ItemState.AC));
		}
		else
		{
			start = item.getBiddingStart();
			end = item.getBiddingEnd();

			if (end.compareTo(start) < 0)
			{
				exception.add(ModelError.INVALID_ITEM_END_DATE);
			}
			else
			{
				// In the past, the item can only be sold or canceled.
				if (end.compareTo(current) <= 0) newState = (item.getBuyer() != null) ? (ItemState.CA) : (ItemState.SD);
				// In the future, it is waiting to be sold.
				else if (start.compareTo(current) > 0) newState = ItemState.WT;
				// In other cases, it is in selling.
				else newState = ItemState.AC;

				stateCondition = ((newState == ItemState.CA) || (newState == ItemState.WT) || (newState == ItemState.AC));
				propertyCondition1 = ((item.getBuyer() == null) && (item.getFinalPrice() == 0));
				propertyCondition2 = ((item.getBuyer() != null) && (item.getFinalPrice() != 0));

				if (((!stateCondition) || (!propertyCondition1)) && ((stateCondition) || (!propertyCondition2)))
					exception.add(ModelError.INVALID_ITEM_STATE);
				else
					item.setState(newState);
			}
		}

		// If there is an error, throws an exception.
		if (exception.hasError()) throw exception;
	}
}
