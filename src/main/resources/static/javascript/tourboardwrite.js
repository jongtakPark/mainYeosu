$(".submit").click(function(){
		var errorMessage = [[${errorMessage}]]; 
		if(errorMessage != null){
			alert(errorMessage);
	}
	bindDomEvent();
});

function bindDomEvent(){
	$(".file").on("change", function() {
		var fileName = $(this).val().split("\\").pop(); 
		var fileExt = fileName.substring(fileName.lastIndexOf(".")+1); 
		fileExt = fileExt.toLowerCase(); 
		
		if(fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif" && fileExt != "png" && fileExt != "bmp"){
			alert("이미지 파일만 등록이 가능합니다."); 
		return;
	}
	});
}