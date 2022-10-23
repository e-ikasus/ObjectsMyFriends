package fr.eikasus.objectsmyfriends.controller;

import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.BidManager;
import fr.eikasus.objectsmyfriends.model.bll.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Bid;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelError;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import org.jetbrains.annotations.NotNull;

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
	 * <p></p>
	 * This method is used to display the item bid form to allow the current user
	 * to make an offer on the item which identifier is supplied in attribute. The
	 * method also compute the initial price of the possible offer. If there is no
	 * identifier supplied or if the user is not connected, he is redirected to
	 * the welcome page.
	 */

	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long itemIdentifier;
		boolean welcome = true;

		try
		{
			if ((itemIdentifier = controllerSupport.parseLongParameter(request, "item")) != 0)
			{
				// Read the item from the database.
				Item item = itemManager.find(itemIdentifier).get(0);

				// Save the item instance to allow doPost method to do his job.
				request.getSession().setAttribute("item", item);

				// Read the best bid for the item form the database.
				Bid bestBid = item.getBids().stream().max(Comparator.comparingInt(Bid::getPrice)).orElse(null);

				// Save the bid made on this item to allow doPost method to do his job.
				request.getSession().setAttribute("bestBid", bestBid);

				// Compute initial offer
				int initialOffer = ((bestBid != null) ? (bestBid.getPrice() + 1) : (item.getInitialPrice()));

				// Save-it in the form.
				request.setAttribute("yourOffer", initialOffer);

				// Don't go to the welcome page.
				welcome = false;

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
		if (welcome) response.sendRedirect(request.getContextPath() + "/welcome");
	}

	/**
	 * Handle the bid form to register a bid.
	 * <p></p>
	 * This method is called when tue user press the make offer button. his offer
	 * is the only parameter retrieved for the request. If there is an error in
	 * the bid process, the form is displayed again showing the cause of the
	 * problem. The doGet method is not used to display the form again.
	 */

	@Override protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Check if the user wants to make an offer.
		if (request.getParameter("makeOffer") != null)
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
		else
		{
			// The user doesn't want to make a bid, so return to the welcome page
			response.sendRedirect(request.getContextPath() + "/welcome");
		}
	}
}
