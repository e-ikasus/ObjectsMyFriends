package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

/**
 * Interface used for implementing the user data access object.
 * <p>
 * This interface defines all the specific methods to handle user objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager. The generic dao interface, that this interface
 * implements too, implements the methods commun to all data access objects.
 *
 * @see #findByUsername(String)
 * @see #findByEmail(String)
 */

public interface UserDAO extends GenericDAO<User, Long>
{
	/**
	 * Find a user by name.
	 * <p>
	 * This method is used to find a particular user in the database from its
	 * username.
	 *
	 * @param username Username of the user to search form.
	 *
	 * @return Found user
	 *
	 * @throws ModelException In case of problem.
	 */

	User findByUsername(String username) throws ModelException;

	/**
	 * Find a user by email.
	 * <p>
	 * This method is used to find a particular user in the database from its
	 * email.
	 *
	 * @param email Email of the user to search form.
	 *
	 * @return Found user
	 *
	 * @throws ModelException In case of problem.
	 */

	User findByEmail(String email) throws ModelException;
}
