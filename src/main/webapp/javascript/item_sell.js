window.ItemSell = {};

(function (object)
{
	/**
	 * Initialisation stuff for the item form.
	 */

	object.initialize = function (path)
	{
		this.imageHandlerPath = path;

		let inputClickToAddPicture = $('#inputClickToAddPicture');

		this.picturesDivChangeHandler();

		$(window).on('load', () => this.loadItemImagesHandler());

		$(window).on('resize', () => this.picturesDivChangeHandler());

		$('#clickToAddPicture').on('click', () => inputClickToAddPicture.click());

		inputClickToAddPicture.on('click', event => event.stopPropagation());

		inputClickToAddPicture.on('change', event => this.clickToAddPicturesHandler(event));
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
	 * Handle adding image to an item.
	 *
	 * This function is called each time the user select an image to add to an item. Once selected, the image is sent to
	 * the server via AJAX. After receiving the response from the server confirming the success of the upload, the image
	 * is loaded and display in the form.
	 *
	 * @param event Event occured on the input HTML element.
	 */

	object.clickToAddPicturesHandler = function (event)
	{
		// Retrieve the files selected by the user.
		const files = event.target.files;

		// For each file
		Array.from(files).forEach(file =>
		{
			// The file need to be an image.
			if (!file.type.startsWith("image/")) return;

			// Create an HTML element for the image
			const img = $("<img>", {class: 'picture'});

			// Add the newly created HTML element to the DOM.
			$("#picturesDiv").append(img);

			// Create a formData object.
			const formData = new FormData();

			// And put the file in it to send it to the server.
			formData.append("file", file);

			$.ajax(
				{
					url: this.imageHandlerPath,
					data: formData,
					processData: false,
					contentType: false,
					type: "POST",
					enctype: "multipart/form-data",
					success: () => this.loadImageFromDisk(img, file),
					error: () => img.remove()
				}
			);
		});
	}

	/**
	 * Read an image from a file.
	 *
	 * This function read an image from the file described by the file attribute of the passed image and put it in that
	 * image.
	 *
	 * @param image Html image element in which put the image.
	 * @param file File to load.
	 */

	object.loadImageFromDisk = function (image, file)
	{
		const reader = new FileReader();

		// Once the file is loaded, put the ref image in the HTML img element.
		reader.onload = (event) =>
		{
			image.attr("src", event.target.result);
		};

		// Read the file.
		reader.readAsDataURL(file);
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

})(window.ItemSell);
