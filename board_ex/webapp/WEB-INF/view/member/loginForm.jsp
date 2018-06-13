<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>
<layoutTag:layout>

	<!DOCTYPE html>
	<html>
<head>

</head>
<body>
	<div class="sub_wrap">
		<!-- 서브 내용 -->
		<div class="sub_content">
			<div class="login_box">
				<input type="text" placeholder="아이디" id="memberId" name="memberId"
					class="input" /> <input type="password" placeholder="비밀번호"
					id="memberPw" name="memberPw" class="input" />
				<button class="btn_submit" type="button"
					onclick="memberLoginAjax(); return false;">로그인</button>

				<div class="join">
					<ul>
						<li><a href="/join.do">회원가입</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!--메인끝 -->
</body>
	</html>

</layoutTag:layout>