$(document).ready(function() {
    var resourceUrl = $(".card-resource").attr("data-path");
    $.ajax({
        url: resourceUrl + '.json',
        type: "GET",
        success: function(response) {
            console.log(response);
            console.log("ajax is running for card component")
        },
        error: function() {
            console.log("error");
        }

    });
});