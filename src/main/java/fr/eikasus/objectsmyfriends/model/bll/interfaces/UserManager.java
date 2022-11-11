package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public interface UserManager
{
	User add(String username, String firstName, String lastName, String email, String phoneNumber, String street, String zipCode, String city, String plainPassword, int credit, boolean admin) throws ModelException;

	User find(String username, String email, String plainPassword) throws ModelException;

	List<User> find(Long identifier) throws ModelException;

	void update(User user, @NotNull HashMap<String, Object> properties) throws ModelException;

	void delete(User user, boolean archived) throws ModelException;
}
