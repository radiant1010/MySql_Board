<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>
<layoutTag:layout>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">
       function deleteBoard() {
          $.ajax({
             type: 'POST',
             url: "/board/deleteBoardAjax.do",
                data: {
                   boardNo : "${boardList.boardNo}",
                },
                success: function(data){
                   alert(data.resultMsg);
                   if(data.result == "ok") {
                      //화면이동 처리
                      location.href="boardList.do?pageNo=1";
                   }
                }
            }); // end of ajax
       };
       
       function deleteReply(index) {
          
          if(confirm("삭제 하시겠습니까?")) {
            $.ajax({
                type: 'POST',   
                url: "/board/deleteBoardReplyAjax.do",
                   data: {
                      brNo : index
                   },
                   success: function(data){
                      alert(data.resultMsg);
                      if(data.result == "ok") {
                         //화면이동 처리
                         location.reload();
                      }
                   }
             });
            }
       }
       
       //댓글 아작스
       $(function() {
          $("#commentOk").click(function(){
             $.ajax({
                type : "post",
                url : "/board/boardReplyViewAjax.do",
                data : {
                   brContent:$("#commentText").val(),
                   boardNo : "${boardList.boardNo}"
                },
                dataType : "json",
                success : function(data){
                   console.log(data);
                   if(data.result == "ok"){
                      var str = "";
                       str += "<tr>";
                      str += "<td>" + data.BoardReplyBean.memberName + ":" + "</td>;";
                      str += "<td>" + data.BoardReplyBean.brContent + "</td>";
                      str += "</tr>";
                      $("#replyList").append(str);
                      
                      location.reload();
                   }
                },
                error : function(xhr, status, error){
                   console.log(xhr);
                   alert("로그인 후 입력하세요");
//                    alert("error\nxhr : " + xhr+ ", status"+status+", error : "+error);
                }
             }); // end ajax
          }); // end click
       }); // end ready
       
    </script>
</head>
<body>
   <!--메인 -->
   <section>
   <div class="sub_wrap">
      <!-- 서브 네비 -->
      <div class="sub_nav">

         <!-- 서브 내용 -->
         <div class="sub_content">
            <div class="write_table">
               <table>
                  <tr>
                     <th>제목</th>
                     <td colspan="3" id="boardTitle">${boardList.boardTitle}</td>
                  </tr>
                  <tr>
                     <th width="20%">이름</th>
                     <td width="30%" id="memberName">${boardList.memberName}</td>
                     <th width="20%">날짜</th>
                     <td width="30%" id="regDate">${boardList.boardDate}</td>
                  </tr>
                  <tr>
                     <th>번호</th>
                     <td id="boardNo">${boardList.boardNo}</td>
                     <th>조회수</th>
                     <td>${boardList.boardCount+1}</td>
                  </tr>
                  <tr>
                     <th>첨부파일</th>
                     <td colspan="3"><c:forEach var="bean" items="${fileList}"
                           varStatus="status">
                           <c:if test="${fileList ne null}">
                              <a target="_blank" href="${bean.fileImgPath}">${bean.fileOriginName}</a>
                           </c:if>
                        </c:forEach></td>
                  </tr>
                  <tr>
                     <th>내용</th>
                     <td colspan="3" id="boardContent"><c:forEach var="bean"
                           items="${fileList}" varStatus="status">
                           <c:if test="${fileList ne null}">
                              <img width="100%" src="${bean.fileImgPath}" class="img-responsive" alt="Responsive image">
                           </c:if>
                        </c:forEach> ${boardList.boardContent}</td>
                  </tr>
               </table>
            </div>
            <!-- 댓글 작성 부분 -->
            <div class="container_reply">
               <table class="table table-condensed">
                  <tr>
                     <td><span class="form-inline" role="form">
                           <div class="form-group">
                              <label>[댓글]</label> <br /> <label>[유저명]&nbsp;&nbsp;:&nbsp;&nbsp;<input
                                 type="text" disabled="disabled"
                                 value="${sessionScope.memberBean.memberName}" /> &nbsp;
                                 <button type="button" id="commentOk" class="btn btn-default">완료</button>
                              </label>
                           </div> <textarea id="commentText" class="form-control col-lg-12"
                              style="width: 100%" rows="4"></textarea>
                     </span></td>
                  </tr>
               </table>
               <hr />
               <!--댓글 -->
               <div class="container_reply">
                  <table>
                     <tbody id="replyList"></tbody>
                     <label>[댓글 리스트]</label>
                     <br />
                     <c:forEach var="bean" items="${boardReplyList}"
                        varStatus="status">
                        <c:choose>
                           <c:when
                              test="${bean.memberNo eq sessionScope.memberBean.memberNo or !empty sessionScope.memberBean.memberAdmin}">
                              <a style="cursor: pointer;"
                                 onclick="deleteReply(${bean.brNo});">${bean.memberName} :
                                 ${bean.brContent}</a>
                              <br />
                           </c:when>
                           <c:otherwise>
                         ${bean.memberName} : ${bean.brContent}
                        <br />
                           </c:otherwise>

                        </c:choose>

                     </c:forEach>
                  </table>
               </div>
               <hr />
               <div class="sub_content">
                  <div class="write_table">

                     <!--  로그인이 되어 있을 때  -->
                     <c:if
                        test="${boardList.memberNo eq sessionScope.memberBean.memberNo or !empty sessionScope.memberBean.memberAdmin}">
                        <button type="submit" class="btn_submit"
                           onclick="location.href='boardUpdate.do?boardNo=${boardList.boardNo}'">수정하기</button>
                        <button type="submit" class="btn_delete" onclick="deleteBoard()">삭제하기</button>
                        <button class="btn_back" onclick="location.href='boardList.do'">뒤로가기</button>
                     </c:if>

                     <!--  로그인이 안 되어 있을 때 -->
                     <c:if
                        test="${boardList.memberNo ne sessionScope.memberBean.memberNo and empty sessionScope.memberBean.memberAdmin}">
                        <button class="btn_back" onclick="location.href='/board/boardList.do'">뒤로가기</button>
                     </c:if>
                  </div>
               </div>

            </div>


         </div>
      </div>
   </div>


   </section>
   <!--메인끝 -->
</body>
</html>
</layoutTag:layout>