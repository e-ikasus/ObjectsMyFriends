package fr.eikasus.objectsmyfriends.model.bo;

import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class representing a user.
 * <p>
 * This class define a user. Among standard methods like getters and setters or
 * constructors, the class define a useful method to hash the plain password.
 * This method is part of the User class instead of Business Logic Layer because
 * the hashing process is more specific to the class implementation. See
 * {@code hashPassword()} for more details.
 *
 * @see #hashPassword()
 */

@Entity @Table(name = "users")
public class User implements Serializable
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final long serialVersionUID = 1L;

	public static final int MAX_LENGTH_USERNAME = 32;
	public static final int MAX_LENGTH_FIRSTNAME = 32;
	public static final int MAX_LENGTH_LASTNAME = 32;
	public static final int MAX_LENGTH_EMAIL = 64;
	public static final int MAX_LENGTH_PHONE_NUMBER = 16;
	public static final int MAX_LENGTH_STREET = 32;
	public static final int MAX_LENGTH_ZIPCODE = 16;
	public static final int MAX_LENGTH_CITY = 32;
	public static final int MAX_LENGTH_PASSWORD = 32;
	public static final int MAX_LENGTH_SALT = 32;
	public static final int MAX_LENGTH_PASSWORD_DB = 1024;

	private static final String UNDEFINED_FIELD = "undefined";

	/* ************* */
	/* Class members */
	/* ************* */

	// Identifier of the user.
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long identifier;

	// Name used for login.
	@Column(nullable = false, length = MAX_LENGTH_USERNAME, unique = true)
	private String username;

	// User firstname.
	@Column(name = "first_name", nullable = false, length = MAX_LENGTH_FIRSTNAME)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = MAX_LENGTH_LASTNAME)
	private String lastName;

	@Column(nullable = false, length = MAX_LENGTH_EMAIL, unique = true)
	private String email;

	@Column(name = "phone_number", nullable = false, length = MAX_LENGTH_PHONE_NUMBER)
	private String phoneNumber;

	@Column(nullable = false, length = MAX_LENGTH_STREET)
	private String street;

	@Column(name = "zip_code", nullable = false, length = MAX_LENGTH_ZIPCODE)
	private String zipCode;

	@Column(nullable = false, length = MAX_LENGTH_CITY)
	private String city;

	@Column(nullable = false, length = MAX_LENGTH_PASSWORD_DB)
	private byte[] password;

	// Salt used to hash password in database.
	@Column(nullable = false, length = MAX_LENGTH_SALT)
	private byte[] salt;

	// Number of token the user has to by items.
	@Column(nullable = false)
	private int credit;

	// Determine whether the user is an administrator or not.
	@Column(name = "is_administrator", nullable = false)
	private boolean isAdmin;

	// Determine whether the user is archived or not.
	@Column(name = "is_archived", nullable = false)
	private boolean isArchived;

	// Password not hashed.
	@Transient
	private String plainPassword;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public long getIdentifier()
	{
		return identifier;
	}

	public User setIdentifier(long identifier)
	{
		this.identifier = identifier;

		return this;
	}

	public String getUsername()
	{
		return username;
	}

	public User setUsername(String username)
	{
		this.username = username;

		return this;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public User setFirstName(String firstName)
	{
		this.firstName = firstName;

		return this;
	}

	public String getLastName()
	{
		return lastName;
	}

	public User setLastName(String lastName)
	{
		this.lastName = lastName;

		return this;
	}

	public String getEmail()
	{
		return email;
	}

	public User setEmail(String email)
	{
		this.email = email;

		return this;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public User setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;

		return this;
	}

	public String getStreet()
	{
		return street;
	}

	public User setStreet(String street)
	{
		this.street = street;

		return this;
	}

	public String getZipCode()
	{
		return zipCode;
	}

	public User setZipCode(String zipCode)
	{
		this.zipCode = zipCode;

		return this;
	}

	public String getCity()
	{
		return city;
	}

	public User setCity(String city)
	{
		this.city = city;

		return this;
	}

	public byte[] getPassword()
	{
		return password;
	}

	public User setPassword(byte[] password)
	{
		this.password = password;

		return this;
	}

	public byte[] getSalt()
	{
		return salt;
	}

	public User setSalt(byte[] salt)
	{
		this.salt = salt;

		return this;
	}

	public int getCredit()
	{
		return credit;
	}

	public User setCredit(int credit)
	{
		this.credit = credit;

		return this;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public User setAdmin(boolean admin)
	{
		isAdmin = admin;

		return this;
	}

	public boolean isArchived()
	{
		return isArchived;
	}

	public User setArchived(boolean archived)
	{
		isArchived = archived;

		return this;
	}

	public String getPlainPassword()
	{
		return plainPassword;
	}

	public User setPlainPassword(String plainPassword)
	{
		this.plainPassword = plainPassword;

		return this;
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Create an empty user with default values.
	 */

	public User()
	{
		this(UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, UNDEFINED_FIELD, null, 0, false);
	}

	/**
	 * Create a copied user.
	 * <p>
	 * Create a cloned user form the supplied one.
	 *
	 * @param model User to copy properties from.
	 */

	public User(@NotNull User model)
	{
		this.identifier = model.getIdentifier();
		this.username = model.getUsername();
		this.firstName = model.getFirstName();
		this.lastName = model.getLastName();
		this.email = model.getEmail();
		this.phoneNumber = model.getPhoneNumber();
		this.street = model.getStreet();
		this.zipCode = model.getZipCode();
		this.city = model.getCity();
		this.password = model.getPassword();
		this.salt = model.getSalt();
		this.credit = model.getCredit();
		this.isAdmin = model.isAdmin();
		this.isArchived = model.isArchived();
		this.plainPassword = model.getPlainPassword();
	}

	/**
	 * Create a new user.
	 * <p>
	 * This method create a new user with all properties set but isArchived,
	 * password and salt.
	 *
	 * @param username      Name of the user used to log in (<= 32 cars).
	 * @param firstName     User first name (<= 32 cars).
	 * @param lastName      User first name (<= 32 cars).
	 * @param email         User email (<= 64 cars).
	 * @param phoneNumber   User phone number (<= 16 cars).
	 * @param street        User street address (<= 32 cars).
	 * @param zipCode       User zipcode address (<= 16 cars).
	 * @param city          User city address (<= 32 cars).
	 * @param plainPassword User password (<= 32 cars, but more in the database).
	 * @param credit        Number of coins to buy items.
	 * @param isAdmin       Is this user an administrateur.
	 */

	public User(String username, String firstName, String lastName, String email, String phoneNumber, String street, String zipCode, String city, String plainPassword, int credit, boolean isAdmin)
	{
		this.identifier = 0;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.password = null;
		this.salt = null;
		this.credit = credit;
		this.isAdmin = isAdmin;
		this.isArchived = false;
		this.plainPassword = plainPassword;
	}

	/**
	 * Create a new user.
	 * <p>
	 * Create a user with all properties set but password and salt.
	 *
	 * @param username      Name of the user used to login (<= 32 cars).
	 * @param firstName     User first name (<= 32 cars).
	 * @param lastName      User first name (<= 32 cars).
	 * @param email         User email (<= 64 cars).
	 * @param phoneNumber   User phone number (<= 16 cars).
	 * @param street        User street address (<= 32 cars).
	 * @param zipCode       User zipcode address (<= 16 cars).
	 * @param city          User city address (<= 32 cars).
	 * @param plainPassword User password (<= 32 cars, but more in the database).
	 * @param credit        Number of coins to buy items.
	 * @param isAdmin       Is this user an administrateur.
	 * @param isArchived    Is this user archived, before further deletion.
	 */

	public User(String username, String firstName, String lastName, String email, String phoneNumber, String street, String zipCode, String city, String plainPassword, int credit, boolean isAdmin, boolean isArchived)
	{
		this.identifier = 0;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.zipCode = zipCode;
		this.city = city;
		this.password = null;
		this.salt = null;
		this.credit = credit;
		this.isAdmin = isAdmin;
		this.isArchived = isArchived;
		this.plainPassword = plainPassword;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Hash the plain password.
	 * <p>
	 * Hash the password for the user. The password need to be present and valide
	 * in the object. After the operation, the password is hashed and a salt
	 * specific to this user is created. Be aware that after the method succeed,
	 * the plain password property of the user is emptied. The user can have an
	 * already defined salt. In that case, it is used instead of created again.
	 *
	 * @throws ModelException In case of error
	 */

	public void hashPassword() throws ModelException
	{
		// If no password is defined to be hashed.
		if (plainPassword == null) return;

		KeySpec spec;
		SecretKeyFactory factory;
		SecureRandom rnd = new SecureRandom();

		// Create the salt if it doesn't already exist.
		if (salt == null) rnd.nextBytes((salt = new byte[User.MAX_LENGTH_SALT]));

		try
		{
			spec = new PBEKeySpec(plainPassword.toCharArray(), salt, 65536, 128);

			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

			password = factory.generateSecret(spec).getEncoded();
		}
		catch (Exception e)
		{
			throw new ModelException(e, ModelError.UNABLE_TO_HASH_USER_PASSWORD);
		}

		// If the resulting hash is too long to be saved in the database.
		if (password.length > User.MAX_LENGTH_PASSWORD_DB)
			throw new ModelException(null, ModelError.UNABLE_TO_HASH_USER_PASSWORD);

		// Because the password is hashed, clear his plain form.
		plainPassword = null;
	}

	/**
	 * Create a string containing the instance description.
	 */

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append(" identifier = '").append(identifier).append("', ");
		sb.append(" username = '").append(username).append("', ");
		sb.append(" firstName = '").append(firstName).append("', ");
		sb.append(" lastName = '").append(lastName).append("', ");
		sb.append(" email = '").append(email).append("', ");
		sb.append(" phoneNumber = '").append(phoneNumber).append("', ");
		sb.append(" street = '").append(street).append("', ");
		sb.append(" zipCode = '").append(zipCode).append("', ");
		sb.append(" city = '").append(city).append("', ");
		sb.append(" plainPassword = '").append(plainPassword).append("', ");
		sb.append(" credit = '").append(credit).append("', ");
		sb.append(" isAdmin = '").append(isAdmin).append("', ");
		sb.append(" isArchived = '").append(isArchived).append("' ");
		sb.append("}");

		return sb.toString();
	}

	/**
	 * Method for comparing one user to another.
	 *
	 * @param object User to compare to.
	 *
	 * @return true if users are equal, false otherwise.
	 */

	@Override public boolean equals(Object object)
	{
		User user = (User) object;

		// The object is always equals with it.
		if (this == user) return true;

		// Object to compare to should be not nul and of same class.
		if (user == null || getClass() != object.getClass()) return false;

		// All fields need to be the same value for equality.
		return (identifier == user.getIdentifier())
			&& (credit == user.getCredit())
			&& (isAdmin == user.isAdmin())
			&& (isArchived == user.isArchived())
			&& (username.equals(user.getUsername()))
			&& (firstName.equals(user.firstName))
			&& (lastName.equals(user.lastName))
			&& (email.equals(user.email))
			&& (phoneNumber.equals(user.getPhoneNumber()))
			&& (street.equals(user.getStreet()))
			&& (zipCode.equals(user.getZipCode()))
			&& (city.equals(user.getCity()))
			&& (Arrays.equals(password, user.getPassword()))
			&& (Arrays.equals(salt, user.getSalt()));
	}

	/**
	 * Compute the hash code of the class, representing his fingerprint.
	 *
	 * @return Hash code.
	 */

	@Override public int hashCode()
	{
		return Objects.hash(identifier, username, firstName, lastName, email, phoneNumber, street, zipCode, city, Arrays.hashCode(password), Arrays.hashCode(salt), credit, isAdmin, isArchived);
	}
}
