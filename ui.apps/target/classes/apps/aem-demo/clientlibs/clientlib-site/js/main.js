var resourceUrl = $(".form-resource").attr("data-path");
var resourcePathNode;
//var count = 3;

var loadButtonPath = $(".showmoredata").attr("btn-path");
var clickButtonPath = $(".btn-primary").attr("btnPath");
$(document).ready(function () {
	$('#submit').click(function () {
		var firstName = $('#fn').val();
		var lastName = $('#ln').val();
		var age = $('#ag').val();
		var gender = $('#gender').val();
		$.ajax({
			url: resourceUrl + ".formservlet" + ".json",
			type: 'POST',
			data: {
				firstName: firstName,
				lastName: lastName,
				age: age,
				gender: gender,
				resourcePath: resourcePathNode
			},
			success: function (response) {
				resourcePathNode = '';
				getdetails();
				if (response.status === "error") {
					alert(response.message);
				} else if (response.status === "success") {
					alert(response.message);
				} else if (response.status === "edit") {
					alert(response.message);
				}
			},
			error: function () {
				resourcePathNode = '';
			}
		});
	});
	getdetails();
});

function getdetails() {
	var resourceUrl = $(".form-resource").attr("data-path");
	$.ajax({
		url: resourceUrl + ".formservlet" + ".json",
		type: 'GET',
		success: function (response) {
			$('#fielddata tbody').empty();
			$.each(response, function (data) {
				$('#fielddata tbody').append("<tr><td>"
				 + response[data].first_name + "</td><td>"
				 + response[data].last_name + "</td><td>"
				 + response[data].age + "</td><td>"
				 + response[data].gender +
				 "</td><td>" + "</td><td><button class='edit' node-path='"
				 + response[data].path
				 + "'>Edit</button></td><td><button class='delete' node-path='"
				 + response[data].path
				 + "'>Delete</button></td></tr>");
			});
		},
		error: function () {
			console.log("GET method is not calling");
		}
	});
}
$(document).on('click', '.delete', function (event) {
	var path = $(event.currentTarget).attr("node-path");
	$.ajax({
		url: resourceUrl + ".deleteform" + ".json",
		type: 'POST',
		data: {
			nodePath: path
		},
		success: function (response) {
			alert("data has been deleted");
			getdetails();
		},
		error: function () {
			console.log("failed to call servlet.");
		}
	});
});
$(document).on('click', '.edit', function (event) {
	var path = $(event.currentTarget).attr("node-path");
	$.ajax({
		url: resourceUrl + ".editform" + ".json",
		type: 'POST',
		data: {
			nodePath: path
		},
		success: function (response) {
			console.log("data fetched successfully:");
			var firstName = $("#fn").val(response.first_name);
			var lastName = $("#ln").val(response.last_name);
			var age = $("#ag").val(response.age);
			resourcePathNode = path;
			getdetails();
		},
		error: function () {
			console.log("failed to call servlet.");
		}
	});
});
//var x = document.getElementById('button');
