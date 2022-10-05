package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;

import java.util.List;

public interface ItemDAO extends GenericDAO<Item, Long>
{
	List<Item> findByCriteria(User user, UserRole role, Search search, Category category, String keywords) throws ModelException;

	void deleteByCriteria(User user, UserRole role, Search search, Category category, String keywords) throws ModelException;
}
