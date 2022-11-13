package fr.eikasus.objectsmyfriends.model.dal.misc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Handle access to the database.
 */

public class EntityFactory
{
	/*private static EntityManagerFactory entityManagerFactory = null;

	static
	{
		entityManagerFactory = Persistence.createEntityManagerFactory("ObjectsMyFriends");
	}*/

	/**
	 * Get the object needed to access the database.
	 *
	 * Retrieve the EntityManager object necessary to access the entities in the
	 * database.
	 *
	 * @return The EntityManager object.
	 */
/*
	public static EntityManager getEntityManager() throws Exception
	{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		if (entityManager == null)
			throw new Exception("Impossible to get access to the database.");

		return entityManager;
	}*/
}
