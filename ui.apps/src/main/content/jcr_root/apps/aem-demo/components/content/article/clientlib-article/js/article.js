var articlePath = $(".article-component").attr("data-path");
$(document).on('click', '.showmoredata', function () {
  var currentLoadMoreButton = $(this);
  var closestValue = $(this).closest('.article-component');
  var findComponent = closestValue.find('.article-row');
  var count = Number(closestValue.attr('data-offset'));
  var buttonname = closestValue.find('.buttonValue').attr('btnPath');

	$.ajax({

		url: articlePath + ".loadServlet." + count + ".json",
		type: 'GET',
		success: function (response) {
		closestValue.attr('data-offset',count+3);
			$.each(response.data, function (key) {
				findComponent.append("<div class='col-lg-4'>"
				+ "<div class='card-body'>"
				+ "<img class='card-img-top' id='image' src="
				+ response.data[key].image + ">"
				+ "<div class='card-body'>"
				+ "<h4 class='card-title' id='title'>"
				+ response.data[key].title
				+ "</h4>" + "<p class='card-text'>"
				+ response.data[key].description
				+ "</p>" + "<a href="
				+ response.data[key].path
				+ " class='btn btn-primary'>"
				+ buttonname + "</a>")
			});
			if (response.loadMore != true) {
				currentLoadMoreButton.hide();
			}
			//count = count + 3;
			console.log(count);
			console.log("servlet called");
		},
		error: function () {
			console.log("error");
		}
	});
});