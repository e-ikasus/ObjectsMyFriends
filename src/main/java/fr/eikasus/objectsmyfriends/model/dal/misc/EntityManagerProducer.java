package fr.eikasus.objectsmyfriends.model.dal.misc;

import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Class used to handle EntityManager instantiation.
 * <p>
 * This class is used to manage the production of the EntityManager object by
 * the container. This EntityManager object can then be injected whenever it is
 * necessary by using the @Inject and @EntityManagerRequestScoped annotations.
 * His life cycle is the request.
 */

public class EntityManagerProducer
{
	// Entity manager factory commun to all instances.
	static private EntityManagerFactory entityManagerFactory;

	/**
	 * Create an entity manager.
	 * <p>
	 * This method create an entity manager to be injected. It is called by the
	 * container whenever it is needed.
	 *
	 * @return Entity manager for the current request
	 */

	@Produces @RequestScoped @EntityManagerRequestScoped
	public EntityManager createEntityManager()
	{
		// Create the entity manager factory if it is not already done.
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("ObjectsMyFriends");

		// Return a new entity manager.
		return entityManagerFactory.createEntityManager();
	}

	/**
	 * Close the current entity manager.
	 *
	 * @param entityManager Entity manager to close.
	 */

	public void close(@Disposes @EntityManagerRequestScoped @NotNull EntityManager entityManager)
	{
		// Close the entity manager.
		entityManager.close();
	}
}
