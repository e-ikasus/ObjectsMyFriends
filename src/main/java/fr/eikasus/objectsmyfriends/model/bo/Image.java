package fr.eikasus.objectsmyfriends.model.bo;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity @Table(name = "images")
public class Image implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH_PATH = 512;

	/* ************* */
	/* Class members */
	/* ************* */

	// Identifier of the image.
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column (name = "identifier")
	private long identifier;

	// Item that this image belongs to.
	@ManyToOne @JoinColumn(name = "item", nullable = false) @OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	// Path to this image.
	@Column(nullable = false, length = MAX_LENGTH_PATH)
	private String path;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public long getIdentifier()
	{
		return identifier;
	}

	public Image setIdentifier(long identifier)
	{
		this.identifier = identifier;

		return this;
	}

	public Item getItem()
	{
		return item;
	}

	public Image setItem(Item item)
	{
		this.item = item;

		return this;
	}

	public String getPath()
	{
		return path;
	}

	public Image setPath(String path)
	{
		this.path = path;

		return this;
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty image with default values.
	 */

	public Image()
	{
		this(null, null);
	}

	/**
	 * Create a fully qualified image for an item.
	 * <p>
	 * Create an image for an item. If an item is supplied in parameter, then the
	 * image is attached to it.
	 *
	 * @param item Item that this image belongs to.
	 * @param path Path to the file containing the image (<= 512 cars).
	 */

	public Image(Item item, String path)
	{
		this.identifier = 0;
		this.item = item;
		this.path = path;

		if (item != null) item.addImage(this);
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
		sb.append(" identifier = '").append(identifier).append("', ");
		sb.append(" item = '").append(item).append("', ");
		sb.append(" path = '").append(path).append("' ");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Method for comparing one image to another.
	 *
	 * @param object Image to compare to.
	 *
	 * @return true if users are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		Image image = (Image) object;

		// The object is always equals with it.
		if (this == object) return true;

		// Object to compare to should be not nul and of same class.
		if (object == null || getClass() != object.getClass()) return false;

		return (identifier == image.identifier)
						&& (Objects.equals(item, image.item))
						&& Objects.equals(path, image.path);
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		return Objects.hash(identifier, item, path);
	}
}
