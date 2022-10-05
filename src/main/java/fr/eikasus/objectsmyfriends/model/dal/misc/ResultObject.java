package fr.eikasus.objectsmyfriends.model.dal.misc;

import fr.eikasus.objectsmyfriends.model.misc.ModelException;

/**
 * Interface used to pass a method in parameter that returns an object. The
 * implemented method can throw exceptions.
 *
 * @param <T> Type of object to return.
 */

public interface ResultObject<T>
{
	T execute() throws ModelException;
}
