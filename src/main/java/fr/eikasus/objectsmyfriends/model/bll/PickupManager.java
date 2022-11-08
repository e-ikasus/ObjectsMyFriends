package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.PickupDAO;
import fr.eikasus.objectsmyfriends.model.misc.ItemState;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Pickup pLace manager class.
 * <p></p>
 * This class is used to manage pickup places according to the business logic.
 * It should be used by the controllers to handle pickup places like adding,
 * deleting and so. The access of one of data access object method by the
 * controllers is strictly forbidden.
 *
 * @see #add(Item, String, String, String) add()
 * @see #find(Item)
 * @see #update(PickupPlace, HashMap)
 * @see #delete(PickupPlace)
 */

public class PickupManager extends GenericManager
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String VALIDATE_STREET = "^[0-9A-Za-zéèëêùàÉÈËÊÙÀ' -]+$";
	private static final String VALIDATE_ZIPCODE = "^[0-9A-Za-z]+$";
	private static final String VALIDATE_CITY = "^[A-Za-zéèëêùàÉÈËÊÙÀ' -]+$";

	/* ************* */
	/* Class members */
	/* ************* */

	// Data access object instance.
	private final PickupDAO dao;

	private final Pattern streetCheck;
	private final Pattern zipcodeCheck;
	private final Pattern cityCheck;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Constructor of the class.
	 */

	public PickupManager(ManagerFactory managerFactory)
	{
		super(managerFactory);

		// Data access object for user entity operations.
		dao = managerFactory.getDaoFactory().getPickupDAO();

		// Property validators.
		streetCheck = Pattern.compile(VALIDATE_STREET);
		zipcodeCheck = Pattern.compile(VALIDATE_ZIPCODE);
		cityCheck = Pattern.compile(VALIDATE_CITY);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a pickup place.
	 * <p></p>
	 * This method create a pickup place and save it into the database. The newly
	 * created pickup place is attached to the supplied item after successfully
	 * saved. Creating a pickup place for an item not in waiting or active state
	 * will result in an exception generated.
	 *
	 * @param item    Item to add the pickup place to
	 * @param street  Street where to pick up the item.
	 * @param zipCode Zip code where to pick up the item.
	 * @param city    City  to pick up the item.
	 *
	 * @return The newly created pickup place.
	 *
	 * @throws ModelException In case of problem.
	 */

	public PickupPlace add(Item item, String street, String zipCode, String city) throws ModelException
	{
		// Impossible to add a pickup place in an item that already has one.
		if  ((item == null) || (item.getPickupPlace() != null))
			throw new ModelException(null, ModelError.UNABLE_TO_CREATE_PICKUP_PLACE);

		// Create a pickup place.
		PickupPlace pickupPlace = new PickupPlace(null, street, zipCode, city);

		// This is necessary to not update the item after this pickup place is
		// created.
		pickupPlace.setItem(item);

		try
		{
			// Verify the validity of the pickup place.
			validate(pickupPlace);

			// Save it into the database.
			dao.save(pickupPlace);

			// Once the pickup place is successfully created, item can be updated.
			item.setPickupPlace(pickupPlace);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_CREATE_PICKUP_PLACE);
		}

		// Return the newly created pickup place.
		return pickupPlace;
	}

	/**
	 * Find the pickup place that belong to an item.
	 * <p></p>
	 * This method find the pickup place related to the item supplied in parameter.
	 * If no pickup place is found, that means the item should be picked up at the
	 * seller address.
	 *
	 * @param item Item for which finding the pickup place.
	 *
	 * @return Found pickup place.
	 *
	 * @throws ModelException In case of problem or if nothing is found.
	 */

	public PickupPlace find(@NotNull Item item) throws ModelException
	{
		// Check if the item has a pickup place defined.
		if (item.getPickupPlace() == null)
			throw new ModelException(null, ModelError.PICKUP_PLACE_NOT_FOUND);

		try
		{
			// Find the requested pickup place.
			return dao.find(item.getIdentifier());
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.PICKUP_PLACE_NOT_FOUND);
		}
	}

	/**
	 * Update pickup place properties.
	 * <p>
	 * This method update the properties of the supplied pickup place. Each
	 * property new value should be supplied as a pair of key/value, a key
	 * representing the exact name of the pickup place property, including case.
	 * If a key as an undefined property name, an exception is generated.
	 *
	 * @param pickupPlace Pickup place to be updated.
	 * @param properties  Pickup place properties to update.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void update(@NotNull PickupPlace pickupPlace, @NotNull HashMap<String, Object> properties) throws ModelException
	{
		try
		{
			PickupPlace updatedPickupPlace = new PickupPlace(pickupPlace);

			setEntityProperty(updatedPickupPlace, properties, "street", false);

			setEntityProperty(updatedPickupPlace, properties, "zipCode", false);

			setEntityProperty(updatedPickupPlace, properties, "city", true);

			// Check the pickup place validity.
			validate(updatedPickupPlace);

			// Update the pickup place into the database.
			dao.update(updatedPickupPlace);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_UPDATE_PICKUP_PLACE);
		}
	}

	/**
	 * Delete a pickup place.
	 * <p></p>
	 * This method delete a pickup place from the database. Only items that are in
	 * waiting or active state can have their pickup place deleted. Once the
	 * pickup place is deleted, his previously attached item is updated too.
	 *
	 * @param pickupPlace Pickup place to delete
	 *
	 * @throws ModelException In case of problem.
	 */

	public void delete(@NotNull PickupPlace pickupPlace) throws ModelException
	{
		// Normally, the item exist.
		if (pickupPlace.getItem() == null) throw new ModelException(null, ModelError.UNABLE_TO_DELETE_PICKUP_PLACE);

		// Item state
		ItemState state = pickupPlace.getItem().getState();

		// Only an active or waiting item can have his pickup place deleted.
		if ((state != ItemState.AC) && (state != ItemState.WT)) throw new ModelException(null, ModelError.UNABLE_TO_DELETE_PICKUP_PLACE);

		try
		{
			// Remove the pickup place from the item.
			pickupPlace.getItem().setPickupPlace(null);

			// And delete it from the database.
			dao.delete(pickupPlace);
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.UNABLE_TO_DELETE_PICKUP_PLACE);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify a pickup place.
	 * <p>
	 * This method check the validity of a pickup place that will be saved in the
	 * database. If one of his properties is invalid, an exception occur which
	 * contain errors detected.
	 *
	 * @param pickupPlace PickupPlace to verify.
	 *
	 * @throws ModelException In case of wrong information.
	 */

	private void validate(@NotNull PickupPlace pickupPlace) throws ModelException
	{
		ModelException exception = new ModelException();
		String string;

		// Check the street validity.
		if (((string = pickupPlace.getStreet()) == null) || (!streetCheck.matcher(string).matches()) || (string.length() > PickupPlace.MAX_LENGTH_STREET))
			exception.add(ModelError.INVALID_PICKUP_PLACE_STREET);

		// Check the zipcode validity.
		if (((string = pickupPlace.getZipCode()) == null) || (!zipcodeCheck.matcher(string).matches()) || (string.length() > PickupPlace.MAX_LENGTH_ZIPCODE))
			exception.add(ModelError.INVALID_PICKUP_PLACE_ZIPCODE);

		// Check the city validity.
		if (((string = pickupPlace.getCity()) == null) || (!cityCheck.matcher(string).matches()) || (string.length() > PickupPlace.MAX_LENGTH_ZIPCODE))
			exception.add(ModelError.INVALID_PICKUP_PLACE_ZIPCODE);

		// Check the item.
		if (pickupPlace.getItem() == null)
			exception.add(ModelError.INVALID_PICKUP_PLACE_ITEM);

		// Generate an exception in case of error.
		if (exception.hasError()) throw exception;

		// All properties are fine, but can the item accept this pickup place ?
		if ((pickupPlace.getItem().getState() != ItemState.WT) && (pickupPlace.getItem().getState() != ItemState.AC))
			exception.add(ModelError.INVALID_PICKUP_PLACE_ITEM);

		// Generate an exception in case of error.
		if (exception.hasError()) throw exception;
	}
}
