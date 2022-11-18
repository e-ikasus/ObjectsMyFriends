package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerError;
import fr.eikasus.objectsmyfriends.misc.ControllerException;
import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.*;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "ItemSellServlet", value = "/item_sell")
public class ItemSellServlet extends HttpServlet
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Property list for the form.
	HashMap<Object, String> formParameters = new HashMap<>();

	// Formatter for the date.
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Inject
	ManagerFactory managerFactory;

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Initialise the servlet.
	 * <p>
	 * This method does initialisation stuff for the controller handling the item
	 * sell form. It fills the formParameters property of links between error
	 * codes and fields in the form that references user data and HTML elements
	 * containing translated error messages.
	 */

	@Override public void init() throws ServletException
	{
		super.init();

		formParameters.put(ModelError.UNABLE_TO_CREATE_ITEM, "genericError");
		formParameters.put(ModelError.UNABLE_TO_UPDATE_ITEM, "genericError");
		formParameters.put(ModelError.UNABLE_TO_CREATE_PICKUP_PLACE, "genericError");
		formParameters.put(ModelError.INVALID_ITEM_NAME, "name");
		formParameters.put(ModelError.INVALID_ITEM_DESCRIPTION, "description");
		formParameters.put(ModelError.INVALID_ITEM_CATEGORY, "category");
		formParameters.put(ModelError.INVALID_ITEM_PRICE, "initialPrice");
		formParameters.put(ModelError.INVALID_ITEM_START_DATE, "biddingStart");
		formParameters.put(ModelError.INVALID_ITEM_END_DATE, "biddingEnd");
		formParameters.put(ModelError.INVALID_PICKUP_PLACE_STREET, "street");
		formParameters.put(ModelError.INVALID_PICKUP_PLACE_ZIPCODE, "zipCode");
		formParameters.put(ModelError.INVALID_PICKUP_PLACE_CITY, "city");
	}

	/**
	 * Display the creation/update item form.
	 * <p>
	 * When called without parameter, This method display the form for the user to
	 * create an item. In that case, all fields are empty except the pickup place
	 * which contains the address of the curent user. When a parameter is supplied
	 * via the URL address which is the item identifier, the item is then read
	 * from the database and all the form fields are filled with its information.
	 * If no pickup place is defined for that item, then the pickup place fields
	 * are filled with the user address. When an item is to be updated, its
	 * instance is stored in the user session as the named attribute "item" which
	 * allow to doPost method to avoid creating multiple items when something goes
	 * wrong during the update or creation process of the item.
	 */

	@Override protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long itemIdentifier;
		Item item = null;

		// A get request means a new action, so ensure there is no image.
		request.getSession().removeAttribute("itemImages");

		try
		{
			// If no parameter is supplied in the url, this means the user wants to
			// create an item, otherwise it is necessary to read this item from the
			// database to fill the form with its information. The item should then be
			// put in the session to warn the doPost method of an update.
			if ((itemIdentifier = ControllerSupport.parseLongParameter(request, "item")) != 0)
			{
				// Read the item from the database.
				item = managerFactory.getItemManager().find(itemIdentifier).get(0);

				// As this is an update, put the item in the session to be further
				// retrieved by the doPost method.
				request.getSession().setAttribute("item", item);

				// Fill the form with item information.
				request.setAttribute("name", item.getName());
				request.setAttribute("description", item.getDescription());
				request.setAttribute("category", item.getCategory());
				request.setAttribute("initialPrice", item.getInitialPrice());
				request.setAttribute("biddingStart", dateFormat.format(item.getBiddingStart()));
				request.setAttribute("biddingEnd", dateFormat.format(item.getBiddingEnd()));

				// If a pickup place is defined, put its information in the form.
				if (item.getPickupPlace() != null)
				{
					// Get information from the pickup place.
					request.setAttribute("street", item.getPickupPlace().getStreet());
					request.setAttribute("zipCode", item.getPickupPlace().getZipCode());
					request.setAttribute("city", item.getPickupPlace().getCity());
				}
			}

			// If an item is to be created or already exist but with no pickup place.
			if ((item == null) || (item.getPickupPlace() == null))
			{
				// Get the information from the user who is the owner of the item.
				User user = (User) request.getSession().getAttribute("user");

				request.setAttribute("street", user.getStreet());
				request.setAttribute("zipCode", user.getZipCode());
				request.setAttribute("city", user.getCity());
			}

			// List of available categories into the form.
			request.setAttribute("categories", managerFactory.getCategoryManager().find(null));

			// All is fine, display the item sell form.
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/itemSell.jsp");
			requestDispatcher.forward(request, response);
		}
		catch (ModelException me)
		{
			// When something goes wrong, return to the welcome page.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}

	/**
	 * Update or create an item.
	 * <p>
	 * This method is called when the user presses the save button of the HTML
	 * form. All form parameters are then retrieved from the request. If an item
	 * already exists in the user session, then it is updated with the parameters
	 * that have been modified by the user. If no item exists in the user session,
	 * an item is created in the database and is also saved in the user session
	 * to prevent the recreation of the same item if something goes wrong after
	 * the item body was created, but before the end of the pickup place creation
	 * or images. If something goes wrong, all form data supplied as parameters
	 * are transferred to the JSP via attributes. After the item is completely
	 * created, its instance, like other part of it, is removed form the user
	 * session. This method doesn't call doGet method to display the form again
	 * but call the JSP directly. Note that because the item information are tied
	 * to more than one object, errors couldn't be displayed at the same time in
	 * the form.
	 */

	@Override protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("UTF-8");

		// Retrieve the connected user. At this stage, there is always one because
		// of the filter servlet. This user is the item owner
		User user = ControllerSupport.getUserFromSession(managerFactory, request);

		// Retrieve the item from the session if it exists for updating.
		Item item = ControllerSupport.getItemFromSession(managerFactory, request);

		// By default, all works fine.
		boolean success = true;

		// Check if the user wants to create or update an item.
		if ( (user != null) && (request.getParameter("save") != null) )
		{
			// Retrieve the item images from the session if user upload them.
			List<String> uploadedImages = (List<String>) request.getSession().getAttribute("itemImages");

			// Retrieve the pickup place that belongs to the item if exists.
			PickupPlace pickupPlace = (item != null) ? (item.getPickupPlace()) : (null);

			// Create list that will receive new properties.
			HashMap<String, Object> newProperties = new HashMap<>();

			// Retrieve received item information.
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String categoryLabel = request.getParameter("category");

			// Retrieve received pickup place information.
			String street = request.getParameter("street");
			String zipCode = request.getParameter("zipCode");
			String city = request.getParameter("city");

			// Retrieve received sale information.
			int initialPrice = ControllerSupport.parseIntegerParameter(request, "initialPrice");
			Date biddingStart = ControllerSupport.parseDateParameter(request, "biddingStart");
			Date biddingEnd = ControllerSupport.parseDateParameter(request, "biddingEnd");

			try
			{
				// Retrieve the category chosen by the user for that item.
				Category category = managerFactory.getCategoryManager().find(categoryLabel).get(0);

				// Read categories again in case of further pb.
				request.setAttribute("categories", managerFactory.getCategoryManager().find(null));

				// If an item is present in the session, this means an update is to be
				// done, otherwise the item should be created.
				if (item != null)
				{
					// Find the modified properties to put into the list.
					if (item.getName().compareTo(name) != 0) newProperties.put("name", name);
					if (item.getDescription().compareTo(description) != 0) newProperties.put("description", description);
					if (item.getCategory() != category) newProperties.put("category", category);
					if (item.getInitialPrice() != initialPrice) newProperties.put("initialPrice", initialPrice);
					if (item.getBiddingStart().compareTo(biddingStart) != 0) newProperties.put("biddingStart", biddingStart);
					if (item.getBiddingEnd().compareTo(biddingEnd) != 0) newProperties.put("biddingEnd", biddingEnd);

					// Try to update the item.
					managerFactory.getItemManager().update(item, newProperties);
				}
				else
				{
					// Create the item.
					item = managerFactory.getItemManager().add(name, description, biddingStart, biddingEnd, initialPrice, user, category);
				}

				if (pickupPlace != null)
				{
					// Clear the list because an item was maybe updated.
					newProperties.clear();

					if (pickupPlace.getStreet().compareTo(street) != 0) newProperties.put("street", street);
					if (pickupPlace.getZipCode().compareTo(zipCode) != 0) newProperties.put("zipCode", zipCode);
					if (pickupPlace.getCity().compareTo(city) != 0) newProperties.put("city", city);

					// Try to update the pickup place.
					managerFactory.getPickupManager().update(pickupPlace, newProperties);
				}
				else
				{
					// Create the pickup place and attach-it to the item.
					managerFactory.getPickupManager().add(item, street, zipCode, city);
				}

				// Now deal with images that have a special process.
				ControllerSupport.addUploadedImagesToItem(managerFactory, request, item, uploadedImages);
			}
			catch (Exception me)
			{
				// An error occurred.
				success = false;

				// Transform parameters to attributes for saving the form.
				ControllerSupport.saveForm(request, formParameters);

				// Put the error in the form.
				if (me instanceof ModelException)
					ControllerSupport.putFormError((ModelException) me, request, formParameters);
				else
					ControllerSupport.putFormError(new ControllerException(me, ControllerError.UNATTENDED_ERROR), request, formParameters);

				// Save the item. It was created or updated, but not the pickup place.
				if (item != null) request.getSession().setAttribute("item", item);

				// Return to the item page and display errors.
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/itemSell.jsp");
				requestDispatcher.forward(request, response);
			}
		}

		// If no error occurred during creation/update or if user canceled the form.
		if (success)
		{
			// The item is created or the user canceled the form, so remove-it from the
			// session.
			request.getSession().removeAttribute("item");

			// Do the same with images.
			request.getSession().removeAttribute("itemImages");

			// Return to the welcome page.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
