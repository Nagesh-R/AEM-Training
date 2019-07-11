$(document).on('click','.loadmore',function(){
    var articleComponentPath = $(this).closest('.article-component').attr("data-path");
    var currentLoadMoreButton = $(this);
    var articleComponent = $(this).closest('.article-component');
    var articleChild = articleComponent.find('.article-child');
    var count = Number(articleComponent.attr('data-count'));
    var buttonName = articleComponent.find('.buttonValue').attr('data-button-text');
    $.ajax({
        url: articleComponentPath+".article"+"."+count+".json",
        type:'GET',
        success : function(response){
            articleComponent.attr('data-count',count+3);
            $.each(response.data,function(key){
            articleChild.append("<div class='col-lg-4'>"+"<div class='card'>" +
                "<img class='card-img-top' src=" + response.data[key].image +
                "><div class='card-body'>" + "<h4 class='card'>" + response.data[key].title +
                "</h4>" + "<p class='card-text'>" + response.data[key].description + "</p>" + "<a href=" +
                response.data[key].path + " class='btn btn-primary'>" + buttonName + "</a></div></div></div>");

            });
            if (response.resultShowMore != true) {
              currentLoadMoreButton.hide();
            }
        },
        error : function(){
            console.log("Error in article component");
        }
    });
});
