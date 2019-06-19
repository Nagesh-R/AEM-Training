$(document).ready(function(){
$.ajax({

    url : 'aem-demo/components/content/card',
    type : 'GET',
    success : function() {
        alert("This is just a test ");
    }
});

});

