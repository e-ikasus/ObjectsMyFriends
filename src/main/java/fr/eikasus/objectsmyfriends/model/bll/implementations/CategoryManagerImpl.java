package fr.eikasus.objectsmyfriends.model.bll.implementations;

import fr.eikasus.objectsmyfriends.model.bll.annotations.CategoryManagerDB;
import fr.eikasus.objectsmyfriends.model.bll.interfaces.CategoryManager;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Category manager class.
 * <p></p>
 * This class is used to manage categories according to the business logic. It
 * should be used by the controllers to handle categories like adding, deleting
 * and so. The access of one of data access object method by the controllers is
 * strictly forbidden.
 *
 * @see #add(String) add()
 * @see #find(String) find()
 * @see #update(Category, HashMap) update()
 * @see #delete(Category)
 * @see #delete(long)
 */

@ApplicationScoped @CategoryManagerDB
public class CategoryManagerImpl extends GenericManagerImpl implements CategoryManager
{
	/* ******************** */
	/* Constant declaration */
	/* ******************** */

	private static final String VALIDATE_LABEL = "^[0-9A-Za-zéèëêùàÉÈËÊÙÀ' -]+$";

	/* ************* */
	/* Class members */
	/* ************* */

	private final Pattern labelCheck;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	/**
	 * Constructor of the class.
	 */

	public CategoryManagerImpl()
	{
		super();

		// Property validators.
		labelCheck = Pattern.compile(VALIDATE_LABEL);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Create a full qualified category.
	 * <p></p>
	 * This method create and save a category into the database. All parameters
	 * are required to complete the action. If one of that parameters is wrong, an
	 * exception occurs and nothing is saved into the database.
	 *
	 * @param label Name of the category.
	 *
	 * @return Newly created category and added to the database.
	 *
	 * @throws ModelException In case of problem.
	 */

	public Category add(@NotNull String label) throws ModelException
	{
		Category category = new Category(label);

		try
		{
			// Verify the validity of the category.
			validate(category);

			// Save it into the database.
			daoFactory.getCategoryDAO().save(category);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_CREATE_CATEGORY);
		}

		// Return the newly created category.
		return category;
	}

	/**
	 * Find a category.
	 * <p></p>
	 *
	 * Try to find the category whose name is supplied in parameter. If the name
	 * is null, then all categories will be returned.
	 *
	 * @param label Name of the category or null for all.
	 *
	 * @return List of categories found.
	 *
	 * @throws ModelException In case of problem.
	 */

	public List<Category> find(String label) throws ModelException
	{
		try
		{
			// Search and return wanted categories.
			return (label == null) ? (daoFactory.getCategoryDAO().find()) : (daoFactory.getCategoryDAO().findByProperty("label", label));
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.CATEGORY_NOT_FOUND);
		}
	}

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

	public void update(@NotNull Category category, @NotNull HashMap<String, Object> properties) throws ModelException
	{
		Category updatedCategory = new Category(category);
		String string;

		if ((string = (String) properties.get("label")) != null)
		{
			properties.remove("label");
			updatedCategory.setLabel(string);
		}

		// If properties always exist, it means there is a problem.
		if (!properties.isEmpty())
			throw new ModelException(null, ModelError.UNABLE_TO_UPDATE_CATEGORY);

		try
		{
			// Check the category validity.
			validate(updatedCategory);

			// Update the category into the database.
			daoFactory.getCategoryDAO().update(updatedCategory);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_UPDATE_CATEGORY);
		}
	}

	/**
	 * Delete a category.
	 * <p></p>
	 * This method try to delete the category supplied in parameter. If this
	 * category is used by items, then it can be deleted.
	 *
	 * @param category Category to delete
	 * @throws ModelException In case of problem.
	 */

	public void delete(@NotNull Category category) throws ModelException
	{
		try
		{
			daoFactory.getCategoryDAO().delete(category);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_DELETE_CATEGORY);
		}
	}

	/**
	 * Delete a category.
	 * <p></p>
	 * This method try to delete the category whose identifier is supplied in
	 * parameter. If this category is used by items, then it can be deleted.
	 *
	 * @param id Identifier of the category to delete
	 * @throws ModelException In case of problem.
	 */

	public void delete(long id) throws ModelException
	{
		try
		{
			daoFactory.getCategoryDAO().deleteById(id);
		}
		catch (ModelException me)
		{
			// Add generic error.
			throw me.add(ModelError.UNABLE_TO_DELETE_CATEGORY);
		}
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Verify the validity of the supplied category.
	 * <p></p>
	 * This method check the validity of a category to be saved in the database.
	 * If one of his properties is invalid, an exception occur containing errors
	 * detected.
	 *
	 * @param category Category to verify
	 *
	 * @throws ModelException In case of problem.
	 */

	private void validate(@NotNull Category category) throws ModelException
	{
		ModelException exception = new ModelException();
		String string;

		// Check the name validity.
		if (((string = category.getLabel()) == null) || (!labelCheck.matcher(string).matches()) || (string.length() > Category.MAX_LENGTH_LABEL))
			exception.add(ModelError.INVALID_CATEGORY_LABEL);

		// If there is an error, throws an exception.
		if (exception.hasError()) throw exception;
	}
}