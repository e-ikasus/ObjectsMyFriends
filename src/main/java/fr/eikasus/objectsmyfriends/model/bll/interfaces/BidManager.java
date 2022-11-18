package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used for implementing bid manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage bids on items. Methods present in the interface implementation are
 * allowed to use as many as data access objects required to accomplish their
 * goal. The access of one of data access object method by the controllers is
 * strictly forbidden. There is no method to retrieve bids because they are
 * contained in the item itself. Retrieving an item allow then access to its
 * bids.
 *
 * @see #add(User, Item, int)
 * @see #delete(User)
 */

public interface BidManager
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
	 * Make a bid on an item.
	 * <p>
	 * This method is used to create a bid made by a user on an item with the
	 * supplied price. There are some reasons why this operation can't be done.
	 *
	 * @param user  User that make the bid.
	 * @param item  Item on which the bid is made.
	 * @param price Price of the bid
	 *
	 * @return New bid successfully created.
	 *
	 * @throws ModelException In case of problem.
	 */

	Bid add(@NotNull User user, @NotNull Item item, int price) throws ModelException;

	/**
	 * Delete bids.
	 * <p>
	 * This method delete all the bids that belongs to the supplied user.
	 *
	 * @param user User for which delete the bids.
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(@NotNull User user) throws ModelException;
}
