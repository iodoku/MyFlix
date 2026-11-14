<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "mypage/mypage.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 마이페이지 - 내 댓글 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", null); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<div class="container mypage-container p-4 p-md-5 my-5">
    
    <!-- 타이틀 영역 -->
    <div class="d-flex justify-content-between align-items-center pb-3 mb-4 border-bottom border-2 border-dark">
        <h2 class="mypage-title mb-0">마이<em>페이지</em></h2>
        <span class="badge bg-dark px-3 py-2 rounded-pill font-monospace mypage-user-badge">MYFLIX USER</span>
    </div>

    <!-- 메인 레이아웃 구역 -->
    <div class="row g-4">
        
        <!-- 좌측 사이드바 -->
        <aside class="col-md-3">
            <nav class="nav flex-column mypage-menu">
                <a class="nav-link" href="${pageContext.request.contextPath}/mp_review">내 리뷰</a>
                <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/mp_reply">내 댓글</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/mp_nick">내 정보수정</a>
            </nav>
        </aside>

        <!-- 우측 콘텐츠 영역 (댓글 목록 데이터 테이블) -->
        <main class="col-md-9">
            <div class="table-responsive">
                <table class="table align-middle custom-table table-hover">
                    <thead>
                        <tr class="small text-center">
                            <th scope="col" class="col-num">번호</th>
                            <th scope="col" class="col-title text-start">리뷰 제목</th>
                            <th scope="col" class="text-start col-content">댓글 내용</th>
                            <th scope="col" class="col-author">작성자</th>
                            <th scope="col" class="col-date">작성날짜</th>
                        </tr>
                    </thead>
                    <tbody class="small">
                        <c:choose>
                       		<c:when test="${not empty myReply}">
                       			<c:forEach var="item" items="${myReply}">
                       				<tr>
                       					<td class="col-num">${item.rp_no}</td>
                       					<td class="text-start">
                       						<a href="${pageContext.request.contextPath}/reviewdetail?r_no=${item.r_no}"
                       							class="text-decoration-none text-dark fw-bold">${item.r_title}
                       						</a>
                       					</td>


                       					<td class="text-start col-content"><c:out value="${item.rp_content}"/></td>
                       					<td class="col-author">${item.nick}</td>

                       					<td class="text-secondary col-date">${item.rp_dt}</td>
                       				</tr>
                       			</c:forEach>
                       		</c:when>
                       		<c:otherwise>
                       			<tr>
                       				<td colspan="5" class="text-center text-muted py-4">작성한 댓글이 없습니다.</td>
                       			</tr>
                       		</c:otherwise>
                       	</c:choose>
                    </tbody>
                </table>
            </div>

            <!-- 페이지네이션 -->
            <nav aria-label="Page navigation" class="mt-4">
                <ul class="pagination pagination-sm justify-content-center custom-pagination">
                	<!-- 첫 페이지로 이동  -->
                    <li class="page-item ${pagingno == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/mp_reply?page=1" aria-label="First">&laquo;</a>
                    </li>
                    
                    <!-- 이전 페이지로 이동  -->
                    <li class="page-item ${pagingno == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/mp_reply?page=${pagingno - 1}" aria-label="Previous">&lt;</a>
                    </li>
                    
                    <!-- 현재 활성화된 페이지 번호 -->
                    <c:forEach var="page" begin="${startPage}" end="${endPage}">
	                    <li class="page-item ${page == pagingno ? 'active' : ''}" aria-current="page">
	                    	<c:choose>
	                    		<c:when test="${page == pagingno}">
	                        		<span class="page-link">${page}</span>
	                        	</c:when>
	                        	<c:otherwise>
	                        		<a class="page-link" href="${pageContext.request.contextPath}/mp_reply?page=${page}">${page}</a>
	                        	</c:otherwise>
	                        </c:choose>
	                    </li>
	                </c:forEach>
	                
	                <!-- 다음 페이지로 이동 -->
                    <li class="page-item ${pagingno == totalPage ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/mp_reply?page=${pagingno + 1}" aria-label="Next">&gt;</a>
                    </li>
                    
                    <!-- 마지막 페이지로 이동 -->
                    <li class="page-item ${pagingno == totalPage ? 'disabled' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/mp_reply?page=${totalPage}" aria-label="Last">&raquo;</a>
                    </li>
                </ul>
            </nav>
        </main>

    </div>
</div>

</body>
</html>