var formurl =$(".form-component").attr("data-path");
var editPath;

$(document).ready(function(){
    console.log("main js execcuted.......");
    var url =$(".card-component").attr("data-path");

getFormData();

$("#form").validate({
  rules: {
    firstname: {
        required: true,
        number:false
    },
    dob: {
      required: true
     },
    age: {
        required: true,
        number:true
    },
    address: {
        required:true
    }
  },
  messages:{
    firstname:{
        required:"Enter your name"
    },
    dob:{
        required:"select date of birth"
    },
    age:{
        required:"Enter your age"
    },
    address:{
        required:"Enter your address"
    }
  },
  submitHandler:function(){
           var lastName = $("#lastname").val();
           var dob = $("#dob").val();
           var age =$("#age").val();
           var address = $("#address").val();
           $.ajax({
                            url: formurl+".creatingnodeservlet"+".json",
                            type: 'POST',
                            data:{
                               lastName  : lastName,
                               dob       :  dob,
                               age       :  age,
                               address   :  address,
                               path      :  editPath
                           },
                           success : function(response){
                               console.log("form servlet called: \n" + response);
                               editPath = '';
                               getFormData();
                           },
                           error : function(){
                               console.log("error during form servlet");
                               editPath = '';
                           }
                    });
  }
})


});
$(document).on('click','.edit',function(event){
    var path = $(event.currentTarget).attr("data-node-path");
    $.ajax({
            url: formurl+".updatingnodedataservlet"+".json",
            type:'POST',
            data:{
                        path : path
              },
            success:function(response){
                console.log(response);
                $('#lastname').val(response.firstname);
                $('#dob').val(response.dob);
                $('#age').val(response.age);
                $('#address').val(response.address);
                debugger;
                editPath = path;
            },
            error: function(){
                console.log("Error during the data form servlet");
            }

    });
})

function getFormData(){
$.ajax({
    url: formurl+".formservlet"+".json",
    type: 'GET',
    success : function(response){
        console.log(response);
        $('#formdata tbody').empty();
        $.each(response, function(i){
            $('#formdata tbody').append("<tr><td>"+response[i].firstname+"</td>"+
            							"<td>"+response[i].dob+"</td>"+
            							"<td>"+response[i].age+"</td>"+
            							"<td>"+response[i].address+"</td>"+
            							"<td> <button class='edit' data-node-path='"+response[i].path+"'>Edit</button></td>"+
            							"<td> <button class='delete' data-node-path='"+response[i].path+"'>Delete</button></td>");
        });
    },
        error : function(){
            console.log("error during formDataTable servlet call");
    }
 });
}

$(document).on('click', '.delete' ,function(event){
    var path = $(event.currentTarget).attr("data-node-path");

    $.ajax({
        url: formurl+".deletingnodeervlet"+".json",
        type: 'POST',
        data:{
            path : path
        },
        success : function(){
            getFormData();
            console.log("Deleted form servlet");
        },
        error : function(){
            console.log("error during  Delete servlet");
        }
    });
});








