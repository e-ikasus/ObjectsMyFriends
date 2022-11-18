package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Category;

/**
 * Interface used for implementing the category data access object.
 * <p>
 * This interface defines all the specific methods to handle category objects
 * within the data access layer. It is supplied by the DAO factory object and
 * used by its corresponding manager. The generic dao interface, that this
 * interface implements too, implements the methods commun to all data access
 * objects.
 */

public interface CategoryDAO extends GenericDAO<Category, Long>
{
}
