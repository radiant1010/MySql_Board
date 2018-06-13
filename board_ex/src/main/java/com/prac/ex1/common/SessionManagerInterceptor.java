package com.prac.ex1.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.prac.ex1.member.bean.MemberBean;


public class SessionManagerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception 
	{
		MemberBean mBean = (MemberBean)request.getSession().getAttribute(Constants.SESSION_MEMBER_BEAN);
		if( mBean.getMemberAdmin() != null  ) return true;
		
		response.sendRedirect("/board/noticeList.do");
		
		return false;
	}
	
}
