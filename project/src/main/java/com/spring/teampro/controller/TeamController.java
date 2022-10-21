package com.spring.teampro.controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.teampro.team.dto.TeamInfoDTO;
import com.spring.teampro.team.dto.TeamMemberDTO;
import com.spring.teampro.team.service.TeamService;

@Controller
public class TeamController {
	
	private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
	
	@Autowired
	TeamService service;
	
	//>>>>>>>>>>>>>>>>teamDATAIL 페이지 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	
	@RequestMapping(value="/team/teamDetail.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String teamDetail(
			Model model,
			@RequestParam("t_key")int t_key
			) {
	
		logger.info("t_key"+t_key);
		//1. 팀정보들,,,
		//2. 조장말
		logger.info("getTeamInfo" + service.getTeamInfo(t_key));
		model.addAttribute("teamInfo",service.getTeamInfo(t_key));
		//3.팀멤버 정보들
		model.addAttribute("MemberInfo",service.getTeamMemberInfo(t_key));
		//4.오늘의 요약
		//5.팀원신청 숫자
		model.addAttribute("anyAlarm",service.anyAlarm(t_key));
		//6.디데이가져오기
		model.addAttribute("dDay", service.getTDay(t_key));

	return "teamDetail";
	}
	
	//팀삭제
	@RequestMapping(value="/team/delTeam.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String deleteTeam(@RequestParam("t_key") int t_key,
			Model model
			) {
		service.deleteTeam(t_key);
		
		return "redirect:/team/allTeamList.do";
	}
	
	//팀탈퇴하기
	@RequestMapping(value="/team/leaveTeam.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String leaveTeam(@RequestParam("t_key") int t_key,
			HttpServletRequest request, HttpServletResponse response
			) {
		HttpSession session = request.getSession();
		int userkey  = (Integer) session.getAttribute("userKey");
		logger.info("탈퇴시켜줘!!");
		TeamMemberDTO dto = new TeamMemberDTO();
		dto.setT_key(t_key);
		dto.setuserKey(userkey);
		
		service.removeMember(dto);
		
		return "redirect:/team/allTeamList.do";
	}
	
	//>>>>>>>>>>>>>>>>allTeamList 페이지 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	
	//allTeamList.jsp로 가기
	@RequestMapping(value="/team/allTeamList.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String getAllTeamList(
			Model model
			) {
		
		List list = service.getAllTeamList();
		model.addAttribute("allTeamList", list);

	return "allTeamList";
	}
	
	//팀원 신청 팝업 이동.
	@RequestMapping(value="/team/newRequest.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String newRequest(@RequestParam("t_key") int t_key,
			Model model
			) {
		List list = service.getRequestList(t_key);
		model.addAttribute("requestList", list);
		
		return "teamPage/memberRequest";
	}
	
	//디데이 수정 팝업 이동.
	@RequestMapping(value="/team/reviseDday.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String reviseDday(@RequestParam("t_key") int t_key,
			Model model
			) {
		//1.현재 디데이를 가져오기.
		String dDay = service.getTDay(t_key);
		model.addAttribute("dDay", dDay);
		model.addAttribute("t_key", t_key);
		
		return "teamPage/dDayForm";
	}
	
	//>>>>>>>>>>>>>>>>team개설 페이지 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	
	//createTeam.jsp로 가기
	@RequestMapping(value="/team/createForm.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String createForm(
			Model model
			) {
		return "createTeam";
	}
	
	//팀생성하기!
	@RequestMapping(value="/team/newTeam.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String newTeam(@ModelAttribute("teamInfo") TeamInfoDTO dto,
			Model model
			) {
		logger.info("팀생성하기!>>>>>>>>>"+dto.getT_name());
		
		service.addNewTeam(dto);
		
		return "redirect:/team/allTeamList.do";
	}
	
	
}
