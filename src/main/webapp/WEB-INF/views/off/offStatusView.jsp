<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>휴가현황</title>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
 	<link rel="stylesheet" href="resources/css/layout.css">
 	<link rel="stylesheet" href="resources/css/off.css">
</head>
<body>
        <div id="header-layout">
        	<jsp:include page="../common/header.jsp" />

        </div>
	    <div id="container">
	        <div id="sidebar-layout">
	        	<div id="main-sidebar">
	        		<br>
		            <ul id="sidebar-ul">
		                <i class="fi fi-sr-at"></i>&nbsp;
		                <a href=""><b>Mail</b></a>
		                <br>
		                
		                &nbsp;&nbsp;&nbsp;<i class="fi fi-rr-envelope side_subtitle"></i>&nbsp;
		                <a href="" class="side_subtitle side_subtitle">메일</a>
		                <br>
		                &nbsp;&nbsp;&nbsp;<i class="fi fi-br-check side_subtitle"></i>&nbsp;
		                <a href="" class="side_subtitle">읽지않음</a>
		                <br><br>
		
		                <i class="fi fi-rr-time-check"></i>&nbsp;
		                <a href=""><b>근태관리</b></a>
		                <br><br>
		                
		                <i class="fi fi-rs-chart-tree"></i>&nbsp;
		                <a href=""><b>조직도</b></a>
		                <br><br>
		                
		                <i class="fi fi-rr-edit"></i>&nbsp;
		                <a href=""><b>전자결재</b></a>
		                <br>
		                &nbsp;&nbsp;&nbsp;<i class="fi fi-rs-inbox side_subtitle"></i>&nbsp;
		                <a href="" class="side_subtitle">기안문서함</a>
		                <br><br>
		                
		                <i class="fi fi-rs-calendar"></i>&nbsp;
		                <a href=""><b>프로젝트</b></a>
		                <br><br>
		                
		                <i class="fi fi-rs-following"></i>
		                <a href=""><b>커뮤니티</b></a>
		                <br>
		                &nbsp;&nbsp;&nbsp;<i class="fi fi-rr-heart side_subtitle"></i>&nbsp;
		                <a href="" class="side_subtitle">내 게시글</a>
		                <br><br>
		                
		                <i class="fi fi-rr-megaphone"></i>&nbsp;
		                <a href=""><b>공지사항</b></a><br><br>
		            </ul>
	        	</div>
	        </div>
	
	        <div id="content-layout">
            <div id="commute-main">
                <span class="commute_title_text">휴가 현황</span><br><br>
            </div>

       <br>
           
            <div id="calender-area">
                월별조회 <input type="month" name="month"> 
                <button>내 휴가확인</button>
                <div class="cal_top">
        <a href="#" id="movePrevMonth"><span id="prevMonth" class="cal_tit">&lt;</span></a>
        <span id="cal_top_year"></span>
        <span id="cal_top_month"></span>
        <a href="#" id="moveNextMonth"><span id="nextMonth" class="cal_tit">&gt;</span></a>
    </div>
    <div id="cal_tab" class="cal">
    </div>
 
<script type="text/javascript">
    
    var today = null;
    var year = null;
    var month = null;
    var firstDay = null;
    var lastDay = null;
    var $tdDay = null;
    var $tdSche = null;
    var jsonData = null;
    $(document).ready(function() {
        drawCalendar();
        initDate();
        drawDays();
        drawSche();
        $("#movePrevMonth").on("click", function(){movePrevMonth();});
        $("#moveNextMonth").on("click", function(){moveNextMonth();});
    });
    
    //Calendar 그리기
    function drawCalendar(){
        var setTableHTML = "";
        setTableHTML+='<table class="calendar">';
        setTableHTML+='<tr><th>SUN</th><th>MON</th><th>TUE</th><th>WED</th><th>THU</th><th>FRI</th><th>SAT</th></tr>';
        for(var i=0;i<6;i++){
            setTableHTML+='<tr height="100">';
            for(var j=0;j<7;j++){
                setTableHTML+='<td style="text-overflow:ellipsis;overflow:hidden;white-space:nowrap">';
                setTableHTML+='    <div class="cal-day"></div>';
                setTableHTML+='    <div class="cal-schedule"></div>';
                setTableHTML+='</td>';
            }
            setTableHTML+='</tr>';
        }
        setTableHTML+='</table>';
        $("#cal_tab").html(setTableHTML);
    }
    
    //날짜 초기화
    function initDate(){
        $tdDay = $("td div.cal-day")
        $tdSche = $("td div.cal-schedule")
        dayCount = 0;
        today = new Date();
        year = today.getFullYear();
        month = today.getMonth()+1;
        if(month < 10){month = "0"+month;}
        firstDay = new Date(year,month-1,1);
        lastDay = new Date(year,month,0);
    }
    
    //calendar 날짜표시
    function drawDays(){
        $("#cal_top_year").text(year);
        $("#cal_top_month").text(month);
        for(var i=firstDay.getDay();i<firstDay.getDay()+lastDay.getDate();i++){
            $tdDay.eq(i).text(++dayCount);
        }
        for(var i=0;i<42;i+=7){
            $tdDay.eq(i).css("color","red");
        }
        for(var i=6;i<42;i+=7){
            $tdDay.eq(i).css("color","blue");
        }
    }
    
    //calendar 월 이동
    function movePrevMonth(){
        month--;
        if(month<=0){
            month=12;
            year--;
        }
        if(month<10){
            month=String("0"+month);
        }
        getNewInfo();
        }
    
    function moveNextMonth(){
        month++;
        if(month>12){
            month=1;
            year++;
        }
        if(month<10){
            month=String("0"+month);
        }
        getNewInfo();
    }
    
    //정보갱신
    function getNewInfo(){
        for(var i=0;i<42;i++){
            $tdDay.eq(i).text("");
            $tdSche.eq(i).text("");
        }
        dayCount=0;
        firstDay = new Date(year,month-1,1);
        lastDay = new Date(year,month,0);
        drawDays();
        drawSche();
    }
    
    //2019-08-27 추가본
    
    //데이터 등록
    function setData(){
        jsonData = 
        {
            "2019":{
                "07":{
                    "17":"제헌절"
                }
                ,"08":{
                    "7":"칠석"
                    ,"15":"광복절"
                    ,"23":"처서"
                }
                ,"09":{
                    "13":"추석"
                    ,"23":"추분"
                }
            }
        }
    }
    
    //스케줄 그리기
    function drawSche(){
        setData();
        var dateMatch = null;
        for(var i=firstDay.getDay();i<firstDay.getDay()+lastDay.getDate();i++){
            var txt = "";
            txt =jsonData[year];
            if(txt){
                txt = jsonData[year][month];
                if(txt){
                    txt = jsonData[year][month][i];
                    dateMatch = firstDay.getDay() + i -1; 
                    $tdSche.eq(dateMatch).text(txt);
                }
            }
        }
    }
 
</script>
</body>
</html>