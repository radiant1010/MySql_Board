package com.prac.ex1.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prac.ex1.board.bean.BoardBean;
import com.prac.ex1.common.Constants;
import com.prac.ex1.common.bean.PagingBean;
import com.prac.ex1.member.bean.MemberBean;
import com.prac.ex1.member.service.MemberService;
import com.prac.ex1.utils.SessionUtil;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	/** 회원 로그인 폼  */
	@RequestMapping("/member/loginForm")
	public String loginForm() throws Exception {
		return "/member/loginForm";
	}

	// 회원가입화면
	@RequestMapping("/join")
	public String joinForm() {
		return "/join";
	}

	// 회원가입처리
	@RequestMapping("/joinProc")
	@ResponseBody
	public Map<String, Object> joinProc(MemberBean memberBean) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);
		resMap.put(Constants.RESULT_MSG, "회원가입에 실패하였습니다.");
		// DB insert
		try {
			int res = memberService.insertMember(memberBean);
			if (res > 0) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);
				resMap.put(Constants.RESULT_MSG, "회원가입에 성공하였습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}

	// ID 중복확인
	@RequestMapping("/member/joinDoubleCheckProc")
	@ResponseBody
	public Map<String, Object> joinDoubleCheckProc(MemberBean memberBean) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put(Constants.RESULT, Constants.RESULT_FAIL);

		// DB select
		try {

			if (memberService.selectJoinMember(memberBean) != null) {
				resMap.put(Constants.RESULT, Constants.RESULT_OK);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}
	/** 로그인  */
	@RequestMapping("/member/loginAjax")
	@ResponseBody
	public Map<String, Object> loginAjax(MemberBean memberBean, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String result = null;
		String resultMsg = null;
		int resultCnt = -1;
		try {
			resultCnt = memberService.login(memberBean);
			if (resultCnt == 1) {
				result = Constants.OK;
				resultMsg = "로그인 성공";
				memberBean = memberService.selectLoginMember(memberBean);
				// 세션에 userBean으로 member 정보 저장
				SessionUtil.setMemberBean(request, memberBean);
			} else {
				result = Constants.FAIL;
				resultMsg = "아이디와 비밀번호를 확인하세요";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Constants.FAIL;
			resultMsg = "시스템 에러";
		}
		map.put("result", result);
		map.put("resultMsg", resultMsg);
		return map;
	}

	/**
	 * 로그아웃
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/member/logoutAjax")
	@ResponseBody
	public Map<String, Object> logoutAjax(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		SessionUtil.removeMemberBean(request);

		resultMap.put("result", Constants.OK);
		resultMap.put("resultMsg", "정상적으로 로그아웃 되셨습니다.");

		return resultMap;
	}

	/** 회원가입 수정화면 **/
	@RequestMapping("/member/myList")
	public String updateMemberForm(MemberBean memberBean, Model model) {

		try {
			MemberBean resBean = memberService.selectLoginMember(memberBean);
			model.addAttribute("memberBean", resBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/member/myList";
	}

	/** 회원정보 수정 처리를 한다. **/
	@RequestMapping("/member/updateMemberProc")
	@ResponseBody
	public Map<String, Object> updateMemberProc(MemberBean memberBean, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.RESULT, Constants.RESULT_FAIL);
		map.put(Constants.RESULT_MSG, "비밀번호가 틀렸습니다.");
		map.put("memberBean", memberBean);

		// DB update
		try {
			int res = memberService.updateMember(memberBean);
			if (res > 0) {
				map.put(Constants.RESULT, Constants.RESULT_OK);
				map.put(Constants.RESULT_MSG, "수정성공");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/** 멤버 List 화면**/
	@RequestMapping("/member/memberList")
	public String noticeList(Model model, MemberBean bean) {
		List<MemberBean> list;
		try {
			list = memberService.selectMemberList(bean);
			model.addAttribute("memberList", list);
			model.addAttribute("memberBean", bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/member/memberList";
	}
	/** 회원 삭제  */
	@RequestMapping("/member/memberDeleteAjax")
	@ResponseBody
	public Map<String, Object> memberDeleteAjax (MemberBean memberBean,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String result = null;
		String resultMsg = null;
		try {
			memberService.deleteMember(memberBean);
			result = Constants.OK;
			resultMsg = "회원삭제 성공";
//			SessionUtil.removeMemberBean(request);
		} catch (Exception e) {
			e.printStackTrace();
			result = Constants.FAIL;
			resultMsg = "시스템 에러";
		}
		map.put("resultMsg", resultMsg);
		map.put("result", result);
		return map;
	}
	
}
