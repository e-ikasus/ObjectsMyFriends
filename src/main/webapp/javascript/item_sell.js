window.ItemSell = {};

(function (object)
{
	object.initialize = function ()
	{
		$('#clickToAddPicture').on('click', function ()
		{
			$('#inputClickToAddPicture').click();
		});

		$('#inputClickToAddPicture').on('click', function (event)
		{
			event.stopPropagation();
		});

		$('#inputClickToAddPicture').on('change', function (event)
		{
			// Retrieve the files selected by the user.
			const files = this.files;

			// For each file
			Array.from(files).forEach(file =>
			{
				// The file need to be an image.
				if (!file.type.startsWith("image/")) return;

				// Create an HTML element for the image
				const img = $("<img>", {class: 'picture'});

				// Store the file information in the element.
				img.attr("file", file);

				// Add the newly created HTML element to the DOM.
				$("#picturesDiv").append(img);

				const formData = new FormData();

				formData.append("file", file);

				let dataToSend = {data: formData};

				$.ajax(
					{
						url: "http://localhost:8081/ObjectsMyFriends/image_handler",
						data: formData,
						processData: false,
						contentType: false,
						type: "POST",
						enctype: "multipart/form-data",
						success: () =>
						{
							const reader = new FileReader();

							// Once the file is loaded, put the ref image in the HTML img element.
							reader.onload = (event) => {img.attr("src", event.target.result);};

							// Read the file.
							reader.readAsDataURL(file);
						}
					}
				);
			});
		});
	};
})(window.ItemSell);
