# NullStudy_Team_Project
> 스프링 + JSP 프로젝트 입니다.
## 목적
- 웹개발 연습
- 학습 내용 확인 및 구현

>메인
<img width="80%" src="https://user-images.githubusercontent.com/107744382/200740858-44e7cfeb-2b8f-4c46-8127-51b26ca4c10f.png"/>
>대표 페이지
<img width="80%" src="https://user-images.githubusercontent.com/107744382/200740869-fb8f9179-2c37-4831-8dd8-6b0cae355653.png"/>


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


### 📅 달력 및 일정관리 게시판
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200727672-1678e004-5d2e-4d05-b6a4-f703e835ef8b.png"/>
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728047-a478e614-05d5-49b1-9c9b-c092c66bfb28.png"/>

* 일정CRUD
   * 달력 해당 요일에 최신순 3개 출력
   * 해당 날짜에 마우스호버시 전체개수 출력-ajax 구현
* 달력
   * JAVA로 구현
```
@RequestMapping(value = "/mystudy/calCountAjax.do", method = {RequestMethod.GET, RequestMethod.POST})
@ResponseBody public String calCountAjax(
    Model model
    ,@RequestParam String m_schedule_date
    ,@ModelAttribute ScheduleDTO sdto
    , HttpServletRequest request
    ) {
  logger.info("아작스 통신");

  //유저키 세션으로 잡아오기
  HttpSession session = request.getSession();
  Integer userkey = (Integer) session.getAttribute("userKey");
  logger.info(">> calCountAjax--userkey"+userkey);
  sdto.setUserkey(userkey);
  sdto.setM_schedule_date(m_schedule_date);

  int count = scheduleService.calCount(sdto);
  logger.info(">> calCountAjax--count"+count);

  return count+"";
}
```
```
$(function(){
  $(".countView").hover(function(){

    var aObj = $(this);
    var year = $(".y").text().trim();
    var month = $(".m").text().trim();
    var date = $(this).text().trim();
    var m_schedule_date = year + "-" + isTwo(month) + "-" + isTwo(date);

    $.ajax({
      method:"post",
      url:"/project/mystudy/calCountAjax.do",
      data: {"m_schedule_date" : m_schedule_date },
      dataType: "text",
      async:false,
      success:function(val){
        aObj.after("<div class='cPreview'>"+val+"</div>");
        console.log(val);
      },
      error:function(){
        alert("서버통신실패xxx");
      }
    });
  },function(){
    $(".cPreview").remove();	//마우스가 나가면 해당엘리먼트 삭제
  });
});
```


### 📋 메모
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728042-1d7cf607-1daa-4cf3-b720-c146747f6749.png"/>


* 메모CRUD
* 페이징
* 코드샘플
```
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
```
