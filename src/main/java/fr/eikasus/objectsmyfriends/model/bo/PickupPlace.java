package fr.eikasus.objectsmyfriends.model.bo;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing a pickup place for a sold item.
 */

@Entity @Table(name = "pickup_places")
public class PickupPlace implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH_STREET = 32;
	public static final int MAX_LENGTH_ZIPCODE = 16;
	public static final int MAX_LENGTH_CITY = 32;

	private static final String UNDEFINED_FIELD = "undefined";

	/* ************* */
	/* Class members */
	/* ************* */

	// Item related to this place.
	@Id @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item") @OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	// Street where to pick up the item.
	@Column(nullable = false, length = MAX_LENGTH_STREET)
	private String street;

	// Zipcode where to pick up the item.
	@Column(nullable = false, length = MAX_LENGTH_ZIPCODE)
	private String zipCode;

	// City where to pick up the item.
	@Column(nullable = false, length = MAX_LENGTH_CITY)
	private String city;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public Item getItem()
	{
		return item;
	}

	public PickupPlace setItem(Item item)
	{
		this.item = item;

		return this;
	}

	public String getStreet()
	{
		return street;
	}

	public PickupPlace setStreet(String street)
	{
		this.street = street;

		return this;
	}

	public String getZipCode()
	{
		return zipCode;
	}

	public PickupPlace setZipCode(String zipCode)
	{
		this.zipCode = zipCode;

		return this;
	}

	public String getCity()
	{
		return city;
	}

	public PickupPlace setCity(String city)
	{
		this.city = city;

		return this;
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty pickup place with default values.
	 */

	public PickupPlace()
	{
		this(null, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD);
	}

	/**
	 * Create a copied pickup place.
	 * <p>
	 * Create a cloned pickup place form the supplied one.
	 *
	 * @param model Pickup place to copy properties from.
	 */

	public PickupPlace(@NotNull PickupPlace model)
	{
		this.item = model.getItem();
		this.street = model.getStreet();
		this.zipCode = model.getZipCode();
		this.city = model.getCity();
	}

	/**
	 * Create a pickup place for an item.
	 * <p></p>
	 *
	 * Create a pickup place for an item. If an item is supplied in parameter,
	 * then this newly created pickup place is attached to it.
	 *
	 * @param item    Item that this place belongs to.
	 * @param street  Street where to pick up the item (<=32 cars).
	 * @param zipCode Zip code where to pick up the item (<= 16 cars).
	 * @param city    City where to pick up the item (<= 32 cars).
	 */

	public PickupPlace(Item item, String street, String zipCode, String city)
	{
		this.item = item;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;

		if (item != null) item.setPickupPlace(this);
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

		sb.append("PickupPlace={");
		sb.append(" item = '").append(item).append("', ");
		sb.append(" street = '").append(street).append("', ");
		sb.append(" zipCode = '").append(zipCode).append("', ");
		sb.append(" city = '").append(city).append("'}");

		return sb.toString();
	}

	/**
	 * Method for comparing one pickup place to another.
	 *
	 * @param object Pickup place to compare to.
	 *
	 * @return true if pickup places are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		PickupPlace pickupPlace = (PickupPlace) object;

		// The object is always equals with it.
		if (this == pickupPlace) return true;

		// Object to compare to should be not nul and of same class.
		if (object == null || getClass() != object.getClass()) return false;

		// All fields need to be the same value for equality.
		return (getItem().getIdentifier() == pickupPlace.getItem().getIdentifier())
						&& (street.equals(pickupPlace.getStreet()))
						&& (zipCode.equals(pickupPlace.getZipCode()))
						&& (city.equals(pickupPlace.getCity()));
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		return Objects.hash(item.getIdentifier(), street, zipCode, city);
	}
}
