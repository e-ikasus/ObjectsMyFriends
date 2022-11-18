package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.annotations.UserDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.UserDAO;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

/**
 * Class used to implement the user data access object.
 * <p>
 * This class supplies all the necessary methods to handle user objects within
 * the data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager.
 * <p>
 * This is the implementation for database.
 *
 * @see #findByUsername(String)
 * @see #findByEmail(String)
 */

@ApplicationScoped @UserDAODB
public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	// Request for searching one user by its username.
	private final static String JPQL_SELECT1 = "SELECT u FROM User u WHERE u.username = :p1";

	// Request for searching one user by its email.
	private final static String JPQL_SELECT2 = "SELECT u FROM User u WHERE u.email = :p1";

	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public UserDAOImpl()
	{
		super(User.class);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

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

	@Override public User findByUsername(@NotNull String username) throws ModelException
	{
		TypedQuery<User> query;
		User foundUser;

		// Create the query for searching requested user.
		query = entityManager.createQuery(JPQL_SELECT1, User.class);
		query.setParameter("p1", username);

		foundUser = execute(false, ModelError.USER_NOT_FOUND, query::getSingleResult);

		// Return the result.
		return foundUser;
	}

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

	@Override public User findByEmail(@NotNull String email) throws ModelException
	{
		TypedQuery<User> query;
		User foundUser;

		// Create the query for searching requested user.
		query = entityManager.createQuery(JPQL_SELECT2, User.class);
		query.setParameter("p1", email);

		foundUser = execute(false, ModelError.USER_NOT_FOUND, query::getSingleResult);

		// Return the result.
		return foundUser;
	}
}
