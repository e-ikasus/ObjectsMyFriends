window.welcome = {};

(function (object)
	{
		/**
		 * Initialisation stuff for the search form.
		 */

		object.initialize = function (path)
		{
			// URL to the items images handler.
			this.imageHandlerPath = path;

			// Handler for radio buttons to disable HTML elements not selectable.
			$("#purchases,#sales").on("click", () => this.radioButtonsHandler());

			// Handler for loading images after DOM loaded.
			$(window).on("load", () => this.loadItemImages());
		}

		/**
		 * Handles click on radio buttons.
		 *
		 * This function update the states of the HTML element according to the choice made by the user about search
		 * options.
		 *
		 * @param event
		 */

		object.radioButtonsHandler = function ()
		{
			if ($("#purchases").prop("checked"))
			{
				$("#openedBids, #currentBids, #wonBids").prop("disabled", false);
				$("#purchasesFieldset label").removeClass("labelDisable");

				$("#myCurrentSales, #myPendingSales, #myEndedSales").prop("disabled", true);
				$("#salesFieldset label").addClass("labelDisable");
			}
			else
			{
				$("#myCurrentSales, #myPendingSales, #myEndedSales").prop("disabled", false);
				$("#salesFieldset label").removeClass("labelDisable");

				$("#openedBids, #currentBids, #wonBids").prop("disabled", true);
				$("#purchasesFieldset label").addClass("labelDisable");
			}
		}

		/**
		 * Load images for item.
		 *
		 * Load all images associated to each item by querying the images handler of the application. Data received are in
		 * JSON format as an array of URLs for the different item images. Images are downloaded by inserting in the DOM
		 * img elements. Each element receive a handler for dealing with animation stage. When the animation of the front
		 * image is done, this image is put back to his siblings and the new front image received an animation which is
		 * started. The process then do the same for another iteration.
		 */

		object.loadItemImages = function ()
		{
			// For each item in the view.
			$(".itemCardShadowDiv").each((index) =>
			{
				// Current item to deal with.
				const current = $(".itemCardShadowDiv")[index];

				// The element identifier is the item identifier.
				let identifier = $(":first-child", current).attr("id").substring(1);

				// Retrieve the images for that item.
				$.get(this.imageHandlerPath + "?identifier=" + identifier)
					.done((list) =>
					{
						// For each item images.
						list.forEach((imgPath, index, list) =>
						{
							// Create an img element.
							let image = $("<img>", {src: imgPath, class: 'itemCardImage'});

							// Put it at the end of his parent.
							$(".itemCardImageDiv", current).append(image);

							// If there are more than one image, a handler need to be added for animation.
							if (list.length > 1) image.on("load", function ()
							{
								// Attach a handler to the image.
								this.on("animationend", () =>
								{
									// Image parent.
									let parent = this.parent();

									// Previous image sibling.
									let prev = this.prev();

									// Remove the image form the DOM. The image is invisible now, because the animation reach his end.
									this.detach();

									// Remove the animation from the image which will be placed under the other of the item.
									this.removeClass("itemCardImageFront");

									// Now, put this image back to the others.
									parent.prepend(this);

									// the new front image receive now the animation.
									prev.addClass("itemCardImageFront");
								});
							}.bind(image));

							// Animation is only applied to the last image when there is more than one.
							if ((index) && (index === list.length - 1)) image.addClass("itemCardImageFront");
						});
					});
			});
		};
	}
)(window.welcome);
