package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.dal.annotations.CategoryDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.CategoryDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the category data access object.
 */

@ApplicationScoped @CategoryDAODB
public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements CategoryDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	public CategoryDAOImpl()
	{
		super(Category.class);
	}
}
