
$(function(){

	// DBから従業員名一覧リストを取得
	var hostUrl = 'http://localhost:8080/employee/auto-complete-api';
	
	$.ajax({
		url : hostUrl,
		type : 'GET',
		dataType : 'json',
		async: true // 非同期で処理を行う
	}).done(function(data) {
			$("#aPartOfName").autocomplete({
				source: data
			});
		});
	
	
});


