package fr.eikasus.objectsmyfriends.controller;

import com.google.gson.Gson;
import fr.eikasus.objectsmyfriends.model.bll.ManagerFactory;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import fr.eikasus.objectsmyfriends.model.misc.ModelException;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

@WebServlet(name = "WelcomeServlet", urlPatterns = {"/welcome", "/index.jsp", "/index.html"})
public class WelcomeServlet extends HttpServlet
{
	@Inject ManagerFactory managerFactory;

	@SuppressWarnings("unchecked")
	@Override protected void doGet(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HashMap<String, String> savedParams = new HashMap<>();
		String param;
		boolean receiveParam = false;
		Gson gson = new Gson();

		String[] knownParams = {"category", "keywords", "openedBids", "currentBids", "wonBids", "myCurrentSales", "myPendingSales", "myEndedSales", "searchType"};

		// No item is currently selected. This is necessary for the image handler.
		request.getSession().removeAttribute("item");
		request.getSession().removeAttribute("itemImages");

		try
		{
			// Read the available categories.
			request.setAttribute("categories", managerFactory.getCategoryManager().find(null));
		}
		catch (ModelException e)
		{
			// Do nothing.
		}

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

			// If nothing is defined, put the default values.
			if (savedParams.size() == 0)
			{
				// The user will search for the purchases.
				savedParams.put("searchType", "purchases");

				// and available items to buy.
				savedParams.put("openedBids", "openedBids");
			}
		}

		try
		{
			//request.setAttribute("items", ItemManager.getInstance().findByCriteria(null, UserRole.BUYER, new Search().setOpenedBids(), null, ""));
			request.setAttribute("items", managerFactory.getItemManager().find(null));
		}
		catch (ModelException e)
		{
			request.setAttribute("items", new ArrayList<Item>());
		}

		// Puts received parameter to request to be accessed by the JSP.
		savedParams.forEach(request::setAttribute);

		// Send to the browser the updated cookie.
		response.addCookie(new Cookie("searchOptions2", Base64.getEncoder().encodeToString(gson.toJson(savedParams).getBytes())));

		// Go to the welcome page => take search options into account.
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/welcome.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
