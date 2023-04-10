//비밀번호 유효성 검사
var submitPw = false;

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
$("#password").focus(function(){
   $("#fieldErrorPw").hide()
});

$("#modify").click(function submitCheck(){
   var eMsg = $("#passwordMsg");
   if(submitPw == false){
   showErrorMsg(eMsg,"필수 정보입니다.");
   alert("비밀번호를 확인해주세요");
   return false;
   }
   alert("정보가 수정되었습니다");
   return true;
   });   