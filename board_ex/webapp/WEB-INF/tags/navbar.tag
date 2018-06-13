<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/board/boardList.do">게시판 연습</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="active"><a href="/board/boardList.do">게시판 1 <span class="sr-only">(current)</span></a></li>
        <li><a href="/member/loginForm.do">게시판 2</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Dropdown <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="#">Action</a></li>
            <li><a href="#">Another action</a></li>
            <li><a href="#">Something else here</a></li>
            <li class="divider"></li>
            <li><a href="#">Separated link</a></li>
            <li class="divider"></li>
            <li><a href="#">One more separated link</a></li>
          </ul>
        </li>
      </ul>

      <ul class="nav navbar-nav navbar-right">
        <!-- 로그인이 안되어 있을 때 화면 -->
				<c:if test="${empty memberBean}">
					<li><a href="/member/loginForm.do">로그인</a></li>
					<li><a href="/join.do">회원가입</a></li>
				</c:if>

				<!-- 로그인이 되어 있을 때 화면 -->
				<c:if test="${!empty memberBean}">
					<!-- 관리자 일때 화면 -->
					<c:if test="${!empty sessionScope.memberBean.memberAdmin}">
						<li><a href="/member/memberList.do">회원관리</a></li>
						<li><a href="/festival/master_managerFestival.do">축제관리</a></li>
					</c:if>
					<li><a
						href="/member/myList.do?memberId=${sessionScope.memberBean.memberId}">마이페이지</a></li>
					<li><a href="#" onclick="logoutAjax(); return false;">로그아웃</a></li>
				</c:if>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
