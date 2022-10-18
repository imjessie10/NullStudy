package com.spring.teampro.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.teampro.signupin.dto.SignUpInDTO;
import com.spring.teampro.team.service.TeamService;

@Controller
public class TeamController {
	
	private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
	
	@Autowired
	TeamService service;
	
	@RequestMapping(value="/team/teamDetail.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String teamDetail(
			Model model,
			@RequestParam("t_key")int t_key
			) {
	
	//1. 팀정보들,,,
//		2. 조장말
		model.addAttribute("teamInfo",service.getTeamInfo(t_key));
//		3.팀멤버 정보들
//		4.오늘의 요약
//		5.

	return "teamDetail";
	}
	
	
	
}
