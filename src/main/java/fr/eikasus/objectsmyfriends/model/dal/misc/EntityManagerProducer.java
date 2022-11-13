package fr.eikasus.objectsmyfriends.model.dal.misc;

import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer
{
	static private EntityManagerFactory entityManagerFactory;

	@Produces @RequestScoped @EntityManagerRequestScoped
	public EntityManager createEntityManager()
	{
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory("ObjectsMyFriends");

		return entityManagerFactory.createEntityManager();
	}

	public void close(@Disposes @EntityManagerRequestScoped @NotNull EntityManager entityManager)
	{
		entityManager.close();
	}
}
