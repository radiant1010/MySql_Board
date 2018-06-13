//엔터키 이벤트
$(function() {
	var userInputId = getCookie("userInputId");
	$("#memberId").keydown(function(key) {
		if(key.keyCode == 13) {
			memberLoginAjax();
		}
	});
	$("#memberPw").keydown(function(key) {
		if(key.keyCode == 13) {
			memberLoginAjax();
		}
	});
});

//쿠키
function setCookie(cookieName, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate()+exdays);
	var cookieValue = escape(value) + ((exdays==null) ? "": "; exprires=" + exdate.toGMTString());
	document.cookie = cookieName + "=" + cookieValue;
}
function deleteCookie(cookieName) {
	var expireDate = new Date();
	expireDate.setDate(expireDate.getDate() - 1);
	document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}
function getCookie(cookieName) {
	cookieName = cookieName + "=";
	var cookieData = document.cookie;
	var start = cookieData.indexOf(cookieName);
	var cookieValue = "";
	if(start != -1) {
		start += cookieName.length;
		var end = cookieData.indexOf(";", start);
		if(end == -1)end = cookieData.length;
		cookieValue = cookieData.substring(start, end);
	}
	return unescape(cookieValue);
}
//로그인
function memberLoginAjax(){
	var userInputId = $("#memberId").val();
	$("#memberId").empty();
	$.ajax({
		url : "/member/loginAjax.do",
		method : "POST",
		data : {
			memberId : $("#memberId").val(),
			memberPw : $("#memberPw").val(),
		},
		dataType : "json",

		success : function(data) {
			if (data.result == "ok") {
				setCookie("userInputId", userInputId, 30);//30일 동안 쿠키 저장
				try {
					// 값 전달
					var memId = $("#memberId").val();
				} catch(e) {
					console.log(e);
				}
				location.href="/board/boardList.do"
			} else {
				alert(data.resultMsg);
			}
		}
	});
}
//로그아웃
function logoutAjax() {
	$.ajax({
		url : "/member/logoutAjax.do",
		method : "POST",
		data : {},
		dataType : "json",

		success : function(data) {
			if (data.result == "ok") {
				location.href = "/board/boardList.do"
			} else {
				alert(data.resultMsg);
			}
		}
	});
}

//navbar 클릭 이벤트
$(function(){
    $('.nav a').filter(function(){
        return this.href==location.href}).parent().addClass('active').siblings().removeClass('active');

    $('.nav a').click(function(){
        $(this).parent().addClass('active').siblings().removeClass('active')    
        });
    });
