package fr.eikasus.objectsmyfriends.model.dal.interfaces;

import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import java.util.List;

public interface GenericDAO<T, U>
{
	List<T> findByProperty(String property, Object value) throws ModelException;

	List<T> find() throws ModelException;

	T find(U identifier) throws ModelException;

	void save(T entity) throws ModelException;

	void update(T entity) throws ModelException;

	void refresh(T entity) throws ModelException;

	void detach(T entity) throws ModelException;

	void delete(T entity) throws ModelException;

	void deleteById(U identifier) throws ModelException;
}
