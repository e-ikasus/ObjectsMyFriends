package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import java.util.List;

/**
 * Interface used for implementing the commun part of data access object.
 * <p>
 * This interface defines all the commun methods to handle objects within the
 * data access layer. It is supplied by the DAO factory object and used by
 * its corresponding manager. All specific data access objects must implement
 * this interface.
 *
 * @see #findByProperty(String, Object)
 * @see #find()
 * @see #find(Object)
 * @see #save(Object)
 * @see #update(Object)
 * @see #refresh(Object)
 * @see #detach(Object)
 * @see #deleteByProperty(String, Object)
 * @see #delete(Object)
 * @see #deleteById(Object)
 */

public interface GenericDAO<T, U>
{
	/**
	 * Retrieve entities by specific property.
	 * <p>
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

	List<T> findByProperty(String property, Object value) throws ModelException;

	/**
	 * Retrieve all the entities.
	 * <p>
	 * This method is used to retrieve all entities from the database. Be aware
	 * that this can take time and memory consomption if many entities need to be
	 * retrieved.
	 *
	 * @return List of entities.
	 *
	 * @throws ModelException In case of problem.
	 */

	List<T> find() throws ModelException;

	/**
	 * Retrieve one entity.
	 * <p>
	 * This method is used to retrieve an entity form the database whose
	 * identifier is supplied in parameter.
	 *
	 * @return Asked entity.
	 *
	 * @throws ModelException In case of problem.
	 */

	T find(U identifier) throws ModelException;

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

	void save(T entity) throws ModelException;

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

	void update(T entity) throws ModelException;

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

	void refresh(T entity) throws ModelException;

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

	void detach(T entity) throws ModelException;

	/**
	 * Delete entities by specific property.
	 * <p>
	 * This method is used to delete an entity or many from the database whose one
	 * of their property is equal to the value supplied in parameter.
	 * <p>
	 * NO PERSISTENCE CONTEXT SHOULD BE ACTIVE TO USE THIS METHOD.
	 *
	 * @param property Name of the property.
	 * @param value    Value of this property.
	 *
	 * @throws ModelException In case of problem.
	 */

	void deleteByProperty(String property, Object value) throws ModelException;

	/**
	 * Delete an entity from the database.
	 * <p>
	 * The entity supplied in parameter is deleted from the database.
	 *
	 * @param entity Entity to delete from the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	void delete(T entity) throws ModelException;

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

	void deleteById(U identifier) throws ModelException;
}
