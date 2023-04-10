//회원가입 유효성 검사
var submitId = false;
var submitPw = false;
var submitEmail = false;

//일반회원 아이디
$("#mid").blur(function(){
		checkId();	
	});
	
    function checkId(){
		
		var mid = $("#mid").val();
		var eMsg = $("#midMsg");
		
		if(mid==""){
			showErrorMsg(eMsg,"필수 정보입니다.");
			return false;
		}
		
		var isID = /^[a-z0-9][a-z0-9_\-]{4,19}$/;
        if (!isID.test(mid)) {
            showErrorMsg(eMsg,"5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
            return false;
        } else {
            eMsg.hide();
        }
        return true;
    }
    


   
$("#name").blur(function(){
		checkName();	
	});
	
   function checkName(){
		var name1 = $("#name");
		var name = $("#name").val();
		var eMsg = $("#nameMsg");
		
		if(name==""){
			showErrorMsg(eMsg,"필수 정보입니다.");
			return false;
		}
		name1.focusout(function(){
			if(name!=""){
				eMsg.hide();
			} else {
				showErrorMsg(eMsg,"필수 정보입니다.");
			}
		});   
    }
    
$("#password").blur(function(){
		checkPassword();	
	});
	
    function checkPassword(){
		
		var password = $("#password").val();
		var eMsg = $("#passwordMsg");
		
		if(password==""){
			showErrorMsg(eMsg,"필수 정보입니다.");
			return false;
		}   
		var isPw = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/;
        if (!isPw.test(password)) {
            showErrorMsg(eMsg,"8~16자 영문 대 소문자, 숫자를 사용하세요.");
            return false;
        } else {
            eMsg.hide();
        }
        return true; 
    }

$("#password2").blur(function(){
		checkPassword2();	
	});   
	
	 function checkPassword2(){
	 	var password = $("#password").val();
		var password2 = $("#password2").val();
		var eMsg1 = $("#passwordMsg1");
		var eMsg2 = $("#passwordMsg2");
		
		if(password2==""){
			showErrorMsg(eMsg1,"필수 정보입니다.");
			return false;
		}   
        if (password!=password2) {
            showErrorMsg(eMsg1,"비밀번호가 일치하지 않습니다.");
            eMsg2.hide();
            return false;
        } else {
            showSuccMsg(eMsg2,"비밀번호가 일치합니다.");
            eMsg1.hide();
            submitPw = true;
            return false;
        }
        return true; 
    } 
    
$("#email").blur(function(){
		checkEmail();	
	});
	
    function checkEmail(){
		
		var email = $("#email").val();
		var eMsg = $("#emailMsg");
		if(email==""){
			showErrorMsg(eMsg,"필수 정보입니다.");
			return false;
		}   
		var isEmail = /^[a-zA-Z0-9+-\_.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
        if (!isEmail.test(email)) {
            showErrorMsg(eMsg,"이메일 주소를 다시 확인해주세요.");
            return false;
        } else {
            eMsg.hide();
        }
        return true; 
    }

$("#emailsend").click(function(){
	var email = $("#email").val();
	var eMsg = $("#emailMsg");
	var eMsg2 = $("#emailMsg2");
	$.ajax({
            type: "get",
			url: "/mail/sendmail",
			data : { "email" : email },
			success : function(result){
				showSuccMsg(eMsg2,"인증번호를 입력해주세요.");
			},
			error : function(){
				showErrorMsg(eMsg,"이메일 발송에 실패하였습니다. 이메일 정보를 다시 확인해주세요.");
			}
		});
});


$("#emailcheck").click(function(){
	var emailcode = $("#emailcode").val();
	var eMsg = $("#emailCheckMsg");
	var eMsg2 = $("#emailCheckMsg2");
	$.ajax({
		type : "get",
		url : "/mail/checkcode",
		data : { "emailcode" : emailcode },
		success : function(result){
			if(result.result == true) {
				showSuccMsg(eMsg2,"인증번호가 일치합니다");
				submitEmail = true;
				eMsg.hide();
			} else {
				showErrorMsg(eMsg,"인증번호가 일치하지 않습니다.");
			}
		},
		error : function(){
			showErrorMsg(eMsg,"먼저 인증번호를 발송해주세요.");
		}
	});
});


    
$("#tel").blur(function(){
		checkTel();	
	});    
	
function checkTel(){
		
		var tel = $("#tel").val();
		var eMsg = $("#telMsg");
		
		
		var isTel = /\d{3}-\d{3,4}-\d{4}/;
        if(tel==""){
        	eMsg.hide();
        } else if (!isTel.test(tel)) {
            showErrorMsg(eMsg,"전화번호를 다시 확인해주세요.");
            return false;
        } else {
            eMsg.hide();
        }
        return true; 
    }

   
const autoHyphen = (target) => {
 target.value = target.value
   .replace(/[^0-9]/g, '')
   .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
}

    
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

//유효성 검사 이후 나온 error 메시지 감추기
$("#mid").focus(function(){
	$("#fieldErrorMid").hide()
});
$("#password").focus(function(){
	$("#fieldErrorPw").hide()
});
$("#name").focus(function(){
	$("#fieldErrorName").hide()
});
$("#email").focus(function(){
	$("#fieldErrorEmail").hide()
});


//ajax를 이용한 아이디 중복검사
$(".mid_ck").click(function(){
	var eMsg = $("#midMsg");
	var eMsg2 = $("#midMsg2");
	var mid = $("#mid").val();
	if(mid==""){
	showErrorMsg(eMsg,"필수 정보입니다.");
	return false;
	}
	var isID = /^[a-z0-9][a-z0-9_\-]{4,19}$/;
        if (!isID.test(mid)) {
            showErrorMsg(eMsg,"5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
            return false;
        } else {
            eMsg.hide();
            $.ajax({
				type: "get",
				url: "/signup/exists",
				data : { "mid" : mid },
				contentType: "application/json",
					success: function(result){
						if(result.result == false){
							if (!isID.test(mid)) {
            					showErrorMsg(eMsg,"5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.");
            					return false;
       						 } else{
							showSuccMsg(eMsg2,"사용할 수 있는 아이디입니다.");
							eMsg.hide();
							submitId = true;
							}
						}
					else{
							showErrorMsg(eMsg,"사용할 수 없는 아이디입니다.");
							eMsg2.hide();
					}
					},
					error : function(){
						alert("에러발생");
					}
				});
        }
        return true;
});

	
$("#regist").click(function submitCheck(){
	var eMsg = $("#midMsg");
	if(submitId == false){
		showErrorMsg(eMsg,"아이디 중복검사를 해주세요.");
		return false;
	}
	var eMsg1 = $("#passwordMsg1");
	if(submitPw == false){
		showErrorMsg(eMsg1,"비밀번호가 일치되어야 합니다.");
		return false;
	}
	var eMsg = $("#emailCheckMsg");
	if(submitEmail ==false){
		showErrorMsg(eMsg,"이메일 인증이 되어야 합니다.");
		return false;
	}
	alert("회원가입이 완료되었습니다");
	return true;
});