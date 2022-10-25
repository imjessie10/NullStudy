<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
   	import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<body>

<div class="sbwrap">

	<form name="frmListAfter" method="get" action="${pageContext.request.contextPath}/userSearch.do">
        <h3 style="color: #69c7b5" class="searchTitle">유저찾아보기</h3>
        <div class="searchLine">    
        										<!-- 이전 검색어 -->
            <input type="text" name="search" value="${search}" class="searchBar" required>
            <button type="submit" class="searchButton"><i class="fa-solid fa-magnifying-glass"></i></button>      
        
        </div>	
	</form>
	
</div>

<c:if test="${list.size() == 0}">
	<h5 class="searchW">해당 ' ${search} '에 대한 게시글이 없습니다</h5>
	<!-- 검색어에 대한 조회 데이터가 없을 때 출력 -->
</c:if>

<div class="searchTbWrap">

	<table class="searchTb">
		<thead>
			<tr class="searchTr">
				<th class="searchTh1">순번</th>
				<th class="searchTh2">닉네임</th>
				<th class="searchTh3">이메일</th>
				<th class="searchTh4">가입일</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${list!=null}">
			<c:forEach var="dto" items="${list}" varStatus="status">
				<tr class="searchTr">
					<td>${status.count}</td>
					<td class="searchTd">${dto.nickName }</td>
					<td class="searchTd">${dto.email }</td>
					<td>${dto.joinDate }</td>
				</tr>
			</c:forEach>
			</c:if>
			<c:if test="${list.size() == 0}">
				<tr class="listNullTr">
					<td colspan="4">조회된 내용이 없습니다.</td>
				<tr>
			</c:if>
		</tbody>
	</table>
</div>

<c:if test="${list.size() != 0}">
	<div class="pagingDiv">
		<c:set var="dto" value="${dto}"/>
		<!-- paging -->
		<c:if test="${dto.firstNo gt 5}">
			<a href="/project/userSearch.do?pageNum=${dto.firstNo-1}&search=${search}&selectValue=${selectValue}" font-weight:bold; class="pagingA"> << </a>
		</c:if>
		
		<c:if test="${dto.firstNo ne 1}">
			<a href="/project/userSearch.do?pageNum=${dto.pageNum-1}&search=${search}&selectValue=${selectValue}" font-weight:bold; class="pagingA"> < </a>
		</c:if>
	
		<c:forEach var="i" begin="${dto.firstNo}" end="${dto.lastNo}">
			<c:if test="${dto.pageNum eq i }">
				<a href="/project/userSearch.do?pageNum=${i }&search=${search}&selectValue=${selectValue}" style="color:red;font-weight:bold;">${i}</a>&nbsp;
			</c:if>
			<c:if test="${ not (dto.pageNum eq i) }">
				<a href="/project/userSearch.do?pageNum=${i }&search=${search}&selectValue=${selectValue}">${i }</a>&nbsp;
			</c:if>
		</c:forEach>
	
		<c:if test="${dto.lastNo ne dto.lastPage}">
			<a href="/project/userSearch.do?pageNum=${dto.pageNum+1}&search=${search}&selectValue=${selectValue}" font-weight:bold; class="pagingA"> > </a>
		</c:if>
		<c:if test="${dto.lastNo ne dto.lastPage}">
			<a href="/project/userSearch.do?pageNum=${dto.lastNo+1}&search=${search}&selectValue=${selectValue}" font-weight:bold; class="pagingA"> >> </a>
		</c:if>
	</div>
</c:if>

</body>