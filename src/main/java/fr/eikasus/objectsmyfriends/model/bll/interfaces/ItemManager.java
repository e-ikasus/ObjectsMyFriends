package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ItemManager
{
	Item add(String name, String description, Date biddingStart, Date biddingEnd, int initialPrice, User seller, Category category) throws ModelException;

	List<Item> find(Long identifier) throws ModelException;

	List<Item> findByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException;

	void update(Item item, HashMap<String, Object> properties) throws ModelException;

	void delete(@NotNull List<Item> items) throws ModelException;

	void deleteByCriteria(User user, UserRole role, @NotNull Search search, Category category, String keywords) throws ModelException;
}
