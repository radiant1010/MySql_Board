<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>
<layoutTag:layout>

	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1">


<script type="text/javascript">
	function search() {

		var searchText = $("#searchText").val();

		if (searchText == "") {
			alert("검색어를 입력하세요.");
			$("#searchText").focus();
			return;
		}

		//검색실행
		showList(searchText);
	};

	//웹용 아작스
	function showList(searchTextVal) {
		$.ajax({
			type : "post",
			url : "/board/boardListProc.do",
			data : {
				pageNo : "${param.pageNo}",
				'searchText' : searchTextVal
			},
			dataType : "json",
			success : function(data) {
				console.log(data);

				if (data.result == "ok") {

					$("#boardBody").empty();

					//리스트 출력
					$.each(data.boardBean, function(i, mBean) {
						var str = "";
						str += "<tr>";
						str += "<td>" + mBean.boardNo + "</td>";
						str += "<td><a href='/board/boardView.do?boardNo="
								+ mBean.boardNo + "&'>" + mBean.boardTitle
								+ "</a></td>";
						str += "<td>" + mBean.memberName + "</td>";
						str += "<td>" + mBean.boardDate + "</td>";
						str += "<td>" + mBean.boardCount + "</td>";
						str += "</tr>";

						$("#boardBody").append(str);
					});

				} else {
					alert(data.resultMsg);
				}

			},
			error : function(xhr, status, error) {
				console.log(xhr);
				alert("error\nxhr : " + xhr + ", status : " + status
						+ ", error : " + error);
			}
		});
	};

	//////////////////////////////////////////////
	$(function() {

		showList('${param.searchText}');

	});//end ready
</script>
</head>

<body>
	<section>
	<div class="container">
	
	<br>
    <div class="input-group">
    	<div class="form-group">
            <input type="text" class="form-control" alue="${searchText}" id="searchText">
        </div>
        <span class="input-group-btn">
            <button class="btn btn-default" type="button" onClick="search(); return false;">검색하기</button>
        </span>
    </div>
    <br>
	
	
		<div class="table">
		
			<table class="table table-hover">
				<colgroup>
					<col width="10%" class="col1">
					<col width="30%" class="col2">
					<col width="10%" class="col3">
					<col width="15%" class="col4">
					<col width="10%" class="col5">
				</colgroup>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>이름</th>
					<th>날짜</th>
					<th>조회순</th>
				</tr>

				<tbody id="boardBody">
				</tbody>

			</table>

			<ul class="container">
				<!-- 로그인이 안되어 있을 때 화면 -->
				<c:if test="${empty memberBean}">
					<div class="text-left" style="display: none">
						<button class="btn btn-primary"
							onclick="location.href='boardWrite.do' ">글쓰기</button>
					</div>
				</c:if>

				<!-- 로그인이 되어 있을 때 화면 -->
				<c:if test="${!empty memberBean}">
					<div class="text-left">
						<button class="btn btn-primary"
							onclick="location.href='boardWrite.do' ">글쓰기</button>
					</div>
				</c:if>
			</ul>
		</div>

		<!-- 페이징 -->
		<div style="text-align: center;">
			<ul class="pagination">
				<c:if test="${pagingBean.groupNo > 1}">
					<li><a
						href="/board/boardList.do?pageNo=${pagingBean.pageStartNo-1}">&lt;</a></span></li>
				</c:if>
				<c:forEach var="i" begin="${pagingBean.pageStartNo}"
					end="${pagingBean.pageEndNo}">
					<c:choose>
						<c:when test="${pagingBean.pageNo != i}">
							<li><a href="/board/boardList.do?pageNo=${i}">${i}</a></li>
						</c:when>
						<c:otherwise>
							<li class="active"><a>${i}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${pagingBean.groupNo < pagingBean.totalGroupCount}">
					<li><a
						href="/board/boardList.do?pageNo=${pagingBean.pageEndNo+1}">&gt;</a></li>
				</c:if>
			</ul>
		</div>
		</div>
		</div>
	</section>

	<!--메인끝 -->
</body>
	</html>
</layoutTag:layout>