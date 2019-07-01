
var url1=$(".form-resource").attr("data-path");
var existPath;
$(document).ready(function(){
var rurl=$(".card-resource").attr("data-path");
$.ajax({
    url : rurl + '.json',
    type : "GET",
    success :function(response){
        console.log("Resource based servlet for Card component");
    }

});
formData();
/*$("#submit").click(function(){
var Fname=$("#first_name").val();
var Lname=$("#last_name").val();
var Age=$("#age").val();

$.ajax({
    url : url1 +'.createNode'+'.json',
    type : "POST",
    data : {
            firstname : Fname,
            lastname : Lname,
            age : Age
    },
    success :function(response){
        console.log("Resource based servlet for Form Component..."+response);

    }
});

});*/
$("#form").validate(
{
         rules :
         {

            firstname :
            {
                required: true,
               minlength : 2,
            },

            lastname :
            {
                required : true,
            },
            age :
             {
                required : true,
             }
        },
        messages:
        {
            firstname :
            {
                required : "Please Enter your first name",
                minlength : "At least 2 Characters required"
            },
            lastname :
            {
                required : "Please Enter your last name"

            },
            age :
            {
               required: "Please Enter your age"
            }
        },

        submitHandler : function()
        {
            var Fname=$("#first_name").val();
            var Lname=$("#last_name").val();
            var Age=$("#age").val();

            $.ajax({
                url : url1 +'.form-servlet'+'.json',
                type : "POST",
                data :
                {
                        firstname : Fname,
                        lastname : Lname,
                        age : Age,
                        nPath : existPath


                },
                success :function(response)
                {
                        existPath="";
                         console.log("Resource based servlet for Form Component..."+response);
                         formData();

                }
            });

        }

});

});


function formData()
{

    $.ajax(
    {
        url : url1 +'.form-servlet' + '.json',
        type : 'GET',
        success:function(response)
        {
        console.log(+response);
        $('#table tbody').empty(); //used to prevent the temporary addition of table when clicked on delete.
          $.each(response, function(i){
                      $('#table tbody').append("<tr><td>"+response[i].firstname+"</td><td>"+response[i].lastname+
                      "</td><td>"+response[i].age+"</td><td><button class='edit' data-nodePath='"+response[i].nodePath+"'>Edit</button></td>"+
                      "<td><button class='delete' data-nodePath='"+response[i].nodePath+"'>Delete</button></td></tr>");
         });
        },
        error:function()
        {
            console.log("error");
        }
    });
}


$(document).on('click', '.delete', function(event){
    var nPath = $(event.currentTarget).attr("data-nodePath");
    $.ajax({
        url : url1 + ".deleteNode" + ".json",
        type :  "POST",
        data :
            {
                nodePath : nPath
            },
        success : function()
        {
            formData();
        },
        error : function()
        {
            alert("Error deleting the node");
        }
    });

});

$(document).on('click', '.edit', function(event){
    var nPath = $(event.currentTarget).attr("data-nodePath");
    $.ajax({
        url : url1 + ".editNode" + ".json",
        type :  "POST",
        data :
            {
                nodePath : nPath

            },
        success : function(response)
        {
            $("#first_name").val(response.firstname);
            $("#last_name").val(response.lastname);
            $("#age").val(response.age);
            existPath = nPath;
            formData();
        },
        error : function(error)
        {
            alert("Error deleting the node");
        }
    });

});