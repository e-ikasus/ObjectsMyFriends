package fr.eikasus.objectsmyfriends.model.dal.misc;

import fr.eikasus.objectsmyfriends.model.misc.ModelException;

/**
 * Interface used to pass a method in parameter that returns nothing. The
 * implemented method can throw exceptions.
 */

public interface ResultVoid
{
	void execute() throws ModelException;
}
