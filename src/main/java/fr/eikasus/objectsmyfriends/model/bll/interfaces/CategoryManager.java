package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Interface used for implementing category manager class.
 * <p>
 * This interface defines all the specific methods that must be implemented to
 * manage category items. Methods present in the interface implementation are
 * allowed to use as many as data access objects required to accomplish their
 * goal. The access of one of data access object method by the controllers is
 * strictly forbidden.
 *
 * @see #add(String)
 * @see #find(String)
 * @see #update(Category, HashMap)
 * @see #delete(Category)
 * @see #delete(long)
 */

public interface CategoryManager
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
	 * Create a full qualified category.
	 * <p>
	 * This method create and save a category into the persistence unit. All
	 * parameters are required to complete the action. If one of that parameters
	 * is wrong, an exception occurs and nothing is saved.
	 *
	 * @param label Name of the category.
	 *
	 * @return Newly created category.
	 *
	 * @throws ModelException In case of problem.
	 */

	Category add(@NotNull String label) throws ModelException;

	/**
	 * Find a category.
	 * <p>
	 * Try to find the category whose name is supplied in parameter. If the name
	 * is null, then all categories will be returned.
	 *
	 * @param label Name of the category or null for all.
	 *
	 * @return List of categories found.
	 *
	 * @throws ModelException In case of problem.
	 */

	List<Category> find(String label) throws ModelException;

	/**
	 * Update category properties.
	 * <p>
	 * This method update the properties of the supplied category. Each property
	 * new value should be supplied as a pair of key/value, a key representing the
	 * exact name or the category property, including case. If a key as an
	 * undefined property name, an exception is generated.
	 *
	 * @param category   Category to be updated.
	 * @param properties Category properties to update.
	 *
	 * @throws ModelException In case of problem.
	 */

	void update(@NotNull Category category, @NotNull HashMap<String, Object> properties) throws ModelException;

	/**
	 * Delete a category.
	 * <p>
	 * This method try to delete the category supplied in parameter. If this
	 * category is used by items, then it can't be deleted.
	 *
	 * @param category Category to delete
	 * @throws ModelException In case of problem.
	 */

	void delete(@NotNull Category category) throws ModelException;

	/**
	 * Delete a category.
	 * <p>
	 * This method try to delete the category whose identifier is supplied in
	 * parameter. If this category is used by items, then it can't be deleted.
	 *
	 * @param id Identifier of the category to delete
	 * @throws ModelException In case of problem.
	 */

	void delete(long id) throws ModelException;
}
