package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Image;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.dal.DAOFactory;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

public interface ImageManager
{
	void setManagerFactory(ManagerFactory managerFactory);

	DAOFactory getDaoFactory();

	Image add(Item item, String path) throws ModelException;

	void delete(@NotNull Image image) throws ModelException;
}
