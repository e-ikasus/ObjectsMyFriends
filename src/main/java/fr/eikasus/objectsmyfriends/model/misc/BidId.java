package fr.eikasus.objectsmyfriends.model.misc;

import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class used to define the Bid composite primary key.
 */

public class BidId implements Serializable
{
	/* ************* */
	/* Class members */
	/* ************* */

	private User user;

	private Item item;

	private Date date;

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty composite identifier.
	 */

	public BidId()
	{
	}

	/**
	 * Create a fully qualified composite identifier.
	 *
	 * @param user User the bid belongs to.
	 * @param item Item the bid belongs to.
	 * @param date Date of the bid.
	 */

	public BidId(User user, Item item, Date date)
	{
		this.user = user;
		this.item = item;
		this.date = date;
	}

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Item getItem()
	{
		return item;
	}

	public void setItem(Item item)
	{
		this.item = item;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BidId bidId = (BidId) o;
		return user.equals(bidId.user) && item.equals(bidId.item) && date.equals(bidId.date);
	}

	@Override public int hashCode()
	{
		return Objects.hash(user, item, date);
	}
}
