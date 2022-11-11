package fr.eikasus.objectsmyfriends.model.bll.implementations;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bll.implementations.GenericManagerImpl;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.ItemManager;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.UserManager;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.UserDAO;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class used to handle users.
 * <p></p>
 * This class is used to manage users according to the business logic. It should
 * be used by the controllers to handle users like adding, deleting and so. The
 * access of one of data access object method by the controllers is strictly
 * forbidden.
 *
 * @see #add(String, String, String, String, String, String, String, String,
 * String, int, boolean) add()
 * @see #find(String, String, String) find()
 * @see #update(User, HashMap) update()
 * @see #delete(User, boolean) delete()
 */

public class UserManagerImpl extends GenericManagerImpl implements UserManager
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String VALIDATE_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private static final String VALIDATE_PSEUDO = "[A-Za-z0-9]+";
	private static final String VALIDATE_PHONE_NUMBER = "^0[1-9][0-9]{8}$";
	private static final String VALIDATE_PASSWORD = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[-+!*$@%_])([-+!*$@%_\\w]{8,30})$";
	private static final String VALIDATE_NAME = "^[A-Za-zéèëêùàÉÈËÊÙÀ' -]+$";
	private static final String VALIDATE_STREET = "^[0-9A-Za-zéèëêùàÉÈËÊÙÀ' -]+$";
	private static final String VALIDATE_ZIPCODE = "^[0-9A-Za-z]+$";

	/* ************* */
	/* Class members */
	/* ************* */

	// Data access object instance.
	private final UserDAO dao;

	private final Pattern emailCheck;
	private final Pattern pseudoCheck;
	private final Pattern phoneCheck;
	private final Pattern passwordCheck;
	private final Pattern nameCheck;
	private final Pattern streetCheck;
	private final Pattern zipcodeCheck;
	private final Pattern cityCheck;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Constructor of the class.
	 */

	public UserManagerImpl(ManagerFactory managerFactory)
	{
		super(managerFactory);

		// Data access object for user entity operations.
		dao = managerFactory.getDaoFactory().getUserDAO();

		// Property validators.
		emailCheck = Pattern.compile(VALIDATE_EMAIL);
		pseudoCheck = Pattern.compile(VALIDATE_PSEUDO);
		phoneCheck = Pattern.compile(VALIDATE_PHONE_NUMBER);
		passwordCheck = Pattern.compile(VALIDATE_PASSWORD);
		nameCheck = Pattern.compile(VALIDATE_NAME);
		streetCheck = Pattern.compile(VALIDATE_STREET);
		zipcodeCheck = Pattern.compile(VALIDATE_ZIPCODE);
		cityCheck = Pattern.compile(VALIDATE_NAME);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a user.
	 * <p>
	 * Create a new user and add it into the database. All fields are required.
	 * The password supplied in plain text is also hashed and a salt is created
	 * for this user. The method can throw the ModelException exception which
	 * contains all errors encountered. Use methods like {@code getLastError} or
	 * {@code getLastErrorMessage} from ModelException to know the problems.
	 *
	 * @param username      User pseudo.
	 * @param firstName     User firstname.
	 * @param lastName      User lastname.
	 * @param email         User mail.
	 * @param phoneNumber   User phone number.
	 * @param street        User street address.
	 * @param zipCode       User zip code address.
	 * @param city          User city address.
	 * @param plainPassword User plain password.
	 * @param credit        User credit number.
	 * @param admin         Is the user an admin ?
	 *
	 * @return Newly created user.
	 *
	 * @throws ModelException In case of wrong information supplied.
	 * @see ModelException#getLastError() getLastError
	 * @see ModelException#getLastErrorMessage() getLastErrorMessage
	 */

	public User add(String username, String firstName, String lastName, String email, String phoneNumber, String street, String zipCode, String city, String plainPassword, int credit, boolean admin) throws ModelException
	{
		// Create a new user with supplied values.
		User user = new User(username, firstName, lastName, email, phoneNumber, street, zipCode, city, plainPassword, credit, admin);

		try
		{
			// Check the user validity.
			validate(user);

			// Hash password and create salt.
			user.hashPassword();

			// Save the user into the database.
			dao.save(user);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_CREATE_USER);
		}

		// Return the newly created user.
		return user;
	}

	/**
	 * Try to find a user.
	 * <p></p>
	 * This method try to find a user either by its username or email. If the
	 * username parameter is not null, then it will be used to find the user by is
	 * username. If the email parameter is not null, it will find the user by its
	 * email. If both parameters are null, an exception is thrown. Once a user is
	 * found, its password is checked. If it doesn't match, an exception is thrown
	 * too.
	 *
	 * @param username      Name of the user to search.
	 * @param email         Email of the user to search.
	 * @param plainPassword User password in plain text.
	 *
	 * @return User found related to criteria.
	 *
	 * @throws ModelException In case of problem.
	 */

	public User find(String username, String email, String plainPassword) throws ModelException
	{
		User user, userForPassword;

		try
		{
			// Search the user by name or email by ignoring password.
			if (username != null) user = dao.findByUsername(username);
			else if (email != null) user = dao.findByEmail(email);
			else throw new ModelException();

			// Create a temporary user object to hash password supplied.
			userForPassword = new User();
			// The salt should be the same as the supplied user.
			userForPassword.setPlainPassword(plainPassword).setSalt(user.getSalt());
			// Compute the hashed password.
			userForPassword.hashPassword();

			// Is the supplied password correct ?
			if (!Arrays.equals(user.getPassword(), userForPassword.getPassword()))
				throw new ModelException();
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.USER_NOT_FOUND);
		}

		// return tue found user.
		return user;
	}

	/**
	 * Search a specific user.
	 * <p></p>
	 * Search a specific user in the database whose identifier is the one supplied
	 * in parameter. If this supplied parameter is null, then all users are
	 * returned by this method.
	 *
	 * @param identifier User identifier or null for all.
	 *
	 * @return List of users found.
	 *
	 * @throws ModelException In case of pb.
	 */

	public List<User> find(Long identifier) throws ModelException
	{
		List<User> users = new ArrayList<>();

		try
		{
			// Find a specific user or all.
			if (identifier == null) users = dao.find();
			else users.add(dao.find(identifier));
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.USER_NOT_FOUND);
		}

		// Return the result
		return users;
	}

	/**
	 * Update user properties.
	 * <p>
	 * This method update the properties of the supplied user. Each property new
	 * value should be supplied as a pair of key/value, a key representing the
	 * exact name or the user property, including case. If a key as an undefined
	 * property name, an exception is generated. Note that the username can't be
	 * modified.
	 *
	 * @param user       User to be updated.
	 * @param properties User properties to update.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void update(User user, @NotNull HashMap<String, Object> properties) throws ModelException
	{
		User updatedUser = new User(user);
		String string;
		Integer integer;

		if ((string = (String) properties.get("firstName")) != null)
		{
			properties.remove("firstName");
			updatedUser.setFirstName(string);
		}

		if ((string = (String) properties.get("lastName")) != null)
		{
			properties.remove("lastName");
			updatedUser.setLastName(string);
		}

		if ((string = (String) properties.get("email")) != null)
		{
			properties.remove("email");
			updatedUser.setEmail(string);
		}

		if ((string = (String) properties.get("phoneNumber")) != null)
		{
			properties.remove("phoneNumber");
			updatedUser.setPhoneNumber(string);
		}

		if ((string = (String) properties.get("street")) != null)
		{
			properties.remove("street");
			updatedUser.setStreet(string);
		}

		if ((string = (String) properties.get("zipCode")) != null)
		{
			properties.remove("zipCode");
			updatedUser.setZipCode(string);
		}

		if ((string = (String) properties.get("city")) != null)
		{
			properties.remove("city");
			updatedUser.setCity(string);
		}

		if ((string = (String) properties.get("plainPassword")) != null)
		{
			properties.remove("plainPassword");
			updatedUser.setPlainPassword(string);
		}

		if ((integer = (Integer) properties.get("credit")) != null)
		{
			properties.remove("credit");
			updatedUser.setCredit(integer);
		}

		// If properties always exist, it means they are unknown.
		if (!properties.isEmpty())
			throw new ModelException(null, ModelError.UNABLE_TO_UPDATE_USER);

		try
		{
			// Check the user validity.
			validate(updatedUser);

			// Hash password and create salt.
			updatedUser.hashPassword();

			// Update the user into the database.
			dao.update(updatedUser);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_UPDATE_USER);
		}
	}

	/**
	 * Delete  user.
	 * <p></p>
	 * This method delete a user from the database. Whether it is really deleted
	 * depends on the archived parameter. If true, the user is only marked as
	 * archived, remaining in the database. If false, the user and all is related
	 * data are really deleted from the database. User items sold are not deleted
	 * but their link with the deleted user is broken.
	 *
	 * @param user     User to delete.
	 * @param archived If the User should be marked as archived.
	 *
	 * @throws ModelException IN case of proglem.
	 */

	public void delete(User user, boolean archived) throws ModelException
	{
		List<Item> items;
		ItemManager itemManager;
		Search criteria;

		try
		{
			// Different action depending on the archived parameter.
			if (archived)
			{
				// Mark the user as archived.
				user.setArchived(true);
				// Update it in the database.
				dao.update(user);
			}
			else
			{
				// Instance of the item manager to delete them.
				itemManager = managerFactory.getItemManager();

				// Search for items related to the user.
				criteria = new Search();
				criteria.setMyCurrentSales().setMyWaitingSales().setMyCanceledSales();

				// Search items that are not sold.
				items = itemManager.findByCriteria(user, UserRole.SELLER, criteria, null, null);

				// Delete the items. They are owned by the user on which he couldn't make
				// bid, so no bid are pointing to those items that are deleted.
				itemManager.delete(items);

				// Delete bids that the user did.
				managerFactory.getBidManager().delete(user);

				// Delete the user from the database.
				dao.delete(user);
			}
		}
		catch (ModelException me)
		{
			throw me.add(ModelError.UNABLE_TO_DELETE_USER);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify a user.
	 * <p>
	 * This method verify the validity of the user supplied in parameter. Whether
	 * the password is checked depends on the {@code plainPassword} property of
	 * this supplied user: If this property is null, then no password is checked,
	 * and it is assumed that the password and salt properties of that user are
	 * already populated. If one or more properties are wrong, an exception is
	 * generated containing an error code for each invalid data. One this method
	 * succeed, it is necessary to call the {@code hashPassword} method to compute
	 * the hashed password if this is a new user, or a user who want to change his
	 * password.
	 *
	 * @param user User to verify.
	 *
	 * @throws ModelException In case of wrong information.
	 * @see User#hashPassword()
	 */

	private void validate(@NotNull User user) throws ModelException
	{
		ModelException exception = new ModelException();
		String string;

		// Check the pseudo validity.
		if (((string = user.getUsername()) == null) || (!pseudoCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_USERNAME))
			exception.add(ModelError.INVALID_USER_PSEUDO);

		// Check the last name validity.
		if (((string = user.getLastName()) == null) || (!nameCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_LASTNAME))
			exception.add(ModelError.INVALID_USER_LASTNAME);

		// Check the first name validity.
		if (((string = user.getFirstName()) == null) || (!nameCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_FIRSTNAME))
			exception.add(ModelError.INVALID_USER_FIRSTNAME);

		// Check the email validity.
		if (((string = user.getEmail()) == null) || (!emailCheck.matcher(string).matches()) || (string.trim().length() > User.MAX_LENGTH_EMAIL))
			exception.add(ModelError.INVALID_USER_EMAIL);

		// Check the phone number validity.
		if (((string = user.getPhoneNumber()) == null) || (!phoneCheck.matcher(string).matches()) || (string.trim().length() > User.MAX_LENGTH_PHONE_NUMBER))
			exception.add(ModelError.INVALID_USER_PHONE_NUMBER);

		// Check the street name validity.
		if (((string = user.getStreet()) == null) || (!streetCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_STREET))
			exception.add(ModelError.INVALID_USER_STREET);

		// Check the zipcode validity.
		if (((string = user.getZipCode()) == null) || (!zipcodeCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_ZIPCODE))
			exception.add(ModelError.INVALID_USER_ZIPCODE);

		// Check the city name validity.
		if (((string = user.getCity()) == null) || (!cityCheck.matcher(string).matches()) || (string.length() > User.MAX_LENGTH_CITY))
			exception.add(ModelError.INVALID_USER_CITY);

		// Check the password validity if needed.
		if ((string = user.getPlainPassword()) != null)
		{
			if ((string.length() == 0) || (string.length() > User.MAX_LENGTH_PASSWORD))
				exception.add(ModelError.INVALID_USER_PASSWORD);
			else if (!passwordCheck.matcher(string).matches())
				exception.add(ModelError.INVALID_USER_PASSWORD_TO_LIGHT);
		}
		else
		{
			// If no plain password is supplied, a hashed password should be defined.
			if (user.getPassword() == null)
				exception.add(ModelError.INVALID_USER_PASSWORD);
		}

		// Check the credit validity.
		if (user.getCredit() < 0) exception.add(ModelError.INVALID_USER_CREDITS);

		// Generate an exception in case of error.
		if (exception.hasError()) throw exception;
	}
}
