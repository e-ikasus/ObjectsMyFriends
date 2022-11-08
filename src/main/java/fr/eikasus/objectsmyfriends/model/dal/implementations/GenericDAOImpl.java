package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.misc.ResultList;
import fr.eikasus.objectsmyfriends.model.dal.misc.ResultObject;
import fr.eikasus.objectsmyfriends.model.dal.misc.ResultVoid;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.persistence.*;
import java.util.List;

/**
 * Support for Data Access Objects.
 * <p></p>
 * This abstract class is used to add meaningfully methods for implementing data
 * access objects as easy as possible with a minimum of redondent code. This
 * class also provide methods for accessible objects from database that are
 * common for all entities.
 *
 * @see #findByProperty(String, Object) findByProperty(property, value)
 * @see #find() find()
 * @see #find(Object) find(identifier)
 * @see #save(Object) save(Entity)
 * @see #update(Object) update(Entity)
 * @see #refresh(Object) refresh(Entity)
 * @see #detach(Object) detach(Entity)
 * @see #deleteByProperty(String, Object) deleteByProperty()
 * @see #delete(Object) delete(Entity)
 * @see #deleteById(Object) delete(identifier)
 */

public abstract class GenericDAOImpl<T, U>
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private final static String JPQL_SELECT1 = "SELECT o FROM %s o WHERE o.%s = :p1";

	private final static String JPQL_SELECT2 = "DELETE o FROM %s o WHERE o.%s = :p1";

	/* ************* */
	/* Class members */
	/* ************* */

	protected DAOFactory daoFactory = null;

	protected EntityManager entityManager = null;

	protected Class<T> entityClass;

	protected String className;

	/* *********** */
	/* Constructor */
	/* *********** */

	/**
	 * Constructor of the class.
	 */

	private GenericDAOImpl()
	{
	}

	/**
	 * Real constructor of the class used to instantiate it. The supplied
	 * parameter is used to obtain the class name of the object instanced and then
	 * create a string containing the name of that class.
	 *
	 * @param entity Class of the entity this instance deals with.
	 */

	protected GenericDAOImpl(Class<T> entity, DAOFactory daoFactory)
	{
		this.daoFactory = daoFactory;

		entityManager = this.daoFactory.getEntityManager();

		// Entity class.
		entityClass = entity;

		// Name of the entity class.
		className = entityClass.toString().substring(entityClass.toString().lastIndexOf('.')+1);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Retrieve entities by specific property.
	 * <p></>
	 * This method is used to retrieve an entity or many from the database whose
	 * one of their property is equal to the value supplied in parameter.
	 *
	 * @param property Name of the property.
	 * @param value    Value of this property.
	 *
	 * @return List found entities.
	 *
	 * @throws ModelException In case of problem.
	 */

	public List<T> findByProperty(String property, Object value) throws ModelException
	{
		return execute(false, ModelError.UNABLE_TO_RETRIEVE_ENTITIES, () ->
		{
			String request = String.format(JPQL_SELECT1, className, property);

			TypedQuery<T> query = entityManager.createQuery(request, entityClass);

			query.setParameter("p1", value);

			return query.getResultList();
		});
	}

	/**
	 * Retrieve all the entities.
	 * <p>
	 * This method is used to retrieve all entities from the database. Be aware
	 * that this can take time and memory consomption if many entities need to be
	 * retrieved.
	 *
	 * @return List of entyties.
	 *
	 * @throws ModelException In case of problem.
	 */

	public List<T> find() throws ModelException
	{
		return execute(false, ModelError.UNABLE_TO_RETRIEVE_ENTITIES, () -> entityManager.createQuery("from " + className, entityClass).getResultList());
	}

	/**
	 * Retrieve one entity.
	 * <p>
	 * This method is used to retrieve a entity form the database whose
	 * identifier is supplied in parameter.
	 *
	 * @return Asked entity.
	 *
	 * @throws ModelException In case of problem.
	 */

	public T find(U identifier) throws ModelException
	{
		return execute(false, ModelError.UNABLE_TO_RETRIEVE_ENTITY, () -> entityManager.find(entityClass, identifier));
	}

	/**
	 * Save an entity into the database.
	 * <p>
	 * The entity supplied in parameter is stored in the database. Its
	 * identifier should be zero, otherwise an error occurs.
	 *
	 * @param entity The entity to save into the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void save(T entity) throws ModelException
	{
		execute(true, ModelError.UNABLE_TO_CREATE_ENTITY, () -> entityManager.persist(entity));
	}

	/**
	 * Update an entity into the database.
	 * <p>
	 * The entity supplied in parameter is updated in the database. Its
	 * identifier should not be zero, otherwise an error occurs.
	 *
	 * @param entity The entity to update into the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void update(T entity) throws ModelException
	{
		execute(true, ModelError.UNABLE_TO_UPDATE_ENTITY, () -> entityManager.merge(entity));
	}

	/**
	 * Refresh an entity from the database.
	 * <p>
	 * The entity supplied in parameter is refreshed from the database. Its
	 * identifier should not be zero, otherwise an error occurs.
	 *
	 * @param entity The entity to refresh from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void refresh(T entity) throws ModelException
	{
		execute(false, ModelError.UNABLE_TO_RETRIEVE_ENTITY, () -> entityManager.refresh(entity));
	}

	/**
	 * Detach an entity from the database.
	 * <p>
	 * The entity supplied in parameter is detached from the database, which means
	 * all changes made to this entity will not be reported to the database. This
	 * also means that another find operation for the same object will create
	 * another instance.
	 *
	 * @param entity The entity to detach from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void detach(T entity) throws ModelException
	{
		execute(false, ModelError.UNABLE_TO_RETRIEVE_ENTITY, () -> entityManager.detach(entity));
	}

	/**
	 * Delete entities by specific property.
	 * <p></p>
	 * This method is used to delete an entity or many from the database whose one
	 * of their property is equal to the value supplied in parameter.
	 * <p></p>
	 * NO PERSISTENCE CONTEXT SHOULD BE ACTIVE TO USE THIS METHOD.
	 *
	 * @param property Name of the property.
	 * @param value    Value of this property.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void deleteByProperty(String property, Object value) throws ModelException
	{
		execute(false, ModelError.UNABLE_TO_DELETE_ENTITY, () ->
		{
			String request = String.format(JPQL_SELECT2, className, property);

			TypedQuery<T> query = entityManager.createQuery(request, entityClass);

			query.setParameter("p1", value);

			query.executeUpdate();
		});
	}

	/**
	 * Delete an entity from the database.
	 * <p>
	 * The entity supplied in parameter is deleted from the database.
	 *
	 * @param entity Entity to delete from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void delete(T entity) throws ModelException
	{
		execute(true, ModelError.UNABLE_TO_DELETE_ENTITY, () -> entityManager.remove(entity));
	}

	/**
	 * Delete an entity from the database.
	 * <p>
	 * The entity whose identifier is supplied in parameter is deleted from the
	 * database.
	 *
	 * @param identifier Identifier of the entity to delete from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public void deleteById(U identifier) throws ModelException
	{
		execute(true, ModelError.UNABLE_TO_DELETE_ENTITY, () ->
		{
			T entity = entityManager.find(entityClass, identifier);

			entityManager.remove(entity);
		});
	}

	/**
	 * Execute an action related to the database.
	 * <p>
	 * This method is used whenever an action need to be performed with the
	 * database. The action represent the code that will be executed inside a
	 * transaction or not. This method is used to avoid redondance in code.
	 *
	 * @param errorCode Error returned with the ModelException object in case of
	 *                  problem.
	 * @param action    Action to realize.
	 *
	 * @throws ModelException In case of problem, contain the error code supplied
	 *                        in parameter.
	 */

	protected void execute(boolean transaction, ModelError errorCode, ResultVoid action) throws ModelException
	{
		EntityTransaction entityTransaction = null;

		try
		{
			// If the action need to be performed inside a transaction.
			if (transaction)
			{
				// Obtain an entity transaction for possible rollback.
				entityTransaction = entityManager.getTransaction();

				// After that, an undo is possible in case of problem.
				entityTransaction.begin();
			}

			// Execute the supplied function.
			action.execute();

			// All worked fine, so validate changes.
			if (entityTransaction != null) entityTransaction.commit();
		}
		catch (Exception e)
		{
			// Rollback because there was a problem.
			if (entityTransaction != null) entityTransaction.rollback();

			throw new ModelException(e, errorCode);
		}
	}

	/**
	 * Execute an action related to the database.
	 * <p>
	 * This method is used whenever an action need to be performed with the
	 * database. The action represent the code that will be executed inside a
	 * transaction or not. This method is used to avoid redondance in code.
	 *
	 * @param transaction Whether to use a transaction to perform the desired
	 *                    action.
	 * @param errorCode   Error returned with the ModelException object in case of
	 *                    problem.
	 * @param action      Action to realize.
	 *
	 * @return Result of the operation, depending on the action performed.
	 *
	 * @throws ModelException In case of problem, contain the error code supplied
	 *                        in parameter.
	 */

	protected T execute(boolean transaction, ModelError errorCode, ResultObject<T> action) throws ModelException
	{
		EntityTransaction entityTransaction = null;
		T result;

		try
		{
			// If the action need to be performed inside a transaction.
			if (transaction)
			{
				// Obtain an entity transaction for possible rollback.
				entityTransaction = entityManager.getTransaction();

				// After that, an undo is possible in case of problem.
				entityTransaction.begin();
			}

			// Execute the supplied function.
			result = action.execute();

			// All worked fine, so validate changes.
			if (entityTransaction != null) entityTransaction.commit();
		}
		catch (Exception e)
		{
			// Rollback because there was a problem.
			if (entityTransaction != null) entityTransaction.rollback();

			throw new ModelException(e, errorCode);
		}

		// Return the result of the operation.
		return result;
	}

	/**
	 * Execute an action related to the database.
	 * <p>
	 * This method is used whenever an action need to be performed with the
	 * database. The action represent the code that will be executed inside a
	 * transaction or not. This method is used to avoid redondance in code.
	 *
	 * @param errorCode Error returned with the ModelException object in case of
	 *                  problem.
	 * @param action    Action to realize.
	 *
	 * @return Result of the operation, depending on the action performed.
	 *
	 * @throws ModelException In case of problem, contain the error code supplied
	 *                        in parameter.
	 */

	protected List<T> execute(boolean transaction, ModelError errorCode, ResultList<T> action) throws ModelException
	{
		EntityTransaction entityTransaction = null;
		List<T> result;

		try
		{
			// If the action need to be performed inside a transaction.
			if (transaction)
			{
				// Obtain an entity transaction for possible rollback.
				entityTransaction = entityManager.getTransaction();

				// After that, an undo is possible in case of problem.
				entityTransaction.begin();
			}

			// Execute the supplied function.
			result = action.execute();

			// All worked fine, so validate changes.
			if (entityTransaction != null) entityTransaction.commit();
		}
		catch (Exception e)
		{
			// Rollback because there was a problem.
			if (entityTransaction != null) entityTransaction.rollback();

			throw new ModelException(e, errorCode);
		}

		// Return the result of the operation.
		return result;
	}
}
