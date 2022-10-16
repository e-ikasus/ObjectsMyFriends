package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.BidManager;
import fr.eikasus.objectsmyfriends.model.bll.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Comparator;
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
	 * form. It fills the formParameters property of links between error codes and
	 * fields in the form that references user data and HTML elements containing
	 * translated error messages.
	 */

	@Override public void init() throws ServletException
	{
		super.init();

		formParameters.put(ModelError.UNABLE_TO_CREATE_BID, "genericError");
		formParameters.put(ModelError.INVALID_BID_PRICE, "yourOffer");
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

				// Read the best bid for the item form the database.
				Bid bestBid = item.getBids().stream().max(Comparator.comparingInt(Bid::getPrice)).orElse(null);

				// Save the bid made on this item to allow doPost method to do his job.
				request.getSession().setAttribute("bestBid", bestBid);

				// Go to the bid page.
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/itemBid.jsp");
				requestDispatcher.forward(request, response);
			}
		}
		catch (ModelException me)
		{
			// nothing to do.
		}

		// When something goes wrong, return to the welcome page.
		response.sendRedirect(request.getContextPath() + "/welcome");
	}

	/**
	 * Handle the bid form to register a bid.
	 */

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Retrieve the current user.
		User user = (User) request.getSession().getAttribute("user");

		// Retrieve the item from the session.
		Item item = (Item) request.getSession().getAttribute("item");

		// Retrieve the current user offer.
		int userOffer = controllerSupport.parseIntegerParameter(request, "yourOffer");

		try
		{
			// Add the user bid
			bidManager.add(user, item, userOffer);

			// Return to the welcome page.
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
		catch (ModelException me)
		{
			// Transform parameters to attributes for saving the form.
			controllerSupport.saveForm(request, formParameters);

			// Put the error in the form.
			controllerSupport.putFormError(me, request, formParameters);

			// Return to the bid page and display errors.
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/itemBid.jsp");
			requestDispatcher.forward(request, response);
		}
	}
}
