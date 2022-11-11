package fr.eikasus.objectsmyfriends.model.bll.interfaces;

import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public interface CategoryManager
{
	Category add(@NotNull String label) throws ModelException;

	List<Category> find(String label) throws ModelException;

	void update(@NotNull Category category, @NotNull HashMap<String, Object> properties) throws ModelException;

	void delete(@NotNull Category category) throws ModelException;

	void delete(long id) throws ModelException;
}
