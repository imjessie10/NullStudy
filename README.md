# NullStudy_Team_Project
> 스프링 + JSP 프로젝트 입니다.

## 목적
- 웹개발 연습
- 학습 내용 확인 및 구현

## 💻개발기간
- 1차 : DB 접속
- 2차 : MVC2 설계
- 최종 2022.10.04 ~ 2022.10.25

## 👥멤버구성
- 제시 : 캘린더, 메모
- 토니 : 로그인, 회원가입, 관리자
- 애니 : 스터디관련 모든페이지
- 콩 : 자유게시판
- 규 : 서치

## ⚙️개발환경
- JAVA 11
- OS: windows 10
- DB: Oracle 11g xe
- WAS: Apache tomcat 9
- IDE: eclipse

## 📍나의 주요기능

<img width="100%" src="https://user-images.githubusercontent.com/107744382/200727672-1678e004-5d2e-4d05-b6a4-f703e835ef8b.png"/>
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728042-1d7cf607-1daa-4cf3-b720-c146747f6749.png"/>
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728047-a478e614-05d5-49b1-9c9b-c092c66bfb28.png"/>

### 📅 달력 및 일정관리 게시판
-
-
- 코드샘플

    @Controller
    public class MyMemoController {
    	
    	private static final Logger logger = LoggerFactory.getLogger(MyMemoController.class);
    	
	@Autowired
	MemoService memoService;
	
	//메모 페이징으로
	@RequestMapping(value="/mystudy/memo.do", method= {RequestMethod.GET, RequestMethod.POST} )
	public String memolist(
			Model model
			,HttpServletRequest request
			,@ModelAttribute MemoDTO memoDTO
			) {
		logger.info("memolist 실행");
		HttpSession session = request.getSession();
		
		int userkey = (Integer) session.getAttribute("userKey");
		logger.info("userkey"+userkey);
		
		int viewPage = memoDTO.getViewPage();
		int countPerPage = memoDTO.getCountPerPage();
		
		int total = memoService.selectlistCount(userkey);
		int totalPage = (int) Math.ceil( (double)total/ countPerPage );
		
		int startIdx = ( (viewPage - 1) * countPerPage ) + 1;
		int endIdx =  viewPage * countPerPage;
		
		memoDTO.setStartIdx(startIdx);
		memoDTO.setEndIdx(endIdx);
		memoDTO.setUserkey(userkey);
		
		logger.info("페이징용>>"+startIdx+","+endIdx);
		
		logger.info(">>"+memoDTO.getUserkey());
		
		List<MemoDTO> list = memoService.selectPagingList(memoDTO);		
		logger.info("리스트 사이즈: "+list.size());
		
		model.addAttribute("userkey", userkey);
		model.addAttribute("viewPage", viewPage);
		model.addAttribute("countPerPage", countPerPage);
		
		model.addAttribute("total", total);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("resultList",list);
		
		return "memo";
	}
}  

### 📋 메모

