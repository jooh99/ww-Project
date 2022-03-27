<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>품의서 작성하기</title>

<style>
#content-layout {
	border: 1px solid lightgray;
	padding: 30px;
	margin-left: 30px;
}

#docu-header-area {
	width: 100%;
	height: 100%;
	display: inline-block;
}

#docu-basic-area {
	width: 600px;
	height: 100%;
	font-size: 16pt;
	float: left;
}

#docu-signoff-area {
	width: 500px;
	height: 100%;
	font-size: 16pt;
	float: right;
	padding-left: 25px;
}

#docuFormat {
	width: 160px;
}

.signoff-table-area {
	border: 1px solid black;
	width: 450px;
	height: 130px;
	font-size: 14pt;
	text-align: center;
}

#docu-content {
	font-size: 16pt;
}

.docu-content-textarea {
	width: 1050px;
	height: 300px;
	vertical-align: top;
	resize: none;
}

#docuTitle {
	width: 1050px;
}

#button-area {
	text-align: center;
}

#draftDocu {
	background-color: rgb(102, 164, 166);
}

.btn-custom {
	width: 130px;
	height: 40px;
	border-radius: 15px;
	border: none;
}

.sub-menu {
	margin-left: 25px;
	line-height: 40px;
}

.docu-format-base th {
	margin-right: 20px;
	text-align: left;
}

.docu-format-base td {
	margin-right: 20px;
	text-align: left;
}

#fileList {
	width: 1050px;
	height: 150px;
	vertical-align: top;
	border: 1px solid #999;
	margin-left: 50px;
	overflow: auto;
}

#fileList>li {
	list-style: none;
}

button.x-btn {
    line-height: 20px;
    vertical-align: middle;
    margin-left: 10px;
    background: #EEEE;
    transition: 0.05s;
}

button.x-btn:hover {
    background: #CCC;
}


/* 사원검색 */
input[type="search"] {
	width: 409px;
	height: 58px;
	border-radius: 15px;
	margin-top: -18px;
	margin-bottom: 33px;
	margin-left: 10px;
}

.search-member {
	width: 409px;
	height: 70px;
	text-align: center;
	margin-left: 10px;
	font-size: 13pt;
	line-height: 50px;
	border-radius: 10px;
}

#searchMemberResult {
	width: 409px;
	height: 100px;
	border: 1px solid lightgray;
	margin-left: 10px;
	overflow: auto;
}

#searchMember>div>div>div.modal-body>div.search-area>table>tbody>tr:hover
	{
	cursor: pointer;
	padding: 10px;
	background-color: #edf1f1;
	border-radius: 10px;
}

#searchMember>div>div>div.modal-body>div.search-area>table>tbody>tr>td {
	margin-left: 5px;
}

.btn-searchMember {
	width: 112px;
	height: 37px;
	background-color: rgb(102, 164, 164);
	color: white;
	border: 1px lightgray;
	border-radius: 10px;
	margin-left: 37%;
	margin-bottom: 16px;
}
</style>

</head>
<body>
	<div id="header-layout">
		<jsp:include page="../common/header.jsp" />
	</div>
	<div id="container">
		<div id="sidebar-layout">
			<div id="main-sidebar">
				<br> <i class="fi fi-rr-menu-burger"></i>&nbsp;<b>전자결재</b>
				<div class="sub-menu">
					<i class="fi fi-rr-edit"></i>&nbsp;<a href="signoffs.docu">문서작성하기</a>
				</div>
				<div class="sub-menu">
					<i class="fi fi-rr-folder"></i>&nbsp;<a href="docubox.draft">기안문서함</a>
				</div>
				<div class="sub-menu">
					<i class="fi fi-rr-folder"></i>&nbsp;<a href="">수신문서함</a>
				</div>
				<div class="sub-menu">
					<i class="fi fi-rr-folder"></i>&nbsp;<a href="">부서문서함</a>
				</div>
				<div class="sub-menu">
					<i class="fi fi-rr-folder"></i>&nbsp;<a href="">반려문서함</a>
				</div>
			</div>
		</div>

		<div id="content-layout">
			<form id="enrollForm" class="enroll-form" action="docubox.insert" enctype="multipart/form-data" method="post">
				<div id="docu-header-area">
					<div id="docu-basic-area">
						<br>
						<h2>&nbsp;문서작성</h2>
						<hr>

						<table class="docu-format-base">
							<tr>
								<th>&nbsp;형식</th>
								<td><select id="docuFormat" name="docuFormat" onchange="changeFormat(this)">
										<option value="1">기안문서</option>
										<option value="2" selected>품의서</option>
								</select>&nbsp;&nbsp;</td>
								<th>&nbsp;문서보존기간</th>
								<td>&nbsp;5년</td>
							</tr>
							<tr>
								<th></th>
								<td></td>
								<th>&nbsp;수신부서</th>
								<td style="cursor: pointer; border: 1px solid black; text-align: center" onclick="selectDeptModal()">선택</td>
							</tr>
							<tr>
								<th>&nbsp;품의일자</th>
								<td>&nbsp;${now}</td>
								<th>&nbsp;소속</th>
								<td>&nbsp;개발팀</td>
							</tr>
						</table>
					</div>

					<br>
					<div id="docu-signoff-area">
						<table class="signoff-table-area" border="1">
							<tr>
								<th rowspan="3">결재</th>
								<th>대표이사</th>
								<th>팀장</th>
								<th>사원</th>
							</tr>
							<tr>
								<td>대표자</td>
								<td style="cursor: pointer; border: 1px solid black;" onclick="selectMemberModal(1)" data-index="1">서명</td>
								<td>서명</td>
							</tr>
							<tr>
								<td>결재일</td>
								<td>결재일</td>
								<td>결재일</td>
							</tr>
						</table>
					</div>
				</div>
				<br> <br>
				<hr>
				<div id="docu-content">
					<br> <b>제목</b>&nbsp;&nbsp;<input type="text" name="docuTitle" value="" id="docuTitle" required><br>
					<br> <b>내용</b>&nbsp;
					<textarea class="docu-content-textarea" name="docuContent" required></textarea>
					<br>
					<br> <b>첨부</b>&nbsp;
					<ul id="fileList" onclick="attachFile()"></ul>
				</div>
				<br>
				<div id="button-area">
					<button class="btn-custom" type="reset" style="margin-right: 30px;">취소</button>
					<button class="btn-custom" type="button" onclick="submitDocument()" id="draftDocu">품의</button>
				</div>
			</form>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/views/signoffs/enrollMemberModal.jsp" />
	<jsp:include page="/WEB-INF/views/signoffs/enrollFormJs.jsp" />
</body>
</html>