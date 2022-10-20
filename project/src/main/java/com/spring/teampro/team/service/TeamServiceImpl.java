package com.spring.teampro.team.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.teampro.team.dao.TeamDAO;
import com.spring.teampro.team.dto.MemberRequestDTO;
import com.spring.teampro.team.dto.MyTeamListDTO;
import com.spring.teampro.team.dto.TeamInfoDTO;
import com.spring.teampro.team.dto.TeamMemberDTO;

@Service
public class TeamServiceImpl implements TeamService {
	
	private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);

	@Autowired
	TeamDAO dao;
	
	//>>>>>>>>>>>>>>>>팀 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	//나의 팀 리스트
	@Override
	public List getMyTeamList(int userkey) {
		return  dao.getMyTeamList(userkey);
		
	}
	
	//내가 속한 팀인가요?
	@Override
	public boolean alreadyMyTeam(int userkey,int t_key) {
		
		boolean result = false;
		
		List list = dao.getMyTeamList(userkey);
		
		for(int i=0; i<list.size();i++) {
			MyTeamListDTO dto = (MyTeamListDTO) list.get(i);
			int myt_key = dto.getT_key();
			if(myt_key == t_key) {
				result = true;
			}
		}
		return result;
	}
	
	//팀정보 가져오기
	@Override
	public TeamInfoDTO getTeamInfo(int t_key) {
		
		TeamInfoDTO dto =  dao.getTeamInfo(t_key);
		int t_filed = dto.getT_field();
		
		switch(t_filed) {
		case 1:
			dto.setT_field2("코딩");
			break;
		case 2:
			dto.setT_field2("자격증");
			break;
		case 3:
			dto.setT_field2("토익");
			break;
		case 4:
			dto.setT_field2("기타");
			break;
		}
		
		return dto;
	}

	//팀정보 업데이트 하기
	@Override
	public int updateTeamInfo(TeamInfoDTO dto) {
		return dao.updateTeamInfo(dto);
	}
		
	//조장한마디 업데이트 하기 
	@Override
	public int updateLMemo(TeamInfoDTO dto) {
		return dao.updateLMemo(dto);
	}
	
	//전체 팀리스트 가져오기
	@Override
	public List getAllTeamList() {
		
		List list = dao.allTeamList();
		
		for(int i=0; i<list.size();i++) {
			TeamInfoDTO dto = (TeamInfoDTO)list.get(i);
			int t_filed = dto.getT_field();
			
			switch(t_filed) {
			case 1:
				dto.setT_field2("코딩");
				break;
			case 2:
				dto.setT_field2("자격증");
				break;
			case 3:
				dto.setT_field2("토익");
				break;
			case 4:
				dto.setT_field2("기타");
				break;
			}
		}
		
		return list;
	}

	//팀삭제
	@Override
	public int deleteTeam(int t_key) {
		return dao.deleteTeam(t_key);
	}
	
	//>>>>>>>>>>>>>>>>팀멤버 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	//팀 멤버 정보 가져오기
	@Override
	public List getTeamMemberInfo(int t_key) {
		
		List list = dao.getTeamMemberInfo(t_key);
		
		for(int i=0; i<list.size(); i++) {
			TeamMemberDTO memberDTO = (TeamMemberDTO) list.get(i);
			Date lastTime = memberDTO.getLastTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd hh:mm a");
			String time = format.format(lastTime);
			memberDTO.setLastTime2(time);
		}
		return list;
	}

	//멤버 강퇴
	@Override
	public int removeMember(TeamMemberDTO dto){
		return dao.removeMember(dto);
	}

	//>>>>>>>>>>>>>>>>팀가입요청 관련>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
	//가입요청하기
	@Override
	public int requestMember(MemberRequestDTO dto) {
		return dao.memberRequest(dto);
	}

	//이미 가입요청했는가?
	@Override
	public boolean alreadyRequest(MemberRequestDTO dto) {
		boolean result = false;
		int count = dao.alreadyRequest(dto);
		if(count > 0) {
			result = true;
		}
		return result;
	}
	
	//가입요청이 있는가?
	@Override
	public int anyAlarm(int t_key) {
		return dao.anyAlarm(t_key);
	}

	//있다면 가입요청 메세지 리스트 가져오기
	@Override
	public List getRequestList(int t_key) {
		return dao.getRequestList(t_key);
	}

	//멤버 수락하기
	@Override
	public int acceptMember(MemberRequestDTO dto) {
		dao.acceptMember(dto);
		int t_key = dto.getT_key();
		return dao.updateMemberCount(t_key);
	}

	//멤버 거절하기
	@Override
	public int rejectMember(MemberRequestDTO dto) {
		return dao.rejectMember(dto);
	}

	@Override
	public int addNewTeam(TeamInfoDTO dto) {
		return dao.addNewTeam(dto);
	}


}
