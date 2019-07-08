var resourceUrl2 = $(".form-resource").attr("data-path");
var pathNode ;

$(document).ready(function() {
    jQuery.validator.addMethod("lettersOnly", function(value, element) {
        return this.optional(element) || /^[a-zA-Z]+$/.test(value);
    }, 'Please enter a letters only.');

    var formValidator = $("#form").validate({
        rules: {
            fname: {
                required: true,
                minlength: 2,
                lettersOnly: true
            },
            lname: {
                required: true,
                lettersOnly: true
            },
            age: {
                required: true,
                digits: true,
            }
        },
        messages: {
            fname: {
                required: "first name is required",
                minlength: "At least 2 characters required!"
            },
            lname: {
                required: "last name is required"
            },
            age: {
                required: "please enter your Age"
            }

        },
        submitHandler: function(form) {
            console.log(form);
            var Fname = $('#firstname').val();//took value from client side
            var Lname = $('#lastname').val();
            var Age = $('#age').val();
            var Gender=$('#gender').val();
            console.log(Gender);
            $.ajax({
                url: resourceUrl2 + '.createFormNode.json',
                type: 'POST',
                data: {
                    fname: Fname,  // passing value to servlet
                    lname: Lname,
                    age: Age,
                    gender : Gender,
                    resourcePath : pathNode
                },
                success: function(response) {  //parameter is responce to client-side.
                   if(response.status === "error")
                   {
                      alert(response.message);
                   }
                   else if (response.status === "edit")
                   {
                      alert(response.editmessage)
                   }
                   else
                   {

                      alert("the values has been sucessfully added");
                   }
                    getFormData();
                    pathNode =" ";
                },
                error: function() {
                    console.log("error..");
                }

            });
        }
    });
    getFormData();
});

function getFormData() {
    $.ajax({
        url: resourceUrl2 + '.createFormNode.json',
        type: 'GET',
        success: function(response) {
            $('#table tbody').empty();
            $.each(response, function(a) {
                $('#table tbody').append("<tr><td>" + response[a].FirstName + "</td><td>" + response[a].LastName +
                    "</td><td>" + response[a].Age + "</td><td>" + response[a].Gender + "</td><td>" + "</td><td><button class='edit' data-node-path='"+response[a].resourcePath+"'>Edit</button></td><td><button class='delete' data-node-path='"+response[a].resourcePath+"'>Delete</button></td></tr>");
             });
        },
        error: function() {
            console.log("error");

        }
    });
}

$(document).on('click', '.delete', function(event) {
   var path = $(event.currentTarget).attr("data-node-path");
   $.ajax({
        url: resourceUrl2+ '.deleteFormNode.json',
        type : 'POST',
        data :
        {
         resourcePath: path
        },
        success: function(response) {
           alert("Your Data has been deleted");
            getFormData();
        },
        error: function() {
            console.log("error..");
        }
   });

});

$(document).on('click', '.edit', function(event) {
   var path = $(event.currentTarget).attr("data-node-path");
   $.ajax({
        url: resourceUrl2+ '.editNode.json',
        type : 'POST',
        data :
        {
         resourcePath : path
        },
        success: function(response) {
            var fname=$("#firstname").val(response.FirstName);
            var lname=$("#lastname").val(response.LastName);
            var age=$("#age").val(response.Age);
            var age=$("#gender").val(response.Gender);
            pathNode = path;
            console.log(response);

        },
        error: function() {
            console.log("error..");
        }
   });

});

$("#show").click(function() {
     $("#table").toggle(1500);
  });

$("#hide").click(function() {
     $("#slingTable").toggle(1500);
  });

