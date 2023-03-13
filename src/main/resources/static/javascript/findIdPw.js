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
//ajax post 이용시 csrf의 token이 누락되면 403에러가 발생한다.
var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

//일반 회원 아아디 찾기(HashMap으로 값 받을 때)
$("#find-id-btn").click(function(){
	var email = $("#email1").val();
	var name = $("#name").val();
	var sMsg = $("#sMsg1")
	var eMsg = $("#eMsg1")
	$.ajax({
            type: "post",
			url: "/signup/findid",
			data : { "email" : email,  "name" : name },
			beforeSend: function(xhr){
        		xhr.setRequestHeader(header, token);
    		},
			success : function(result){
				showSuccMsg(sMsg,"입력하신 이메일에서 아이디를 확인해 주세요");
				eMsg.hide();
			},
			error : function(){
				showErrorMsg(eMsg,"이름 또는 이메일을 다시 확인해주세요.");
				sMsg.hide();
			}
		});
});

//일반 회원 비밀번호 찾기(String으로 값 받을때)
$("#find-pw-btn").click(function(){
	var mid = $("#mid").val();
	var email = $("#email2").val();
	var sMsg = $("#sMsg2")
	var eMsg = $("#eMsg2")
	var sendData = "mid="+mid+"&email="+email;
	$.ajax({
            type: "post",
			url: "/signup/findpw",
			data : sendData,
			beforeSend: function(xhr){
        		xhr.setRequestHeader(header, token);
    		},
			success : function(result){
				showSuccMsg(sMsg,"입력하신 이메일에서 임시 비밀번호를 확인해 주세요");
				eMsg.hide();
			},
			error : function(){
				showErrorMsg(eMsg,"이름 또는 이메일을 다시 확인해주세요.");
				sMsg.hide();
			}
		});
});


//기업 회원 비밀번호 찾기(String으로 값 받을때)
$("#find-com-pw-btn").click(function(){
	var com = $("#com1").val();
	var email = $("#email").val();
	var sMsg = $("#sMsg")
	var eMsg = $("#eMsg")
	var sendData = "com="+com+"&email="+email;
	$.ajax({
            type: "post",
			url: "/signup/findcompw",
			data : sendData,
			beforeSend: function(xhr){
        		xhr.setRequestHeader(header, token);
    		},
			success : function(result){
				showSuccMsg(sMsg,"입력하신 이메일에서 임시 비밀번호를 확인해 주세요");
				eMsg.hide();
			},
			error : function(){
				showErrorMsg(eMsg,"이름 또는 이메일을 다시 확인해주세요.");
				sMsg.hide();
			}
		});
});


 function showErrorMsg(obj, msg) {
        obj.attr("class", "error");
        obj.html(msg);
        obj.show();
    }
    
    function showSuccMsg(obj, msg) {
        obj.attr("class", "succ");
        obj.html(msg);
        obj.show();
    }