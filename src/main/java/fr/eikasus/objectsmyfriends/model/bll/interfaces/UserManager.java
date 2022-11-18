package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Interface used for implementing user manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage users. Methods present in the interface implementation are allowed to
 * use as many as data access objects required to accomplish their goal. The
 * access of one of data access object method by the controllers is strictly
 * forbidden.
 *
 * @see #add(String, String, String, String, String, String, String, String,
 * String, int, boolean)
 * @see #find(String, String, String)
 * @see #update(User, HashMap)
 * @see #delete(User, boolean)
 */

public interface UserManager
{
	/**
	 * Set the manager factory.
	 * <p>
	 * This method retrieves the manager factory to allow the instance methods to
	 * access others managers.
	 *
	 * @param managerFactory Manager factory instance.
	 */

	void setManagerFactory(ManagerFactory managerFactory);

	/**
	 * Create a user.
	 * <p>
	 * Create a new user and add it into the persistence unit. All fields are
	 * required. The password supplied in plain text is also hashed and a salt is
	 * created for this user. The method can throw the ModelException exception
	 * which contains all errors encountered. Use method like {@code getLastError}
	 * or {@code getLastErrorMessage} from ModelException to know the problems.
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
	 * @see ModelException#getLastError()
	 * @see ModelException#getLastErrorMessage()
	 */

	User add(String username, String firstName, String lastName, String email, String phoneNumber, String street, String zipCode, String city, String plainPassword, int credit, boolean admin) throws ModelException;

	/**
	 * Try to find a user.
	 * <p>
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

	User find(String username, String email, String plainPassword) throws ModelException;

	/**
	 * Search a specific user.
	 * <p>
	 * Search a specific user in the persistence unit whose identifier is the one
	 * supplied in parameter. If this supplied parameter is null, then all users
	 * are returned by this method.
	 *
	 * @param identifier User identifier or null for all.
	 *
	 * @return List of users found.
	 *
	 * @throws ModelException In case of pb.
	 */

	List<User> find(Long identifier) throws ModelException;

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

	void update(User user, @NotNull HashMap<String, Object> properties) throws ModelException;

	/**
	 * Delete  user.
	 * <p>
	 * This method delete a user from the persistence unit. Whether it is really
	 * deleted depends on the archived parameter. If true, the user is only marked
	 * as archived, remaining in the persistence unit. If false, the user and all
	 * is related data are really deleted from the persistence unit. User items
	 * sold are not deleted but their link with the deleted user is broken.
	 *
	 * @param user     User to delete.
	 * @param archived If the User should be marked as archived.
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(User user, boolean archived) throws ModelException;
}
