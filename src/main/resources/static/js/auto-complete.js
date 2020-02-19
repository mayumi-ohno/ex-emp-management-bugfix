
$(function(){

	// DBから従業員名一覧リストを取得
	var hostUrl = 'http://localhost:8080/employee/auto-complete-api';
	var list;
	$.ajax({
		url : hostUrl,
		type : 'POST',
		dataType : 'json',
		async: true // 非同期で処理を行う
	}).done(function(data) {
			$("#aPartOfName").autocomplete({
				source: data
			});
		});
	
	
});

