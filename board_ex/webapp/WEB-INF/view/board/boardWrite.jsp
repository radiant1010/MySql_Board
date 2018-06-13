<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
   <%@ taglib prefix="layoutTag" tagdir="/WEB-INF/tags"%>
<layoutTag:layout>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
   function writeBoard() {
      var formData = new FormData();
      var _boardTitle = $("#boardTitle").val();
      var _boardContent = $("#boardContent").val();

      if ("" == _boardTitle) {
         alert("제목을 입력하세요");
         $("#boardTitle").focus();
         return;
      } else if ("" == _boardContent) {
         alert("내용을 입력하세요");
         $("#boardContent").focus();
         return;
      }
      formData.append("boardTitle", $("#boardTitle").val());
      formData.append("boardContent", $("#boardContent").val());
      if ($("input[name=upFile]")[0].files[0] != null) {

         formData.append("upFile", $("input[name=upFile]")[0].files[0]);
      }
      /*  formData.append("upFile", 
            $("input[name=upFile]")[1].files[0]) ; */

      $.ajax({
         url : "/board/boardWriteAjax.do",
         processData : false,
         contentType : false,
         type : 'POST',
         data : formData,
         success : function(data) {

            alert(data.resultMsg);

            if (data.result == "ok") {
               //화면이동 처리
               var boardNo = data.LbBean;

               location.href = "/board/boardView.do?boardNo="
                     + boardNo.boardNo;
            }
         }
      }); // end of ajax
   }; // end of function
</script>
</head>
<body>
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
                        <td><input type="text" class="input" id="boardTitle"
                           placeholder="제목을 입력해 주세요" /></td>
                     </tr>
                     <tr>
                        <th>첨부파일</th>
                        <td><input type="file" name="upFile"></td>
                     </tr>
                     <tr>
                        <th>내용</th>
                        <td><textarea id="boardContent"></textarea></td>
                     </tr>
                  </table>
                  <button type="submit" class="btn_submit" onclick="writeBoard()">등록하기</button>
               </div>
            </div>
         </div>
      </div>


   </section>
   <!--메인끝 -->
</body>
</html>
</layoutTag:layout>