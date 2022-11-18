package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.dal.annotations.BidDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.BidDAO;
import fr.eikasus.objectsmyfriends.model.misc.BidId;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Class used to implement the bid data access object.
 * <p>
 * This class supplies all the necessary methods to handle bid objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager.
 * <p>
 * This is the implementation for database.
 *
 * @see #findBestBid(Item)
 */

@ApplicationScoped @BidDAODB
public class BidDAOImpl extends GenericDAOImpl<Bid, BidId> implements BidDAO
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private final static String JPQL_FIND_BID = "SELECT b FROM Bid b WHERE b.item = :i ORDER BY b.price DESC";

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public BidDAOImpl()
	{
		super(Bid.class);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Find the highest bid on an item.
	 * <p>
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
