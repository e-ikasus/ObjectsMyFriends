package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Interface used for implementing pickup place manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage pickup place items. Methods present in the interface implementation
 * are allowed to use as many as data access objects required to accomplish
 * their goal. The access of one of data access object method by the controllers
 * is strictly forbidden.
 *
 * @see #add(Item, String, String, String)
 * @see #find(Item)
 * @see #update(PickupPlace, HashMap)
 * @see #delete(PickupPlace)
 */

public interface PickupManager
{
	/**
	 * Set the manager factory.
	 * <p>
	 * This method retrieves the manager factory to allow the instance methods to
	 * access others managers.
	 *
	 * @param managerFactory Manager factory instance.
	 */

	void setManagerFactory(ManagerFactory managerFactory);

	/**
	 * Create a pickup place.
	 * <p>
	 * This method create a pickup place and save it into the persistence unit.
	 * The newly created pickup place is attached to the supplied item after
	 * successfully saved.
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

	PickupPlace add(Item item, String street, String zipCode, String city) throws ModelException;

	/**
	 * Find the pickup place that belong to an item.
	 * <p>
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

	PickupPlace find(@NotNull Item item) throws ModelException;

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

	void update(@NotNull PickupPlace pickupPlace, @NotNull HashMap<String, Object> properties) throws ModelException;

	/**
	 * Delete a pickup place.
	 * <p>
	 * This method delete a pickup place from the persistence unit. Once the
	 * pickup place is deleted, his previously attached item is updated too.
	 *
	 * @param pickupPlace Pickup place to delete
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(@NotNull PickupPlace pickupPlace) throws ModelException;
}
