var resourceUrl=$(".form-resource").attr("data-path");
var resourcePathNode;
$(document).ready(function(){


/*
$.ajax({
    url: 'aem-demo/components/content/formdata',
    type: 'GET',
    success : function(){
    console.log("Servlet called ");
    },
    error : function(){
                console.log("something is wrong");
      }
});
*/
$('#submit').click(function() {

      var firstName= $('#fn').val() ;
      var lastName= $('#ln').val() ;
      var age= $('#ag').val() ;

console.log(resourceUrl);

$.ajax({
    url: resourceUrl+".formservlet"+".json",
    type: 'POST',
    //contentType : 'application/json',
    data: {
           firstName : firstName,
           lastName : lastName,
           age  : age,
           resourcePath : resourcePathNode
    },
    success : function(response){
    console.log(response);

    },
    error : function(){
    //console.log("POST method is not calling");
      }
   });




      });
getdetails();
      });

function getdetails()
{

var resourceUrl=$(".form-resource").attr("data-path");
    $.ajax({
           url: resourceUrl+".formservlet"+".json",
           type: 'GET',
           success : function(response){
           $.each(response, function(data){
               $('#fielddata tbody').append(
               "<tr><td>"+response[data].first_name+
               "</td><td>"+response[data].last_name+
               "</td><td>"+response[data].age+
               /*</td><td>"+response[data].path+*/
               "</td><td>"+ "</td><td><button class='edit' node-path='"+response[data].path+"'>Edit</button></td><td><button class='delete' node-path='"+response[data].path+"'>Delete</button></td></tr>"
               );

          });
           },
           error : function(){
           console.log("GET method is not calling");
             }
          });

}



$(document).on('click', '.delete', function(event) {
   var path = $(event.currentTarget).attr("node-path");
   $.ajax({
        url: resourceUrl+".deleteform"+".json",
        type : 'POST',
        data :
        {
         nodePath : path
        },
        success: function(response) {
           console.log("Your Data has been deleted");
            //getdetails();
        },
        error: function() {
            console.log("failed to call servlet.");
        }
   });

});


$(document).on('click', '.edit', function(event) {
   var path = $(event.currentTarget).attr("node-path");
   $.ajax({
        url: resourceUrl+".editform"+".json",
        type : 'POST',
        data :
        {
         nodePath : path
        },

        success: function(response) {
           console.log("data fetched successfully:");
           //getdetails();
          // console.log(response);
            var firstName=$("#fn").val(response.first_name);
            var lastName=$("#ln").val(response.last_name);
            var age=$("#ag").val(response.age);
            resourcePathNode=path;

        },
        error: function() {
            console.log("failed to call servlet.");
        }
   });

});





