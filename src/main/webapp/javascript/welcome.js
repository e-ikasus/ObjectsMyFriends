window.welcome = {};

(function(object)
{
	/**
	 * Initialisation stuff for the search form.
	 */

	object.initialize = function ()
	{
		$("#purchases,#sales").on("click", () => this.radioButtonsHandler());
	}

	/**
	 * Handles click on radio buttons.
	 *
	 * This function update the states of the HTML element according to the choice made by the user about search options.
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
}
)(window.welcome);
