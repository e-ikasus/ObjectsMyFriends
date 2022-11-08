package fr.eikasus.objectsmyfriends.model.dal.implementations;

import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.dal.interfaces.PickupDAO;

/**
 * Class used to implement the pickup place data access object.
 */

public class PickupDAOImpl extends GenericDAOImpl<PickupPlace, Long> implements PickupDAO
{
	public PickupDAOImpl(DAOFactory daoFactory)
	{
		super(PickupPlace.class, daoFactory);
	}
}
