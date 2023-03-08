// 로그인에서 아이디/비밀번호 찾기 클릭 시 페이지 이동

$(document).ready(function(){

    $('#per').click(function(){
        $('#com_find_body').hide();
        $('#per_find_body_pw').hide();
        $('#per_find_body_id').show();
    });

    $('#com').click(function(){
        $('#per_find_body_id').hide();
        $('#per_find_body_pw').hide();
        $('#com_find_body').show();
    });

    
    $("input[name='ck']").change(function(){
        if($("input[name='ck']:checked").val() =='비밀번호찾기'){
            document.getElementById('per_find_body_pw').style.display = 'block';
            document.getElementById('per_find_body_id').style.display = 'none';
            $("input[name='ck1'][value='비밀번호찾기']").prop("checked",true);
        }
    });

    $("input[name='ck1']").change(function(){
        if($("input[name='ck1']:checked").val() =='아이디찾기'){
            document.getElementById('per_find_body_id').style.display = 'block';
            document.getElementById('per_find_body_pw').style.display = 'none';
            $("input[name='ck'][value='아이디찾기']").prop("checked",true);
        }
    });

});

//일반 회원 아아디 찾기
$("#find-id-btn").click(function(){
	var email = $("#email").val();
	var name = $("#name").val();
	var sMsg = $("#sMsg")
	var ㄷMsg = $("#eMsg")
	$.ajax({
            type: "post",
			url: "/mail/findid",
			data : { "email" : email },
			success : function(result){
			console.log(result);
				showSuccMsg(sMsg,"입력하신 이메일에서 아이디를 확인해 주세요");
			},
			error : function(){
				showErrorMsg(eMsg,"이름 또는 이메일을 다시 확인해주세요.");
			}
		});
});