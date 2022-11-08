package fr.eikasus.objectsmyfriends.model.bll;

import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Generic manager class.
 * <p></p>
 * This class is an abstract class used to provide common methods for avoiding
 * redondance in the code. All entity manager must extend it.
 *
 * @see #setEntityProperty(Object, HashMap, String, boolean) setEntityProperty()
 */

public abstract class GenericManager
{
	/* ************* */
	/* Class members */
	/* ************* */

	protected ManagerFactory managerFactory;

	/* *************************** */
	/* Constructors and instancier */
	/* *************************** */

	protected GenericManager(ManagerFactory managerFactory)
	{
		this.managerFactory = managerFactory;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Set an entity property.
	 * <p></p>
	 * This method is used to change an entity property whose name and value are
	 * stored in the supplied list. This is a helper method to prevent for using
	 * redondent code.
	 *
	 * @param object     Object to change property for.
	 * @param properties Properties list
	 * @param name       Name of the property to change.
	 * @param last       CHeck if the supplied list is empty.
	 *
	 * @throws ModelException IN case of problem
	 */

	protected void setEntityProperty(Object object, HashMap<String, Object> properties, String name, boolean last) throws ModelException
	{
		try
		{
			Object objectProperty;

			// If the named property is found in the list.
			if ((objectProperty = properties.get(name)) != null)
			{
				// Retrieve the object class.
				Class<?> objectClass = object.getClass();

				// Found the property.
				Field field = objectClass.getDeclaredField(name);

				// Necessary because this property is private.
				field.setAccessible(true);

				// Set the property new value.
				field.set(object, objectProperty);

				// Remove the value from the list supplied.
				properties.remove(name);
			}
		}
		catch (Exception exc)
		{
			throw new ModelException(exc, ModelError.UNKNOWN_ENTITY_PROPERTY);
		}

		// If this was the last property, check if list is empty for unknown.
		if ((last) && (!properties.isEmpty()))
			throw new ModelException(null, ModelError.UNKNOWN_ENTITY_PROPERTY);
	}
}
