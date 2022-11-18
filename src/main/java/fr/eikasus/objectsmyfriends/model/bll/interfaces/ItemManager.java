package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Interface used for implementing item manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage items. Methods present in the interface implementation are allowed
 * to use as many as data access objects required to accomplish their goal. The
 * access of one of data access object method by the controllers is strictly
 * forbidden.
 *
 * @see #add(String, String, Date, Date, int, User, Category)
 * @see #find(Long)
 * @see #findByCriteria(User, UserRole, Search, Category, String)
 * @see #update(Item, HashMap)
 * @see #delete(List)
 * @see #deleteByCriteria(User, UserRole, Search, Category, String)
 */

public interface ItemManager
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
	 * Create a full qualified item.
	 * <p>
	 * This method creates and saves an item into the persistence unit. All
	 * parameters are required to complete the action. If one of that parameters
	 * is wrong, an exception occurs and nothing is saved.
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

	Item add(String name, String description, Date biddingStart, Date biddingEnd, int initialPrice, User seller, Category category) throws ModelException;

	/**
	 * Search a specific item.
	 * <p>
	 * Search a specific item in the persistence unit whose identifier is the one
	 * supplied in parameter. If this supplied parameter is null, then all items
	 * are returned by this method.
	 *
	 * @param identifier Item identifier or null for all.
	 *
	 * @return List of items found.
	 *
	 * @throws ModelException In case of pb.
	 */

	List<Item> find(Long identifier) throws ModelException;

	/**
	 * Search items using criteria.
	 * <p>
	 * Search items in the persistence unit that are related to the supplied user.
	 * His role determine the nature of the search criteria.
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

	List<Item> findByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException;

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

	void update(Item item, HashMap<String, Object> properties) throws ModelException;

	/**
	 * Delete items.
	 * <p>
	 * This method deletes the supplied items from the persistence unit. This
	 * should be used even if one item has to te deleted. Be aware that all
	 * associated information will be deleted too, like images, bids and pickup
	 * place.
	 *
	 * @param items List of item to delete.
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(@NotNull List<Item> items) throws ModelException;

	/**
	 * Delete items using criteria.
	 * <p>
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

	void deleteByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException;
}
