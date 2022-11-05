package fr.eikasus.objectsmyfriends.controller;

import com.google.gson.Gson;
import fr.eikasus.objectsmyfriends.misc.ControllerSupport;
import fr.eikasus.objectsmyfriends.model.bll.ItemManager;
import fr.eikasus.objectsmyfriends.model.bo.Item;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ImageServlet", value = "/image_handler") @MultipartConfig
public class ImageServlet extends HttpServlet
{
	@Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		ControllerSupport controllerSupport = ControllerSupport.getInstance();

		try
		{
			// Where the image file names will be stored before sending.
			ArrayList<String> imageFileNames = new ArrayList<>();

			// List of images file name uploaded if exists.
			ArrayList<String> uploadedImages = (ArrayList<String>) request.getSession().getAttribute("itemImages");

			// Item currently edited if exists.
			Item currentItem = (Item) request.getSession().getAttribute("item");

			// For convenience, there is always one list, even if it is empty.
			if (uploadedImages == null) uploadedImages = new ArrayList<>();

			// Retrieve a possible item identifier.
			long identifier = controllerSupport.parseLongParameter(request, "identifier");

			// Find the item to deal with: the one from the database or those that is
			// currently modified/created.
			if ((currentItem == null) && (identifier != 0))
			{
				List<Item> items = ItemManager.getInstance().find(identifier);

				if (items.size() != 0) currentItem = items.get(0);
			}

			// Put the uploaded image file names in the list.
			uploadedImages.forEach(fileName -> imageFileNames.add(controllerSupport.getUrlImage(request, fileName)));

			// Now, put those of the item if defined.
			if (currentItem != null) currentItem.getImages().forEach(image -> imageFileNames.add(controllerSupport.getUrlImage(request, image.getPath())));

			// The response will be in JSON format.
			response.setContentType("application/json");

			// Encoded in UTF-8.
			response.setCharacterEncoding("UTF-8");

			// The image file name list will be sent.
			response.getWriter().println(new Gson().toJson(imageFileNames));

			// Send-it.
			response.getWriter().flush();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());

			// Impossible to deal with the request.
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override protected void doPost(HttpServletRequest request, @NotNull HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html;charset=UTF-8");

		ControllerSupport controllerSupport = ControllerSupport.getInstance();

		try
		{
			// Retrieve already uploaded image list.
			ArrayList<String> itemImages = (ArrayList<String>) request.getSession().getAttribute("itemImages");

			// If the list doesn't exist yet, create-it
			if (itemImages == null)
			{
				// Create the list.
				itemImages = new ArrayList<>();

				// And save-it in the user session.
				request.getSession().setAttribute("itemImages", itemImages);
			}

			// Add the image to the item images list. The list will be scanned for
			// adding image to the current modified/new item.
			itemImages.add(controllerSupport.loadImage(request, "file"));

			// All was fine.
			response.sendError(HttpServletResponse.SC_OK);
		}
		catch (Exception e)
		{
			// Impossible to deal with the request.
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
