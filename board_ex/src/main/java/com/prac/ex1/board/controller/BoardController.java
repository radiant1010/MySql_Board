package com.prac.ex1.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prac.ex1.board.bean.BoardBean;
import com.prac.ex1.board.bean.BoardFileBean;
import com.prac.ex1.board.bean.BoardReplyBean;
import com.prac.ex1.board.service.BoardService;
import com.prac.ex1.common.Constants;
import com.prac.ex1.common.bean.PagingBean;
import com.prac.ex1.member.bean.MemberBean;
import com.prac.ex1.utils.SessionUtil;

@Controller
public class BoardController {
	
	//파일 업로드 저장경로
	@Value("#{config['file.upload.path']}")
	private String FILE_UPLOAD_PATH;
	@Value("#{config['upload.board.img.path']}")
	private String FILE_IMG_PATH;
	
	@Autowired
	private BoardService boardService;

	/** 게시판 View 화면 **/
	@RequestMapping("/board/boardView")
	public String boardView(Model model, BoardBean bBean, BoardFileBean bfBean, BoardReplyBean brBean) {
		
		// 게시글 조회
		BoardBean baordBean = boardService.selectBoard(bBean);
		// 조회수 증가
		int baordBeanJoin = boardService.updateBoardJoin(bBean);
		// 파일 업로드 조회
		List<BoardFileBean> baordbfBean = boardService.selectBoardFileList(bBean);
		// 덧글
		List<BoardReplyBean> baordbrList = boardService.selectBoardReplyList(brBean);
		List<BoardReplyBean> tt = new ArrayList<BoardReplyBean>();
		for (int i = 0; i < baordbrList.size(); i++) {
			if (baordbrList.get(i).getBoardNo().equals(bBean.getBoardNo())){
				tt.add(baordbrList.get(i));
			}
		}
		model.addAttribute("boardList",baordBean);
		model.addAttribute("baordBeanJoin", baordBeanJoin);
		model.addAttribute("fileList", baordbfBean);
		model.addAttribute("boardReplyList", tt);

		return "/board/boardView";
	}
	
