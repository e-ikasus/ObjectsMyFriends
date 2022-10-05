package fr.eikasus.objectsmyfriends.model.bo;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing an item category.
 */

@Entity @Table(name = "categories")
public class Category implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH_LABEL = 32;

	private static final String UNDEFINED_CATEGORY = "undefined";

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long identifier;

	@Column(name = "label", nullable = false, length = MAX_LENGTH_LABEL, unique = true)
	private String label;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public long getIdentifier()
	{
		return identifier;
	}

	public Category setIdentifier(long identifier)
	{
		this.identifier = identifier;

		return this;
	}

	public String getLabel()
	{
		return label;
	}

	public Category setLabel(String label)
	{
		this.label = label;

		return this;
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create a category with default values.
	 */

	public Category()
	{
		// Fill class with default values.
		this(UNDEFINED_CATEGORY);
	}

	/**
	 * Create a copied category.
	 * <p>
	 * Create a cloned category form the supplied one.
	 *
	 * @param model Category to copy properties from.
	 */

	public Category(@NotNull Category model)
	{
		this.identifier = model.getIdentifier();
		this.label = model.getLabel();
	}

	/**
	 * Full class constructor..
	 *
	 * @param label Category name (<= 32 cars).
	 */

	public Category(String label)
	{
		this.identifier = 0;
		this.label = label.trim();
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a string containing the class description.
	 *
	 * @return String containing the full description of the class.
	 */

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append(" identifier = '").append(identifier).append("', ");
		sb.append(" label = '").append(label).append("' ");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Method for comparing one category to another.
	 *
	 * @param object Category to compare to.
	 *
	 * @return true if categories are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		Category category = (Category) object;

		// The object is always equals with it.
		if (this == category) return true;

		// Object to compare to should be not nul and of same class.
		if (object == null || getClass() != object.getClass()) return false;

		// All fields need to be the same value for equality.
		return (identifier == category.getIdentifier())
						&& (label.equals(category.getLabel()));
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		return Objects.hash(identifier, label);
	}
}
