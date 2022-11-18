package fr.eikasus.objectsmyfriends.model.bo;

import fr.eikasus.objectsmyfriends.model.misc.ItemState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class representing an item being sold.
 */

@Entity(name="Item") @Table(name = "items")
public class Item implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH_DESCRIPTION = 512;
	public static final int MAX_LENGTH_NAME = 32;

	private static final String UNDEFINED_FIELD = "undefined";

	/* ************* */
	/* Class members */
	/* ************* */

	// Item identifier
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long identifier;

	// Item name
	@Column(nullable = false, length = MAX_LENGTH_NAME)
	private String name;

	// Item description
	@Column(nullable = false, length = MAX_LENGTH_DESCRIPTION)
	private String description;

	// Start of the bidding.
	@Column(name = "bidding_start", nullable = false) @Temporal(TemporalType.TIMESTAMP)
	private Date biddingStart;

	// End of the bidding.
	@Column(name = "bidding_end", nullable = false) @Temporal(TemporalType.TIMESTAMP)
	private Date biddingEnd;

	// Initial item price.
	@Column(name = "initial_price", nullable = false)
	private int initialPrice;

	// Price at which the item is sold.
	@Column(name = "final_price", nullable = false)
	private int finalPrice;

	// Current state of the item.
	@Column(nullable = false, length = 2) @Enumerated(EnumType.STRING)
	private ItemState state;

	// Seller of the item.
	@ManyToOne @JoinColumn(name = "seller")
	private User seller;

	// Buyer that won the item.
	@ManyToOne @JoinColumn(name = "buyer")
	private User buyer;

	// Item category.
	@ManyToOne @JoinColumn(name = "category", nullable = false)
	private Category category;

	// Images attached to the item.
	// Default => LAZY.
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.DETACH}, targetEntity = Image.class, mappedBy = "item", fetch = FetchType.LAZY)
	private List<Image> images;

	// Pickup place for that item.
	// OneToOne => EAGER.
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.DETACH}, targetEntity = PickupPlace.class, mappedBy = "item")
	private PickupPlace pickupPlace;

	// Bids attached to the item.
	// Default => LAZY.
	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.DETACH}, targetEntity = Bid.class, mappedBy = "item", fetch = FetchType.LAZY)
	private List<Bid> bids;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public long getIdentifier()
	{
		return identifier;
	}

	public Item setIdentifier(long identifier)
	{
		this.identifier = identifier;

		return this;
	}

	public String getName()
	{
		return name;
	}

	public Item setName(String name)
	{
		this.name = name;

		return this;
	}

	public String getDescription()
	{
		return description;
	}

	public Item setDescription(String description)
	{
		this.description = description;

		return this;
	}

	public Date getBiddingStart()
	{
		return biddingStart;
	}

	public Item setBiddingStart(Date biddingStart)
	{
		this.biddingStart = biddingStart;

		return this;
	}

	public Date getBiddingEnd()
	{
		return biddingEnd;
	}

	public Item setBiddingEnd(Date biddingEnd)
	{
		this.biddingEnd = biddingEnd;

		return this;
	}

	public int getInitialPrice()
	{
		return initialPrice;
	}

	public Item setInitialPrice(int initialPrice)
	{
		this.initialPrice = initialPrice;

		return this;
	}

	public int getFinalPrice()
	{
		return finalPrice;
	}

	public Item setFinalPrice(int finalPrice)
	{
		this.finalPrice = finalPrice;

		return this;
	}

	public ItemState getState()
	{
		return state;
	}

	public Item setState(ItemState state)
	{
		this.state = state;

		return this;
	}

	public User getSeller()
	{
		return seller;
	}

	public Item setSeller(User seller)
	{
		this.seller = seller;

		return this;
	}

	public User getBuyer()
	{
		return buyer;
	}

	public Item setBuyer(User buyer)
	{
		this.buyer = buyer;

		return this;
	}

	public Category getCategory()
	{
		return category;
	}

	public Item setCategory(Category category)
	{
		this.category = category;

		return this;
	}

	public List<Image> getImages()
	{
		// Return a defensive copy of the image list.
		return new ArrayList<>(images);
	}

	public Item setImages(List<Image> images)
	{
		this.images = images;

		return this;
	}

	public PickupPlace getPickupPlace()
	{
		return pickupPlace;
	}

	public Item setPickupPlace(PickupPlace pickupPlace)
	{
		this.pickupPlace = pickupPlace;

		return this;
	}

	public List<Bid> getBids()
	{
		// Return a defensive copy of the bid list.
		return new ArrayList<>(bids);
	}

	public Item setBids(List<Bid> bids)
	{
		this.bids = bids;

		return this;
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty item with default values.
	 */

	public Item()
	{
		this(UNDEFINED_FIELD, UNDEFINED_FIELD, null, null, 0, null, null, null, null);
	}

	/**
	 * Create a copied item.
	 * <p>
	 * Create a cloned item form the supplied one.
	 *
	 * @param model Item to copy properties from.
	 */

	public Item(@NotNull Item model)
	{
		this.identifier = model.getIdentifier();
		this.name = model.getName();
		this.description = model.getDescription();
		this.biddingStart = model.getBiddingStart();
		this.biddingEnd = model.getBiddingEnd();
		this.initialPrice = model.getInitialPrice();
		this.finalPrice = model.getFinalPrice();
		this.state = model.getState();
		this.seller = model.getSeller();
		this.buyer = model.getBuyer();
		this.category = model.getCategory();
		this.images = model.getImages();
		this.pickupPlace = model.getPickupPlace();
		this.bids =  model.getBids();
	}

	/**
	 * Create a full qualified item.
	 *
	 * @param name         Short name of the item (<= 32 cars).
	 * @param description  Description of the item (<= 512 cars).
	 * @param biddingStart Start of the bidding.
	 * @param biddingEnd   End of the bidding.
	 * @param initialPrice Initial price of the idem.
	 * @param state        Current state of the item.
	 * @param seller       Seller of the item.
	 * @param buyer        Buyer of the item.
	 * @param category     Category of the item.
	 */

	public Item(String name, String description, Date biddingStart, Date biddingEnd, int initialPrice, ItemState state, User seller, User buyer, Category category)
	{
		this.identifier = 0;
		this.name = name;
		this.description = description;
		this.biddingStart = biddingStart;
		this.biddingEnd = biddingEnd;
		this.initialPrice = initialPrice;
		this.finalPrice = 0;
		this.state = state;
		this.seller = seller;
		this.buyer = buyer;
		this.category = category;

		this.images = new ArrayList<>();
		this.pickupPlace = null;
		this.bids = new ArrayList<>();
	}

	/* ************************ */
	/* Help methods implemented */
	/* ************************ */

	public boolean isSold()
	{
		return (this.state == ItemState.SD);
	}

	public boolean isActive()
	{
		return (this.state == ItemState.AC);
	}

	public boolean isCanceled()
	{
		return (this.state == ItemState.CA);
	}

	public boolean isWaiting()
	{
		return (this.state == ItemState.WT);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Add an image to the item.
	 * <p>
	 * An image is added to the item and his corresponding property in the image
	 * is updated to guaranty the bidirectional relation. Note that this is the
	 * only way to add an image. Trying to add an image in the list returned by
	 * getImages() is not possible as this list is read only.
	 *
	 * @param image Image to add.
	 */

	public Item addImage(Image image)
	{
		// Add the image to the image list of the item.
		images.add(image);

		// Reference this item in the image.
		image.setItem(this);

		// Return this item for setter chaining.
		return this;
	}

	/**
	 * Remove an image from the item.
	 * <p>
	 * An image is removed from the item and his corresponding property in the
	 * image is updated to guaranty the bidirectional relation. Note that this is
	 * the only way to remove an image. Trying to remove an image in the list
	 * returned by getImages() is not possible as this list is read only.
	 *
	 * @param image Image to remove.
	 */

	public Item removeImage(Image image)
	{
		// Remove the bid to the bid list of the item.
		images.remove(image);

		// Remove reference to this item.
		image.setItem(null);

		// Return this item for setter chaining.
		return this;
	}

	/**
	 * Add a bid to the item.
	 * <p>
	 * A bid is added to the item and his corresponding property in the bid is
	 * updated to guaranty the bidirectional relation. Note that this is the only
	 * way to add a bid. Trying to add a bid in the list returned by getBids() is
	 * not possible as this list is read only.
	 *
	 * @param bid Bid to add.
	 */

	public Item addBid(Bid bid)
	{
		// Add the bid to the bid list of the item.
		bids.add(bid);

		// Reference this item in the bid.
		bid.setItem(this);

		// Return this item for setter chaining.
		return this;
	}

	/**
	 * Remove a bid from the item.
	 * <p>
	 * A bid is removed from the item and his corresponding property in the bid is
	 * updated to guaranty the bidirectional relation. Note that this is the only
	 * way to remove a bid. Trying to remove a bid in the list returned by
	 * getBids() is not possible as this list is read only.
	 *
	 * @param bid Bid to remove.
	 */

	public Item removeBid(Bid bid)
	{
		// Remove the bid to the bid list of the item.
		bids.remove(bid);

		// Remove reference to this item.
		bid.setItem(null);

		// Return this item for setter chaining.
		return this;
	}

	/**
	 * Create a string containing the instance description.
	 */

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append(" identifier = '").append(identifier).append("', ");
		sb.append(" name = '").append(name).append("', ");
		sb.append(" description = '").append(description).append("', ");
		sb.append(" biddingStart = '").append(biddingStart).append("', ");
		sb.append(" biddingEnd = '").append(biddingEnd).append("', ");
		sb.append(" initialPrice = '").append(initialPrice).append("', ");
		sb.append(" finalPrice = '").append(finalPrice).append("', ");
		sb.append(" state = '").append(state).append("', ");
		sb.append(" seller = ").append(seller).append(", ");
		sb.append(" buyer = ").append(buyer).append(", ");
		sb.append(" category = ").append(category).append(" ");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Method for comparing one item place to another.
	 *
	 * @param object Item to compare to.
	 *
	 * @return true if items are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		Item item = (Item) object;

		// The object is always equals with it.
		if (this == object) return true;

		// Object to compare to should be not nul and of same class.
		if (object == null || getClass() != object.getClass()) return false;

		// All fields need to be the same value for equality.
		return (identifier == item.getIdentifier())
						&& (initialPrice == item.getInitialPrice())
						&& (finalPrice == item.getFinalPrice())
						&& (name.equals(item.getName()))
						&& (description.equals(item.getDescription()))
						&& ((biddingStart == null) ? (0) : (biddingStart.getTime())) == ((item.getBiddingStart() == null) ? (0) : (item.getBiddingStart().getTime()))
						&& ((biddingEnd == null) ? (0) : (biddingEnd.getTime())) == ((item.getBiddingEnd() == null) ? (0) : (item.getBiddingEnd().getTime()))
						&& (state.equals(item.getState()))
						&& (seller.getIdentifier() == item.getSeller().getIdentifier())
						&& ((buyer != null) == (item.getBuyer() != null))
						&& ((buyer == null) || (buyer.getIdentifier() == item.getBuyer().getIdentifier()));
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		Long dateStart = ((biddingStart == null) ? (0) : (biddingStart.getTime()));
		Long dateEnd = ((biddingEnd == null) ? (0) : (biddingEnd.getTime()));

		if (buyer == null)
			return Objects.hash(identifier, name, description, dateStart, dateEnd, initialPrice, finalPrice, state, seller.getIdentifier(), category.getIdentifier());
		else
			return Objects.hash(identifier, name, description, dateStart, dateEnd, initialPrice, finalPrice, state, seller.getIdentifier(), buyer.getIdentifier(), category.getIdentifier());
	}
}
