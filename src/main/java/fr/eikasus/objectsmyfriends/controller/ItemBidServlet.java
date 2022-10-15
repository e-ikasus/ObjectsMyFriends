package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.BidManager;
import fr.eikasus.objectsmyfriends.model.bll.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "ItemBidServlet", value = "/item_bid")
public class ItemBidServlet extends HttpServlet
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Property list for the form.
	HashMap<Object, String> formParameters = new HashMap<>();

	ControllerSupport controllerSupport = ControllerSupport.getInstance();

	// Managers to use to handle items
	ItemManager itemManager = ItemManager.getInstance();
	BidManager bidManager = BidManager.getInstance();

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Initialise the servlet.
	 * <p></p>
	 * This method does initialisation stuff for the controller handling the bid
	 * form. It fills the formParameters property of links between error codes
	 * and fields in the form that references user data and HTML elements
	 * containing translated error messages.
	 */

	@Override public void init() throws ServletException
	{
		super.init();

		/*formParameters.put(ModelError.UNABLE_TO_CREATE_ITEM, "genericError");
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
		formParameters.put(ModelError.INVALID_PICKUP_PLACE_CITY, "city");*/
	}

	/**
	 * Display the bid form.
	 */

	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long itemIdentifier;
		Item item = null;

		try
		{
			if ((itemIdentifier = controllerSupport.parseLongParameter(request, "item")) != 0)
			{
				// Read the item from the database.
				item = itemManager.find(itemIdentifier).get(0);

				// Save the item instance to allow doPost method to do his job.
				request.getSession().setAttribute("item", item);
			}
		}
		catch (ModelException me)
		{
			// When something goes wrong, return to the welcome page.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/itemBid.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Handle the bid form to register a bid.
	 */

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}
}
