package com.prac.ex1.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prac.ex1.board.bean.BoardBean;
import com.prac.ex1.board.bean.BoardFileBean;
import com.prac.ex1.board.bean.BoardReplyBean;
import com.prac.ex1.board.dao.BoardDao;
import com.prac.ex1.board.dao.BoardFileDao;
import com.prac.ex1.board.dao.BoardReplyDao;
import com.prac.ex1.common.Util;
import com.prac.ex1.common.bean.PagingBean;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private BoardFileDao boardFileDao;
	
	@Autowired
	private BoardReplyDao boardReplyDao;

	@Override
	public int insertBoardFile(BoardBean bBean, BoardFileBean bfBean, String upPath) {
		int resVal = 0;
		try {
			resVal = boardDao.insertBoard(bBean);

			// 올리기 실패
			if (resVal <= 0) {
				return 0;
			} // insert 실패

			// 올리는 부분
			for (int i = 0; i < bfBean.getUpFile().length; i++) {
				MultipartFile file = bfBean.getUpFile()[i]; // 멀티파트파일 클래스에 배열단위로
															// 파일을 넣어준 후
				// insert
				BoardFileBean inBean = new BoardFileBean();
				String fileName = ""; // 저장한 파일이름을 저장하기 위한 변수
				String toPath = ""; // 총 파일경로 저장하기 위한 변수

				fileName = Util.uploadFileMng(file, upPath, inBean); // 파일 저장하는 부분
				
				// 이미지 파일 경로 저장
				toPath = "/upfile/" + fileName;
				
				inBean.setFilePath(upPath);
				inBean.setBoardNo(bBean.getBoardNo());
				inBean.setBfName(fileName);
				inBean.setFileImgPath(toPath);
				// DB
				int resValCheck = boardFileDao.insertBoardFile(inBean);

				if (resValCheck <= 0) { // 파일업로드 실패의 유효성 검사c
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resVal;
	}
	
	@Override
	public int insertBoard(BoardBean bBean) {
		return boardDao.insertBoard(bBean);
	}

	@Override
	public BoardBean selectBoard(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardDao.selectBoard(bean);
	}

	@Override
	public List<BoardBean> selectBoardInfo(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardDao.selectBoardInfo(bean);
	}

	@Override
	public int updateBoard(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardDao.updateBoard(bean);
	}

	@Override
	public int updateBoardJoin(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardDao.updateBoardJoin(bean);
	}

	@Override
	public List<BoardFileBean> selectBoardFileList(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardFileDao.selectBoardFileList(bean);
	}

	@Override
	public int deleteBoard(BoardBean bean) {
		// TODO Auto-generated method stub
		return boardDao.deleteBoard(bean);
	}

	@Override
	public int deleteBoardFile(BoardBean bBean, BoardFileBean bfBean) {
		// TODO Auto-generated method stub
		int resVal = 0;
		try {
			int resValCheck = boardFileDao.deleteBoardFile(bfBean);
			if (resValCheck <= 0) {resValCheck = 0;}
			
			resVal = boardDao.deleteBoard(bBean);
			if (resVal <= 0 && resValCheck == 0) {return 0;}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resVal;
	}

	@Override
	public int deleteBoardOnlyFile(BoardFileBean bfBean) {
		// TODO Auto-generated method stub
		return boardFileDao.deleteBoardFile(bfBean);
	}

	@Override
	public int updateBoardFile(BoardBean bBean, BoardFileBean bfBean, String upPath) {
		int resVal = 0;
		try {
			bBean.setMemberId("test");
			resVal = boardDao.updateBoard(bBean);
			

			// 올리기 실패
			if (resVal <= 0) {
				return 0;
			} // insert 실패

			// 올리는 부분
			for (int i = 0; i < bfBean.getUpFile().length; i++) {
				MultipartFile file = bfBean.getUpFile()[i]; // 멀티파트파일 클래스에 배열단위로
															// 파일을 넣어준 후
				// insert
				BoardFileBean inBean = new BoardFileBean();
				String fileName = ""; // 저장한 파일이름을 저장하기 위한 변수
				String toPath = ""; // 총 파일경로 저장하기 위한 변수

				fileName = Util.uploadFileMng(file, upPath, inBean); // 파일 저장하는 부분
				
				// 이미지 파일 경로 저장
				toPath = "/upfile/" + fileName;
				
				inBean.setFilePath(upPath);
				inBean.setBoardNo(bBean.getBoardNo());
				inBean.setBfName(fileName);
				inBean.setFileImgPath(toPath);
				// DB
				int resValCheck = boardFileDao.insertBoardFile(inBean);

				if (resValCheck <= 0) { // 파일업로드 실패의 유효성 검사c
					return 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resVal;
	}
	@Override
	public List<BoardBean> selectBoardList(PagingBean bean) throws Exception {
		// TODO Auto-generated method stub
		return boardDao.selectBoardList(bean);
	}

	@Override
	public int selectBoardListTotalConut(PagingBean bean) throws Exception {
		// TODO Auto-generated method stub
		return boardDao.selectBoardListTotalConut(bean);
	}
	@Override
	public int writeBoardReply(BoardReplyBean rBean) {
		// TODO Auto-generated method stub
		return boardReplyDao.writeBoardReply(rBean);
	}

	@Override
	public List<BoardReplyBean> selectBoardReplyList(BoardReplyBean bean) {
		// TODO Auto-generated method stub
		return boardReplyDao.selectBoardReplyList(bean);
	}

	@Override
	public BoardReplyBean selectBoardReply(BoardReplyBean bean) {
		// TODO Auto-generated method stub
		return boardReplyDao.selectBoardReply(bean);
	}

	@Override
	public int deleteBoardReply(BoardReplyBean bean) {
		// TODO Auto-generated method stub
		return boardReplyDao.deleteBoardReply(bean);
	}
}
