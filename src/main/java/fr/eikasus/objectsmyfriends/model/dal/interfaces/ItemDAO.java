package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;

import java.util.List;

/**
 * Interface used for implementing the item data access object.
 * <p>
 * This interface defines all the specific methods to handle item objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager. The generic dao interface, that this interface
 * implements too, implements the methods commun to all data access objects.
 *
 * @see #findByCriteria(User, UserRole, Search, Category, String)
 * @see #deleteByCriteria(User, UserRole, Search, Category, String)
 */

public interface ItemDAO extends GenericDAO<Item, Long>
{
	/**
	 * Search items using criteria.
	 * <p>
	 * This method search items according to criteria supplied in parameter. The
	 * relation between each criterion is a logical and. Except the user and its
	 * role, all other parameters can be null.
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

	List<Item> findByCriteria(User user, UserRole role, Search search, Category category, String keywords) throws ModelException;

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

	void deleteByCriteria(User user, UserRole role, Search search, Category category, String keywords) throws ModelException;
}
