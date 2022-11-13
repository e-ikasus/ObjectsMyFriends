package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.PickupPlace;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface PickupManager
{
	void setManagerFactory(ManagerFactory managerFactory);

	DAOFactory getDaoFactory();

	PickupPlace add(Item item, String street, String zipCode, String city) throws ModelException;

	PickupPlace find(@NotNull Item item) throws ModelException;

	void update(@NotNull PickupPlace pickupPlace, @NotNull HashMap<String, Object> properties) throws ModelException;

	void delete(@NotNull PickupPlace pickupPlace) throws ModelException;
}
