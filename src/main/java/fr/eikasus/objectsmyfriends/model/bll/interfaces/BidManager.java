package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

public interface BidManager
{
	void setManagerFactory(ManagerFactory managerFactory);

	DAOFactory getDaoFactory();

	Bid add(@NotNull User user, @NotNull Item item, int price) throws ModelException;

	void delete(@NotNull User user) throws ModelException;
}
