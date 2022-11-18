package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.BidId;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

/**
 * Interface used for implementing the bid data access object.
 * <p>
 * This interface defines all the specific methods to handle bid objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager. The generic dao interface, that this interface
 * implements too, implements the methods commun to all data access objects.
 *
 * @see #findBestBid(Item)
 */

public interface BidDAO extends GenericDAO<Bid, BidId>
{
	/**
	 * Find the highest bid on an item.
	 * <p>
	 * This method find the highest bid made on the item supplied in parameter.
	 * With the bid, it is then possible to access the user who made it.
	 *
	 * @param item Item to search for.
	 *
	 * @return Highest bid or null if none already exist.
	 *
	 * @throws ModelException In case of problem.
	 */

	Bid findBestBid(Item item) throws ModelException;
}
