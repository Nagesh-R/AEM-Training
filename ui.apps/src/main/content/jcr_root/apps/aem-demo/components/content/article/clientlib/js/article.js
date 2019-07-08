var articleUrl = $(".article-container").attr("data-path");
var cta = $(".article-container").attr("data-button-text");
var offset = 3;
$(document).on('click', ".load-more", function(event) {
    $.ajax({
        url: articleUrl + '.article.' + offset + '.json',
        type: 'GET',
        success: function(response) {
            console.log(response);
            var value = response.data;
            var x;
            for (x in value) {
                $(".article-row").append("<div class='col-lg-4'>" +
                    "<div class='card-body'>" + "<img class=card-img-top src='" +
                    value[x].articleImage + "'>" +
                    "<div class='card-body'>" + "<h4 class='card-title'>" + value[x].title + "</h4>" +
                    "<p class='card-text'>" + value[x].description + "</p>" + "<a href='" +
                    value[x].articlePath + "class='btn btn-primary'>" + cta +
                    "</a></div></div></div>");
            }
            if (response.boolean != false) {
                $(".load-more").hide();
            }
            offset = offset + 3;
        },
        error: function() {
            console.log("error of article ajax..");
        }
    });
});