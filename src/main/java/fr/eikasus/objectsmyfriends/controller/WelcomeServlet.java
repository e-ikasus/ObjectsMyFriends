package fr.eikasus.objectsmyfriends.controller;

import com.google.gson.Gson;
import com.mysql.cj.protocol.a.NativeConstants;
import com.sun.deploy.net.HttpRequest;
import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Category;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.bo.User;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
import fr.eikasus.objectsmyfriends.model.misc.Search;
import fr.eikasus.objectsmyfriends.model.misc.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@WebServlet(name = "WelcomeServlet", urlPatterns = {"/welcome", "/index.jsp", "/index.html"})
public class WelcomeServlet extends HttpServlet
{
	private final String CATEGORY = "category";

	private final String KEYWORDS = "keywords";

	private final String OPENED_BIDS = "openedBids";

	private final String CURRENT_BIDS = "currentBids";

	private final String WON_BIDS = "wonBids";

	private final String MY_CURRENT_SALES = "myCurrentSales";

	private final String MY_PENDING_SALES = "myPendingSales";

	private final String MY_ENDED_SALES = "myEndedSales";

	private final String SEARCH_TYPE = "searchType";

	private final String[] knownParams = {CATEGORY, KEYWORDS, OPENED_BIDS, CURRENT_BIDS, WON_BIDS, MY_CURRENT_SALES, MY_PENDING_SALES, MY_ENDED_SALES, SEARCH_TYPE};

	@Inject ManagerFactory managerFactory;

	@Override protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Search searchOptions = new Search();
		UserRole role;
		String requestedCategory;
		Category category;
		List<Category> categories;

		// Connected user.
		User user = (User) request.getSession().getAttribute("user");

		// No item is currently selected. This is necessary for the image handler.
		request.getSession().removeAttribute("item");
		request.getSession().removeAttribute("itemImages");

		// Read parameters from request ok cookie.
		readCriteriaSearch(request, response);

		// create and define search criteria.
		if (request.getAttribute(OPENED_BIDS) != null ) searchOptions.setOpenedBids();
		if (request.getAttribute(CURRENT_BIDS) != null ) searchOptions.setMyCurrentBids();
		if (request.getAttribute(WON_BIDS) != null ) searchOptions.setMyWonBids();
		if (request.getAttribute(MY_CURRENT_SALES) != null ) searchOptions.setMyCurrentSales();
		if (request.getAttribute(MY_PENDING_SALES) != null ) searchOptions.setMyWaitingSales();
		if (request.getAttribute(MY_ENDED_SALES) != null ) searchOptions.setMyEndedSales();

		// Define the user role.
		if (searchOptions.isMyCurrentSales() || searchOptions.isMyWaitingSales() || searchOptions.isMyEndedSales() ) role = UserRole.SELLER;
		else role = UserRole.BUYER;

		try
		{
			// Read the available categories. Will also be used by the view.
			categories = managerFactory.getCategoryManager().find(null);
			request.setAttribute("categories", categories);

			// Category chosen by the user.
			requestedCategory = (String) request.getAttribute(CATEGORY);

			if (requestedCategory == null) category = null;
			else category = categories.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getLabel(), requestedCategory)).findFirst().orElse(null);

			request.setAttribute("items", managerFactory.getItemManager().findByCriteria(user, role, searchOptions, category, (String) request.getAttribute(KEYWORDS)));
			//request.setAttribute("items", managerFactory.getItemManager().find(null));
		}
		catch (ModelException e)
		{
			request.setAttribute("items", new ArrayList<Item>());
		}

		// Go to the welcome page => take search options into account.
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/welcome.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	/* ************** */
	/* Helper methods */
	/* ************** */

	/**
	 * Read filter options parameters from the request.
	 * <p>
	 * This method read parameters from the request and update those that were
	 * previously supplied and stored in the cookie named "searchOptions2".
	 * In all cases, the parameters used to filter the items shown in the welcome
	 * page are to be taken from the request after the execution of this method.
	 *
	 * @param request Http request.
	 * @param response Http response.
	 */

	@SuppressWarnings("unchecked")
	private void readCriteriaSearch(HttpServletRequest request, HttpServletResponse response)
	{
		HashMap<String, String> savedParams = new HashMap<>();
		String param;
		boolean receiveParam = false;
		Gson gson = new Gson();

		// Connected user.
		User user = (User) request.getSession().getAttribute("user");

		// See if known parameters are received from the request.
		for (String knownParam : knownParams)
		{
			// If a parameter is found.
			if ((param = request.getParameter(knownParam)) != null)
			{
				// Save it.
				savedParams.put(knownParam, param);

				// Parameters were received, so cookie need to be updated.
				receiveParam = true;
			}
		}

		// If no parameter is received, check if a cookie exist.
		if (!receiveParam)
		{
			// Retrieve cookies list.
			Cookie[] cookies = request.getCookies();

			// If at least one cookie is supplied.
			if (cookies != null)
			{
				// Find the appropriate cookie.
				for (Cookie cookie : cookies)
				{
					// If the one related to search options is found.
					if (cookie.getName().equals("searchOptions2"))
					{
						// Decode base64 data.
						byte[] decoded = Base64.getDecoder().decode(cookie.getValue());

						// Retrieve current settings.
						savedParams = gson.fromJson(new String(decoded), HashMap.class);

						// Cookie found, so stop the scan.
						break;
					}
				}
			}

			// If nothing is defined, put the default values. Do the same if the user
			// is not connected because in that case, he can only see opened bids.
			if ( (savedParams.size() == 0) || (user == null) )
			{
				// The user will search for the purchases.
				savedParams.put(SEARCH_TYPE, "purchases");

				// and available items to buy.
				savedParams.put(OPENED_BIDS, "1");
			}
		}

		if (user == null)
		{
			savedParams.remove(MY_CURRENT_SALES);
			savedParams.remove(MY_ENDED_SALES);
			savedParams.remove(MY_PENDING_SALES);
		}

		// Puts received parameter to request to be accessed by the JSP.
		savedParams.forEach(request::setAttribute);

		System.out.println("filters parameters:");
		for (String s : knownParams) System.out.printf("%s = %s\n", s, request.getAttribute(s));
		System.out.println();

		// Send to the browser the updated cookie.
		response.addCookie(new Cookie("searchOptions2", Base64.getEncoder().encodeToString(gson.toJson(savedParams).getBytes())));
	}
}
