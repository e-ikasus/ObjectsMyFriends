package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.BidId;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

public interface BidDAO extends GenericDAO<Bid, BidId>
{
	Bid findBestBid(Item item) throws ModelException;
}
