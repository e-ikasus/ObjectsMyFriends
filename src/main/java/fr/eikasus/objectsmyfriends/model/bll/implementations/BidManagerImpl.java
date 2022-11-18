package fr.eikasus.objectsmyfriends.model.bll.implementations;

import fr.eikasus.objectsmyfriends.model.bll.annotations.BidManagerDB;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.BidManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Bid manager class.
 * <p>
 * This class is used to manage bids according to the business logic. It should
 * be used by the controllers to handle bids like adding, deleting and so. The
 * access of one of data access object method by the controllers is strictly
 * forbidden. There is no method to retrieve bids from the database because they
 * are contained in the item itself. Retrieving an item allow then access to its
 * bids.
 *
 * @see #add(User, Item, int)
 * @see #delete(User)
 */

@ApplicationScoped @BidManagerDB
public class BidManagerImpl extends GenericManagerImpl implements BidManager
{
	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Make a bid on an item.
	 * <p>
	 * This method is used to create a bid made by a user on an item with the
	 * supplied price. There are some reasons why this operation can't be done.
	 * See the {@code validate()} method for more details.
	 *
	 * @param user  User that make the bid.
	 * @param item  Item on which the bid is made.
	 * @param price Price of the bid
	 *
	 * @return New bid successfully created.
	 *
	 * @throws ModelException In case of problem.
	 */

	public synchronized Bid add(@NotNull User user, @NotNull Item item, int price) throws ModelException
	{
		// Create a new bid.
		Bid newBid = new Bid(user, null, new Date(), price);

		// This is necessary to not update the item after this bid is created.
		newBid.setItem(item);

		try
		{
			// Check the validity of the bid
			Bid oldBid = validate(newBid);

			// Save it into the database.
			daoFactory.getBidDAO().save(newBid);

			HashMap<String, Object> properties = new HashMap<>();

			// Refund the previous winner if there is one.
			if (oldBid != null)
			{
				properties.put("credit", oldBid.getUser().getCredit() + oldBid.getPrice());
				managerFactory.getUserManager().update(oldBid.getUser(), properties);
			}

			// Reduce the price bid to the new winner credit.
			properties.put("credit", newBid.getUser().getCredit() - newBid.getPrice());
			managerFactory.getUserManager().update(newBid.getUser(), properties);

			// Update the item to finish the operation by adding the new bid to it.
			newBid.getItem().addBid(newBid);
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.UNABLE_TO_CREATE_BID);
		}

		// Return the new created bid.
		return newBid;
	}

	/**
	 * Delete bids.
	 * <p>
	 * This method delete all the bids that belongs to the supplied user.
	 *
	 * @param user User for which delete the bids.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void delete(@NotNull User user) throws ModelException
	{
		List<Bid> bids;

		try
		{
			// Search bids related to the user.
			bids = daoFactory.getBidDAO().findByProperty("user", user);
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.BID_NOT_FOUND);
		}

		try
		{
			// For each found bid.
			for (Bid bid : bids)
			{
				// Delete if from the database.
				daoFactory.getBidDAO().delete(bid);

				// And remove the bid from the item bid list.
				bid.getItem().removeBid(bid);
			}
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.UNABLE_TO_DELETE_BID);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify a bid.
	 * <p>
	 * This method check the validity of a bid that will be saved in the database.
	 * If one of these properties is invalid, an exception occur which contain
	 * errors detected. A bid is considered invalid if one of these properties is
	 * not defined of course, but if his price is not higher than the highest bid
	 * made on the item, or at least equal to the item initial price if no bid is
	 * already made. Of course, a user can't make a higher bid on him.
	 *
	 * @param bid Bid to verify.
	 *
	 * @return Highest bid of the item related to the bid supplied.
	 *
	 * @throws ModelException In case of wrong information.
	 */

	private Bid validate(@NotNull Bid bid) throws ModelException
	{
		ModelException exception = new ModelException();
		Bid maxBid;

		try
		{
			// Check the user validity.
			if (bid.getUser() == null) exception.add(ModelError.INVALID_BID_USER);

			// Check the item validity.
			if (bid.getItem() == null) exception.add(ModelError.INVALID_BID_ITEM);

			// Check the date validity.
			if (bid.getDate() == null) exception.add(ModelError.INVALID_BID_DATE);

			// If there is already an error, it is usefulness to continue.
			if (exception.hasError()) throw exception;

			// Check the validity of the bid price.
			if (bid.getPrice() < 0) throw exception.add(ModelError.INVALID_BID_PRICE);

			// Check if the user has really the necessary credit.
			if (bid.getPrice() > bid.getUser().getCredit())
				throw exception.add(ModelError.INVALID_BID_PRICE);

			// Retrieve the maximum bid made on this item and check it if exists.
			if ((maxBid = daoFactory.getBidDAO().findBestBid(bid.getItem())) != null)
			{
				// A user can't bid on him.
				if (maxBid.getUser() == bid.getUser())
					throw exception.add(ModelError.INVALID_BID_USER);

				// Check if the bid is high enough if this is not the first.
				if (bid.getPrice() <= maxBid.getPrice())
					exception.add(ModelError.INVALID_BID_PRICE);
			}
			else
			{
				// If this is the first, it is at least equal to the item initial price.
				if (bid.getPrice() < bid.getItem().getInitialPrice())
					exception.add(ModelError.INVALID_BID_PRICE);
			}

			// If there is a problem, throw the exception to add a generic one.
			if (exception.hasError()) throw exception;
		}
		catch (ModelException me)
		{
			// If the generated exception is not the one of this method, merge them.
			if (me != exception)
				while (me.hasError()) exception.add(me.getLastError());

			// Generate exception.
			throw exception;
		}

		// Return the highest bid for that item for further operations.
		return maxBid;
	}
}
