window.welcome = {};

(function (object)
	{
		/**
		 * Initialisation stuff for the search form.
		 */

		object.initialize = function (path)
		{
			this.imageHandlerPath = path;

			$("#purchases,#sales").on("click", () => this.radioButtonsHandler());

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


		object.loadItemImages = function ()
		{
			$(".itemCardShadowDiv").each((index) =>
			{
				const current = $(".itemCardShadowDiv")[index];

				let identifier = $(":first-child", current).attr("id").substring(1);

				$.get(this.imageHandlerPath + "?identifier=" + identifier)
					.done(function (list)
					{
						list.forEach((imgPath) =>
						{
							$(".itemCardImageDiv", this).append($("<img>", {src: imgPath, class: 'itemCardImage'}));
							console.log($(this));
						})

					}.bind(current));
			});
		}
	}
)(window.welcome);
