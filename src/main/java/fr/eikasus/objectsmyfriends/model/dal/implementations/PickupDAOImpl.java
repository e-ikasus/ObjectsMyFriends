package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.dal.annotations.PickupDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.PickupDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the pickup place data access object.
 * <p>
 * This class supplies all the necessary methods to handle pickup place objects
 * within the data access layer. It is supplied by the DAO factory object and
 * used by its corresponding manager.
 * <p>
 * This is the implementation for database.
 */

@ApplicationScoped @PickupDAODB
public class PickupDAOImpl extends GenericDAOImpl<PickupPlace, Long> implements PickupDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	/**
	 * Constructor of the class used to pass object type to the upper constructor.
	 */

	public PickupDAOImpl()
	{
		super(PickupPlace.class);
	}
}
