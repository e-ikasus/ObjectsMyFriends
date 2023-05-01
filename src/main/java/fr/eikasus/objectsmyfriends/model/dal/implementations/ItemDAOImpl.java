package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.annotations.ItemDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.ItemDAO;
import fr.eikasus.objectsmyfriends.model.misc.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import java.util.List;

/**
 * Class used to implement the item data access object.
 * <p>
 * This class supplies all the necessary methods to handle item objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager.
 * <p>
 * This is the implementation for database.
 *
 * @see #findByCriteria(User, UserRole, Search, Category, String)
 * @see #deleteByCriteria(User, UserRole, Search, Category, String)
 */

@ApplicationScoped @ItemDAODB
public class ItemDAOImpl extends GenericDAOImpl<Item, Long> implements ItemDAO
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	// As a seller, all cases.
	private final static String JPQL_SELECT1 = "WHERE (i.seller = :p1 AND (";

	// As a buyer, common part of the request for not wanted bids.
	private final static String JPQL_SELECT2 = "WHERE (";

	// As a buyer, common part of the request for wanted bids.
	private final static String JPQL_SELECT3 = "JOIN Bid b ON b.item = i WHERE (";

	// As a buyer, the items I bought.
	private final static String JPQL_SELECT4 = "(i.buyer = :p1)";

	// As buyer, the items that are available.
	private final static String JPQL_SELECT5 = "(i.state = '" + ItemState.AC + "')";

	// As buyer, the items I did a bid.
	private final static String JPQL_SELECT6 = "(i.state = '" + ItemState.AC + "' AND b.user = :p2)";

	// If a category is required.
	private final static String JPQL_SELECT7 = " AND (i.category = :p3)";

	// If wanted bids need to be retrieved.
	private final static String JPQL_SELECT8 = " GROUP BY i";

	// Criteria for seller.
	private final static String JPQL_SELECT9 = "(i.state = :c%d)";

	// Request start.
	private final static String JPQL_SELECT10 = "SELECT i FROM Item i ";

	// Request start.
	private final static String JPQL_SELECT11 = "DELETE FROM Item i ";

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public ItemDAOImpl()
	{
		super(Item.class);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

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

	@Override public List<Item> findByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException
	{
		List<Item> result;
		TypedQuery<Item> query;

		// Create the call of the check procedure.
		StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("CheckItems")
			.setParameter("asked_item", 0L)
			.setParameter("trace", true)
			.setParameter("simulate", false)
			.setParameter("append", true);

		// Scan items to check their state against the current date.
		storedProcedureQuery.execute();

		// Create the request for searching requested items.
		if (role == UserRole.BUYER) query = createJPQLForBuyer(JPQL_SELECT10, user, search, category, keywords);
		else query = createJPQLForSeller(JPQL_SELECT10, user, search, category, keywords);

		// If an error occurred while generating the query.
		if (query == null) throw new ModelException(null, ModelError.UNABLE_TO_DELETE_ENTITY);

		try
		{
			// Retrieve the requested items
			result = query.getResultList();
		}
		catch (Exception e)
		{
			// If something goes wrong.
			throw new ModelException(e, ModelError.UNABLE_TO_RETRIEVE_ENTITIES);
		}

		// Return the retrieved list of items.
		return result;
	}

	/**
	 * Delete items using criteria.
	 * <p>
	 * This method delete items according to criteria supplied in parameter. The
	 * relation between each criterion is a logical and. Except the user and its
	 * role, all other parameters can be null.
	 * <p>
	 * NO PERSISTENCE CONTEXT SHOULD BE ACTIVE TO USE THIS METHOD.
	 *
	 * @param user     User to search for.
	 * @param role     Role of the supplied user (seller or buyer)
	 * @param search   Searching criteria.
	 * @param category Category of items searched.
	 * @param keywords Keywords on item name.
	 *
	 * @throws ModelException In case of pb.
	 */

	@Override public void deleteByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException
	{
		Query query;

		// Create the request for searching requested items.
		if (role == UserRole.BUYER) query = createJPQLForBuyer(JPQL_SELECT11, user, search, category, keywords);
		else query = createJPQLForSeller(JPQL_SELECT11, user, search, category, keywords);

		// If an error occurred while generating the query.
		if (query == null) throw new ModelException(null, ModelError.UNABLE_TO_DELETE_ENTITY);

		execute(true, ModelError.UNABLE_TO_DELETE_ENTITY, query::executeUpdate);
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Create a query to search for items.
	 * <p>
	 * This method search for items that belongs to parameters supplied. Note that
	 * the user need to be a seller. For sellers, use createJPQLFForBuyer()
	 * instead.
	 *
	 * @param clauseStart Start of the clause.
	 * @param user        User for which a search is related to.
	 * @param search      Search criteria.
	 * @param category    Category of items searched.
	 * @param keywords    Keywords for name items
	 *
	 * @return Query used to process the request.
	 */

	@SuppressWarnings({"StringBufferMayBeStringBuilder", "unchecked"})
	private @Nullable TypedQuery<Item> createJPQLForSeller(String clauseStart, @NotNull User user, @NotNull Search search, Category category, String keywords)
	{
		StringBuffer request = new StringBuffer();
		Query query;
		int nbrCriteria = 0;

		// If no criterion is defined for a seller.
		if (!search.asSeller()) return null;

		// First part of the request.
		request.append(clauseStart).append(JPQL_SELECT1);

		// Determine the number of parameters.
		if (search.isMyCurrentSales()) nbrCriteria++;
		if (search.isMyWaitingSales()) nbrCriteria++;
		if (search.isMyEndedSales()) nbrCriteria++;
		if (search.isMyCanceledSales()) nbrCriteria++;

		// Put the parameter place in the request.
		for (int i = 0; i < nbrCriteria; i++)
		{
			// If this statement is not the first.
			if (i != 0) request.append(" OR ");

			// Complete the request.
			request.append(String.format(JPQL_SELECT9, i + 1));
		}

		// Close first condition.
		request.append(")");

		// If a category is supplied, complete the request.
		if (category != null) request.append(JPQL_SELECT7);

		// If keywords are supplied, add them.
		if (keywords != null) request.append(addKeywords(keywords));

		// Close the request
		request.append(")");

		// Create the query.
		if (clauseStart.indexOf("SELECT") == 0) query = entityManager.createQuery(request.toString(), Item.class);
		else query = entityManager.createQuery(request.toString());

		// First used parameter.
		nbrCriteria = 1;

		// Add the user to the request.
		query.setParameter("p1", user);

		// If items currently sell need to be retrieved.
		if (search.isMyCurrentSales())
			query.setParameter(String.format("c%d", nbrCriteria++), ItemState.AC);

		// If items waiting for selling need to be retrieved.
		if (search.isMyWaitingSales())
			query.setParameter(String.format("c%d", nbrCriteria++), ItemState.WT);

		// If items sold need to be retrieved.
		if (search.isMyEndedSales())
			query.setParameter(String.format("c%d", nbrCriteria++), ItemState.SD);

		// If items canceled need to be retrieved.
		if (search.isMyCanceledSales())
			query.setParameter(String.format("c%d", nbrCriteria), ItemState.CA);

		// Add the category if required.
		if (category != null) query.setParameter("p3", category);

		// Return the found items.
		return (TypedQuery<Item>) query;
	}

	/**
	 * Create a query to search for items.
	 * <p>
	 * This method search for items that belongs to parameters supplied. Note that
	 * the user need to be a buyer. For sellers, use createJPQLFForSeller()
	 * instead.
	 *
	 * @param clauseStart Start of the clause.
	 * @param user        User for which a search is related to.
	 * @param search      Search criteria.
	 * @param category    Category of items searched.
	 * @param keywords    Keywords for name items
	 *
	 * @return Query used to process the request.
	 */

	@SuppressWarnings("StringBufferMayBeStringBuilder")
	private @Nullable TypedQuery<Item> createJPQLForBuyer(String clauseStart, User user, @NotNull Search search, Category category, String keywords)
	{
		StringBuffer request = new StringBuffer();
		boolean addOr = false;

		// If no criterion is defined for a buyer.
		if (!search.asBuyer()) return null;

		// User can be null, but in that case all criteria aren't allowed.
		if ( (user == null) && ((search.isMyWonBids()) || (search.isMyCurrentBids())) ) return null;

		// First part of the request.
		request.append(clauseStart).append((search.isMyCurrentBids()) ? (JPQL_SELECT3) : (JPQL_SELECT2));

		// If the items that the supplied user bought need to be retrieved.
		if (search.isMyWonBids())
		{
			// Complete the request.
			request.append(JPQL_SELECT4);

			// Now, the next statement is not the first, so added an 'or' is required.
			addOr = true;
		}

		// If the available items for buying need to be retrieved.
		if (search.isOpenedBids())
		{
			// If this statement is not the first.
			if (addOr) request.append(" OR ");

			// Complete the request.
			request.append(JPQL_SELECT5);

			// Now, the next statement is not the first, so added an 'or' is required.
			addOr = true;
		}

		// If the current bids for the supplied user need to be retrieved.
		if (search.isMyCurrentBids())
		{
			// If this statement is not the first.
			if (addOr) request.append(" OR ");

			// Complete the request.
			request.append(JPQL_SELECT6);
		}

		// The first where clause is closed.
		request.append(")");

		// If a category is supplied, complete the request.
		if (category != null) request.append(JPQL_SELECT7);

		// If keywords are supplied, add them.
		if (keywords != null) request.append(addKeywords(keywords));

		// If an aggregation need to be used.
		if (search.isMyCurrentBids()) request.append(JPQL_SELECT8);

		// Create the query.
		TypedQuery<Item> query = entityManager.createQuery(request.toString(), Item.class);

		if (search.isMyWonBids()) query.setParameter("p1", user);

		if (search.isMyCurrentBids()) query.setParameter("p2", user);

		if (category != null) query.setParameter("p3", category);

		return query;
	}

	/**
	 * Create a string to add to the database request.
	 *
	 * @param keywords Keywords to add to the request.
	 *
	 * @return String to add to the database request.
	 */

	@SuppressWarnings("StringBufferMayBeStringBuilder")
	private @NotNull String addKeywords(@NotNull String keywords)
	{
		StringBuffer sb = new StringBuffer();
		String[] splitKeyWords = keywords.split(" ");

		sb.append(" AND (");

		for (int i = 0; i < splitKeyWords.length; i++)
		{
			sb.append("(i.name LIKE '%").append(splitKeyWords[i]).append("%')");

			if (i != (splitKeyWords.length - 1)) sb.append(" OR ");
		}

		return sb.append(")").toString();
	}
}
