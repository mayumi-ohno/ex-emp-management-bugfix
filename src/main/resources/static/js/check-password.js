$(function(){
	
	$("#confirmationPassword").on("keyup", function(){
		var hostUrl = "http://localhost:8080/pass-check-api"
		var password = $("#password").val();
		var confirmationPassword = $("#confirmationPassword").val();

		$.ajax({
			url: hostUrl,
			type: "POST",
			dataType: "json",
			data:{
				password: password ,
				confirmationPassword: confirmationPassword
			},
			async: true
		}).done(function(data){
			console.log(data);
			$("#duplicateMessage").text(data.duplicateMessage);
		})
	});
});