	/** 게시판 View AJAX **/
	@RequestMapping("/board/boardViewAjax") 
	@ResponseBody
	public Map<String, Object> boardViewAjax(BoardBean bean) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "게시글 상세보기 실패");
		
		
		BoardBean bBean =  boardService.selectBoard(bean);
		
		resMap.put(Constants.RESULT, Constants.RESULT_OK);
		resMap.put(Constants.RESULT_MSG, "게시글 상세보기 성공");
		resMap.put("bBean", bBean); // 게시글 저장
		
		return resMap;
	}
	
	/** 게시판  InsertReply AJAX **/
	@RequestMapping("/board/boardReplyViewAjax")
	@ResponseBody
	public Map<String, Object> boardReplyViewAjax (BoardBean bean, BoardReplyBean rBean, HttpServletRequest request) {
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "댓글 작성 실패");
		
		MemberBean mBean = SessionUtil.getMemberBean(request);
		
		rBean.setMemberNo(mBean.getMemberNo());
		rBean.setMemberName(mBean.getMemberName());
		
		try {
			int res = boardService.writeBoardReply(rBean);

			List<BoardReplyBean> LbBean = boardService.selectBoardReplyList(rBean);

			BoardReplyBean LbBeanIndex = LbBean.get(LbBean.size()-1);

			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "댓글 작성 성공");
				resMap.put("BoardReplyBean", LbBeanIndex); // 게시글 저장
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	
	/** 게시판 List 화면 **/
	@RequestMapping("/board/boardList")
	public String boardList(Model model, BoardBean bBean, PagingBean pagingBean) {
		try {
			int totRecord = boardService.selectBoardListTotalConut(pagingBean);
			pagingBean.calcPage(totRecord);
			model.addAttribute("pagingBean",pagingBean);
			List<BoardBean> list = boardService.selectBoardList(pagingBean);
			model.addAttribute("boardList", list);
		} catch(Exception e){
			e.printStackTrace();
		}
		return "/board/boardList";
	}
	
	/** 게시판 List AJAX **/
	@RequestMapping("/board/boardListProc")
	@ResponseBody
	public Map<String, Object> boardListProcAjax(PagingBean pagingBean, BoardReplyBean boardReplyBean) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "게시글 조회 실패");

		try {
			int totRecord = boardService.selectBoardListTotalConut(pagingBean);
			pagingBean.calcPage(totRecord);
			List<BoardBean> list = boardService.selectBoardList(pagingBean);
			resMap.put(Constants.RESULT, Constants.RESULT_OK);
			resMap.put(Constants.RESULT_MSG, "게시글 조회 성공");

			resMap.put("boardBean", list);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}
	
	/** 게시판 쓰기 화면 **/
	@RequestMapping("/board/boardWrite") 
		public String boardWrite() {
			return "/board/boardWrite";
	}
	
	/** 게시판 쓰기 AJAX **/
	@RequestMapping("/board/boardWriteAjax")
	@ResponseBody
	public Map<String, Object> boardWriteAjax(BoardBean bBean, BoardFileBean bfBean, HttpServletRequest request) {
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "게시글 작성 실패");
		
		MemberBean mBean = SessionUtil.getMemberBean(request);
		
		bBean.setMemberNo(mBean.getMemberNo());
		
		try {
			int res = boardService.insertBoardFile(bBean, bfBean, FILE_UPLOAD_PATH + "/upfile");

			List<BoardBean> LbBean = boardService.selectBoardInfo(bBean);

			BoardBean LbBeanIndex = LbBean.get(LbBean.size()-1);

			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "게시글 작성 성공");
				resMap.put("LbBean", LbBeanIndex); // 게시글 저장
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	
	
	/** 게시판 글쓰기 수정 화면 **/
	@RequestMapping("/board/boardUpdate") 
	public String boardUpdate(BoardBean bBean, Model model) {

		BoardBean baordBean =  boardService.selectBoard(bBean);
		
		// 파일 업로드 조회
		List<BoardFileBean> baordbfBean = boardService.selectBoardFileList(bBean);

		model.addAttribute("boardList", baordBean);
		model.addAttribute("fileList", baordbfBean);

		return "/board/boardUpdate";
	}
	
	/** 게시판 글쓰기 수정 AJAX **/
	@RequestMapping("/board/boardUpdateAjax")
	@ResponseBody
	public Map<String, Object> boardUpdateAjax(BoardBean bBean, BoardFileBean bfBean) {

		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "게시글 수정 실패");

		try {
			
			int res = boardService.updateBoardFile(bBean, bfBean, FILE_UPLOAD_PATH + "/upfile");
			
			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "게시글 수정 성공");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	/** 게시글 글 및 파일 삭제 AJAX **/
	@RequestMapping("/board/deleteBoardAjax")
	@ResponseBody
	public Map<String, Object> deleteBoardAjax(BoardBean bBean, BoardFileBean bfBean) {
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "게시글 삭제 실패");
		
		try {
			int res = boardService.deleteBoardFile(bBean, bfBean);
			
			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "게시글 삭제 성공");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	
	/** 게시글  파일 삭제 AJAX **/
	@RequestMapping("/board/deleteBoardFileAjax")
	@ResponseBody
	public Map<String, Object> deleteBoardFileAjax(BoardFileBean bfBean) {
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "파일 삭제 실패");
		
		try {
			int res = boardService.deleteBoardOnlyFile(bfBean);
			
			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "파일 삭제 성공");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
	
	/** 댓글 삭제 AJAX **/
	@RequestMapping("/board/deleteBoardReplyAjax")
	@ResponseBody
	public Map<String, Object> deleteBoardReplyAjax(BoardReplyBean brBean) {
		
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "댓글 삭제 실패");
		
		try {
			int res = boardService.deleteBoardReply(brBean);
			
			if(res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "댓글 삭제 성공");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}
}
