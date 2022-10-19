 package com.spring.teampro.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.teampro.signupin.dto.AdminDTO;
import com.spring.teampro.signupin.dto.SignUpInDTO;
import com.spring.teampro.signupin.service.SignUpInService;

@Controller
public class SignUpInController {
	
	@Autowired
	SignUpInService signUpInService;
	
	//메인에서 로그인 버튼 누르면 로그인 페이지로 이동 
	@RequestMapping(value="/moveToSignIn.do")
	public String moveToSignIn() {
		return "signIn";
	}
	
	//메인에서 로그인 버튼 누르면 로그인 페이지로 이동 
	@RequestMapping(value="/moveToSignUp.do")
	public String moveToSignUp() {
		return "signUp";
	}
	
	//회원가입 메소드 
	@RequestMapping(value="/signUp.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String signUp(
			@RequestParam("id") String id,
			@RequestParam("pw") String pw,
			@RequestParam("re_pw") String rePw,
			@RequestParam("name") String name,
			@RequestParam("nickname") String nickname,
			@RequestParam("sex") String sex,
			@RequestParam("email") String email,
			HttpServletResponse res) throws Exception{
		
		PrintWriter out = res.getWriter();
		
		SignUpInDTO dto = new SignUpInDTO();
		dto.setId(id);
		dto.setPw(pw);
		dto.setRePw(rePw);
		dto.setName(name);
		dto.setNickName(nickname);
		dto.setSex(sex);
		dto.setEmail(email);

		int idCheck = signUpInService.getIdCheck(dto);
		int emailCheck = signUpInService.getEmailCheck(dto);
		int pwCheck = signUpInService.getPwCheck(dto);
		
		System.out.println("idcheck 결과" + idCheck);
		System.out.println("emailcheck 결과" + emailCheck);
		System.out.println("pwcheck 결과" + pwCheck);
		
		if(idCheck==1) {
			if(pwCheck==1) {
				if(emailCheck==1) {
					if(!(dto.getSex().equals("none"))) {
						int resultAdd=signUpInService.doAddMember(dto);
						if(resultAdd==1) {
							//가입성공 메세지 송출 > 로그인페이지로 이동 
							return "signIn";
							
						} else {
							//가입 실패시 다시 회원가입페이지로
							return "signUp";
						}
					}
					
				}else if(emailCheck==-1) {
					//이메일 사용불가
					return "signUp";
				}else {
					//db오류
					return "signUp";
				}
				
			}else if(pwCheck==-1){
				//비번값 불일치
				return "signUp";
			}else {
				//db오류
				return "signUp";
			}
			
		}else if(idCheck==-1) {
			//아이디 사용불가, 사용중이 아이디 있음
			return "signUp";
		}else {
			//db오류
			return "signUp";
		}
		return "signUp";
	}
	
	
	//회원가입폼에서 아이디 중복체크
	@RequestMapping(value="/idcheck.do", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody public int idCheck(@RequestParam("id") String id) {
		int result = 0;
		SignUpInDTO dto = new SignUpInDTO();
		dto.setId(id);
		result = signUpInService.getIdCheck(dto);
		System.out.println("idck" + result);
		return result;
	}
	
	//회원가입폼에서 이메일 중복체크
	@RequestMapping(value="/emailcheck.do", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody public int emailCheck(@RequestParam("email") String email) {
		int result = 0;
		SignUpInDTO dto = new SignUpInDTO();
		dto.setEmail(email);
		result = signUpInService.getEmailCheck(dto);
		return result;
	}
	
	
	@RequestMapping(value="/signIn.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String signIn(Model model,
			@RequestParam("id") String id,
			@RequestParam("pw") String pw,
			HttpServletRequest req) {
		//result 99:관리자 0:실패 1: 로그인진행 
		int result = signUpInService.doSignIn(id, pw);
		
		
		if( result == 99) {
			// 관리자 로그인 성공 > 메인 admin으로 이동 
			return "main_admin";
		}else if(result ==1 ) {
			//로그인 성공 > 메인2로 이동
			signUpInService.updateLastTime(id);
			HttpSession session = req.getSession();
			session.setAttribute("userInfo", signUpInService.getUserInfo(id));
			session.setAttribute("userKey", signUpInService.getUserInfo(id).getUserKey());
			
			return "main2";
		}else {
			//로그인 실패 >  로그인 페이지로 
			return "signIn";
		}
	}
	
	//로그아웃 
	@RequestMapping(value="/signOut.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String signOut(HttpServletRequest req) {
		req.getSession().invalidate();
		return "main";
	}
	
	
	//관리자관련
	
	//<회원관련 조회>
	//회원조회 페이지로 이동하면서 전체회원 조회
	@RequestMapping(value="/memberList.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String memberList(Model model) {
		List<SignUpInDTO> list = signUpInService.getMemberList();
		model.addAttribute("list",list);
		return "memberList";
	}
	
	//이름으로 회원 조회
	@RequestMapping(value="/searchByName.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchNameList(Model model,
			@RequestParam("name") String keyword) {
		List<SignUpInDTO> list = signUpInService.getListByName(keyword);
		model.addAttribute("list",list);
		return "memberList";
	}
	
	//아이디로 회원 조회
	@RequestMapping(value="/searchById.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchIdList(Model model,
			@RequestParam("id") String keyword) {
		List<SignUpInDTO> list = signUpInService.getListById(keyword);
		model.addAttribute("list",list);
		return "memberList";
	}
	
	//이름+아이디로 회원 조회
	@RequestMapping(value="/searchByBoth.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchBothList(Model model,
			@RequestParam("both") String keyword) {
		List<SignUpInDTO> list = signUpInService.getListByBoth(keyword);
		model.addAttribute("list",list);
		return "memberList";
	}
	
	//<스터디 관련 조회>
	
	//<회원관련 조회>
	//회원조회 페이지로 이동하면서 전체회원 조회
	@RequestMapping(value="/studyList.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String studyList(Model model) {
		List<AdminDTO> list = signUpInService.getStudyList();
		model.addAttribute("list",list);
		return "studyList";
	}
	
	//팀명으로 조회
	@RequestMapping(value="/searchByTeamName.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchByTname(Model model,
			@RequestParam("teamName") String keyword) {
		List<AdminDTO> list = signUpInService.getListByTname(keyword);
		model.addAttribute("list",list);
		return "studyList";
		
	}
	
	//팀장으로 조회
	@RequestMapping(value="/searchByTeamLeader.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchByTleader(Model model,
			@RequestParam("teamLeader") String keyword) {
		List<AdminDTO> list = signUpInService.getListByTleader(keyword);
		model.addAttribute("list",list);
		return "studyList";
	}
	
	//팀정보로 조회
	@RequestMapping(value="/searchByTeamInfo.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String searchByTinfo(Model model,
			@RequestParam("teamInfo") String keyword) {
		List<AdminDTO> list = signUpInService.getListByTinfo(keyword);
		model.addAttribute("list",list);
		return "studyList";
	}
	
	//<회원정보 수정>
	// 수정 클릭시 수정폼으로 넘어가도록
	@RequestMapping(value="/modForm.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String modMember(Model model,
			@RequestParam("id") String id) {
		SignUpInDTO dto = signUpInService.getUserInfo(id);
		model.addAttribute("modUser", dto);
		
		return "modForm";
	}
	
	// 수정진행
	@RequestMapping(value="/modMember.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String modMember(Model model,
			@RequestParam("id") String id,
			@RequestParam("pw") String pw, 
			@RequestParam("nickName") String nickName,
			@RequestParam("sex") String sex,
			@RequestParam("email") String email) {
				
		SignUpInDTO dto = new SignUpInDTO();
		dto.setId(id);
		dto.setPw(pw);
		dto.setNickName(nickName);
		dto.setSex(sex);
		dto.setEmail(email);
		
		signUpInService.doModMember(dto);
		model.addAttribute("list", signUpInService.getMemberList());
		return "memberList";
	}
	
	//회원삭제 
	@RequestMapping(value="/delMember.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String delMember(Model model,
			@RequestParam("id") String id) {

		signUpInService.doDelMember(id);

		model.addAttribute("list", signUpInService.getMemberList());
		return "memberList";
	}
	
}
