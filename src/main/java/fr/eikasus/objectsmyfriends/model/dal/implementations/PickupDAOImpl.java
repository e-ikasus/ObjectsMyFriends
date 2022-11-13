package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.dal.annotations.PickupDAODB;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.PickupDAO;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class used to implement the pickup place data access object.
 */

@ApplicationScoped @PickupDAODB
public class PickupDAOImpl extends GenericDAOImpl<PickupPlace, Long> implements PickupDAO
{
	/* ************ */
	/* Constructors */
	/* ************ */

	public PickupDAOImpl()
	{
		super(PickupPlace.class);
	}
}
