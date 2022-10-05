package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

public interface UserDAO extends GenericDAO<User, Long>
{
	User findByUsername(String username) throws ModelException;

	User findByEmail(String email) throws ModelException;
}
