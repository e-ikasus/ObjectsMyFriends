window.ItemBid = {};

(function (object)
{
	/**
	 * Initialisation stuff for the item form.
	 */

	object.initialize = function (path)
	{
		this.imageHandlerPath = path;

		this.picturesDivChangeHandler();

		$(window).on('load', () => this.loadItemImagesHandler());

		$(window).on('resize', () => this.picturesDivChangeHandler());
	}

	/**
	 * Resize the left part containing images.
	 *
	 * The left part contains the images chosen by the user. This part is placed vertically and at the left for big
	 * screens, but is positioned at the top horizontally for small screens. This function should be called whenever
	 * the size of the windows change.
	 */

	object.picturesDivChangeHandler = function ()
	{
		// Left part containing images.
		let leftPart = $('#leftPart');

		// Right part containing item information.
		let rightPart = $('#rightPart');

		// The height need to be adjusted according to the right part for big screens. For small screen, th width need to
		// be fixed. This allows the overflow css property to work.
		if (leftPart.width() === 200) leftPart.height(rightPart.height());
		else if (leftPart.width() === 400) leftPart.height(128);
	}

	/**
	 * Retrieve images from the server.
	 *
	 * This function retrieve the images from the item currently modified and the images the user uploaded to the server
	 * during the update/creation process of his item. This request is done asynchronously.
	 */

	object.loadItemImagesHandler = function ()
	{
		$.get(this.imageHandlerPath, result => this.loadItemImages(result));
	}

	/**
	 * Add images to the form.
	 *
	 * This function add image HTML elements to the form according to the supplied list in parameter.
	 *
	 * @param imageList List of images to add.
	 */

	object.loadItemImages = function (imageList)
	{
		imageList.forEach(image =>
		{
			$("#picturesDiv").append($("<img>", {src: image, class: 'picture'}));
		});
	}

})(window.ItemBid);
