package fr.eikasus.objectsmyfriends.model.dal.misc;

import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import java.util.List;

/**
 * Interface used to pass a method in parameter that returns a list of objects.
 * The implemented method can throw exceptions.
 *
 * @param <T> Type of objects list to return.
 */

public interface ResultList<T>
{
	List<T> execute() throws ModelException;
}
