package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.CategoryDAO;

/**
 * Class used to implement the category data access object.
 */

public class CategoryDAOImpl extends GenericDAOImpl<Category, Long> implements CategoryDAO
{
	public CategoryDAOImpl()
	{
		super(Category.class);
	}
}
