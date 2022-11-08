package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.BidDAO;
import fr.eikasus.objectsmyfriends.model.misc.BidId;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Class used to implement the bid data access object.
 * <p></p>
 * This class content all the necessary stuff that is exclusively related to
 * handling bids.
 *
 * @see #findBestBid(Item)
 */

public class BidDAOImpl extends GenericDAOImpl<Bid, BidId> implements BidDAO
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private final static String JPQL_FIND_BID = "SELECT b FROM Bid b WHERE b.item = :i ORDER BY b.price DESC";

	/* ************ */
	/* Constructors */
	/* ************ */

	public BidDAOImpl(DAOFactory daoFactory)
	{
		super(Bid.class, daoFactory);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Find the highest bid on an item.
	 * <p></p>
	 * This method find the highest bed made on the item supplied in parameter.
	 * With the bid, it is then possible to access the user who made it.
	 *
	 * @param item Item to search for.
	 *
	 * @return Highest bid or null if none already exist.
	 *
	 * @throws ModelException In case of problem.
	 */

	public Bid findBestBid(Item item) throws ModelException
	{
		try
		{
			// Create tue request for the highest bid on the supplied item
			TypedQuery<Bid> query = entityManager.createQuery(JPQL_FIND_BID, Bid.class);

			// Item to deal with.
			query.setParameter("i", item);

			// Only one, the highest.
			query.setMaxResults(1);

			// Retrieve the highest bid.
			List<Bid> bids = query.getResultList();

			// Return the result.
			return ((bids.size() != 0) ? (bids.get(0)) : (null));
		}
		catch (Exception exc)
		{
			throw new ModelException(exc, ModelError.UNABLE_TO_RETRIEVE_ENTITY);
		}
	}
}
