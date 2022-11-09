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
<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728047-a478e614-05d5-49b1-9c9b-c092c66bfb28.png"/>

### 📅 달력 및 일정관리 게시판
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

<img width="100%" src="https://user-images.githubusercontent.com/107744382/200728042-1d7cf607-1daa-4cf3-b720-c146747f6749.png"/>
### 📋 메모


* 메모CRUD
* 페이징
* 코드샘플
