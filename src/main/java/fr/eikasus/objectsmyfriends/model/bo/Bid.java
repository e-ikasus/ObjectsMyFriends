package fr.eikasus.objectsmyfriends.model.bo;

import fr.eikasus.objectsmyfriends.model.misc.BidId;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class representing a bid on an item.
 */

@Entity(name="Bid") @Table(name = "bids") @IdClass(BidId.class)
public class Bid implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	/* ************* */
	/* Class members */
	/* ************* */

	// User that made the bid.
	@Id @ManyToOne @JoinColumn(name = "user") @OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	// Item that correspond to this bid.
	@Id @ManyToOne @JoinColumn(name = "item") @OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	// Date of the bid.
	@Id @Temporal(TemporalType.TIMESTAMP)
	private Date date;

	// Price of the bid.
	@Column(nullable = false)
	private int price;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public User getUser()
	{
		return user;
	}

	public Bid setUser(User user)
	{
		this.user = user;

		return this;
	}

	public Item getItem()
	{
		return item;
	}

	public Bid setItem(Item item)
	{
		this.item = item;

		return this;
	}

	public Date getDate()
	{
		return date;
	}

	public Bid setDate(Date date)
	{
		this.date = date;

		return this;
	}

	public int getPrice()
	{
		return price;
	}

	public Bid setPrice(int price)
	{
		this.price = price;

		return this;
	}

	public Bid setIdentifier(BidId identifier)
	{
		this.item = identifier.getItem();
		this.user = identifier.getUser();
		this.date = identifier.getDate();

		return this;
	}

	public BidId getIdentifier()
	{
		return new BidId(user, item, date);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty bid with default values.
	 */

	public Bid()
	{
		this(null, null, null, 0);
	}

	/**
	 * Create a bid on an item.
	 *<p>
	 * Create a bid for an item. If an item is supplied in parameter, then this
	 * newly created bid is attached to it.
	 *
	 * @param user  User that made the bid.
	 * @param item  Item on which the bid is related to.
	 * @param date  Date of the bid.
	 * @param price Price of the user bid.
	 */

	public Bid(User user, Item item, Date date, int price)
	{
		this.user = user;
		this.item = item;
		this.date = date;
		this.price = price;

		if (item != null) item.addBid(this);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a string containing the instance description.
	 */

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append(" user = ").append(user).append(", ");
		sb.append(" item = ").append(item).append(", ");
		sb.append(" date = '").append(date).append("', ");
		sb.append(" price = '").append(price).append("' ");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Method for comparing one bid to another.
	 *
	 * @param object Bid to compare to.
	 *
	 * @return true if pickup places are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		Bid bid = (Bid) object;

		// The object is always equals with it.
		if (this == bid) return true;

		// Object to compare to should be not null and of same class.
		if (object == null || getClass() != object.getClass()) return false;

		// All fields need to be the same value for equality.
		return (price == bid.getPrice())
						&& ((user == null) ? (0) : (user.getIdentifier())) == ((bid.getUser() == null) ? (0) : (bid.getUser().getIdentifier()))
						&& ((item == null) ? (0) : (item.getIdentifier())) == ((bid.getItem() == null) ? (0) : (bid.getItem().getIdentifier()))
						&& ((date == null) ? (0) : (date.getTime())) == ((bid.getDate() == null) ? (0) : (bid.getDate().getTime()));
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		long userId = (user == null) ? (0) : (user.getIdentifier());
		long itemId = (item == null) ? (0) : (item.getIdentifier());
		Long bidDate = (date == null) ? (0) : (date.getTime());

		return Objects.hash(userId, itemId, bidDate, price);
	}
}
