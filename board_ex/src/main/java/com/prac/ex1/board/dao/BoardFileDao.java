package com.prac.ex1.board.dao;

import java.util.List;

import com.prac.ex1.board.bean.BoardBean;
import com.prac.ex1.board.bean.BoardFileBean;

public interface BoardFileDao {
	
	/** 파일 insert **/
	public int insertBoardFile(BoardFileBean bean);
	
	/** 파일 List **/
	public List<BoardFileBean> selectBoardFileList(BoardBean bean);
	
	/** 파일 삭제 **/
	public int deleteBoardFile(BoardFileBean bean);
	

}
