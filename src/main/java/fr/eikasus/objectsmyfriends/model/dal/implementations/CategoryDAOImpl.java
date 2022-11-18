package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.dal.annotations.CategoryDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.CategoryDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the category data access object.
 * <p>
 * This class supplies all the necessary methods to handle category objects
 * within the data access layer. It is supplied by the DAO factory object and
 * used by its corresponding manager.
 * <p>
 * This is the implementation for database.
 */

@ApplicationScoped @CategoryDAODB
public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements CategoryDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public CategoryDAOImpl()
	{
		super(Category.class);
	}
}
