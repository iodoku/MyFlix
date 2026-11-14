<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "review/reviewlist.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 영화 리뷰 목록 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", null); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 영역 ===== -->
<main class="container review-list-page">
    
    <!-- 메인 컨테이너 영역 -->
    <div class="list-main-panel">

        <h2 class="list-main-title mb-4">🍿 영화 리뷰 목록</h2>

        <!-- 검색 / 필터 구역 -->
        <div class="search-box-inner mb-4">

		    <form action="${pageContext.request.contextPath}/reviewlist"
		          method="get"
		          class="row g-2 align-items-center">
		
		        <div class="col-6 col-md-2 ms-auto">
		
		            <select name="searchType"
		                    class="form-select select-pill-custom">
		
		                <option value="title"
		                    ${param.searchType == 'title' ? 'selected' : ''}>
		                    리뷰제목
		                </option>
		
		                <option value="movieTitle"
		                    ${param.searchType == 'movieTitle' ? 'selected' : ''}>
		                    영화제목
		                </option>
		
		                <option value="writer"
		                    ${param.searchType == 'writer' ? 'selected' : ''}>
		                    작성자
		                </option>
		
		            </select>
		
		        </div>
		
		        <div class="col-8 col-md-5">
		
		            <input type="text"
		                   name="keyword"
		                   class="form-control input-pill-custom"
		                   placeholder="검색어를 입력하세요"
		                   value="${param.keyword}">
		
		        </div>
		
		        <div class="col-4 col-md-1">
		
		            <button type="submit"
		                    class="btn-minion-sub w-100">
		                검색
		            </button>
		
		        </div>
		
		    </form>
		
		</div>

        <!-- 카드 6개 배치 (3열 2행) -->
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
        
		    <c:forEach var="review" items="${reviewList}">
			    <div class="col">
			
			        <a href="${pageContext.request.contextPath}/reviewdetail?r_no=${review.r_no}"
			           class="review-card">
			
			            <div class="poster-wrapper">
			                <div class="poster-placeholder">
		
			                    <c:choose>
			
			                        <c:when test="${not empty review.m_filename}">
			                            <img src="${pageContext.request.contextPath}${review.m_filepath}/${review.m_filename}"
			                                 alt="${review.m_title}"
			                                 class="poster-img">
			                        </c:when>
			
			                        <c:otherwise>
			                            <span style="font-size: 2.3rem;">🎬</span>
			                            <span>${review.m_title}</span>
			                        </c:otherwise>
			
			                    </c:choose>
			
			                </div>
			            </div>
			
			            <div class="review-card-body">
			                <div>		
			                    <h3 class="review-card-title">${review.r_title}</h3>
			                    <div class="movie-info-sub">🎬 ${review.m_title} · ${review.m_dt} 개봉</div>
			                    <div class="genre-badge-group">
			                    	<c:if test="${not empty review.m_genre1 and review.m_genre1 ne '선택안함'}">			
			                        	<span class="genre-pill">${review.m_genre1}</span>
			                        </c:if>
			                        <c:if test="${not empty review.m_genre2 and review.m_genre2 ne '선택안함'}">
			                            <span class="genre-pill">${review.m_genre2}</span>
			                        </c:if>
			                        <c:if test="${not empty review.m_genre3 and review.m_genre3 ne '선택안함'}">
			                            <span class="genre-pill">${review.m_genre3}</span>
			                        </c:if>
			                    </div>		
			                </div>
			
			                <div class="card-meta-footer">
			
			                    <span>
			                        👤 ${review.nick}
			                    </span>
			
			                    <span class="meta-like">
			                        게시글평점 ★ ${review.r_rating}
			                    </span>
			
			                </div>
			
			            </div>
			
			        </a>
			
			    </div>
			
			</c:forEach>
		
		</div>

        <!-- 하단 페이징 영역 -->
        <div class="pagination-minion">

		    <!-- 이전 페이지 그룹 -->
		    <c:if test="${startPage > 1}">
			    <a href="${pageContext.request.contextPath}/reviewlist?page=${startPage - 1}&searchType=${searchType}&keyword=${keyword}">
			        &laquo;
			    </a>
			</c:if>
			
			<!-- 페이지 번호 5개 -->
			<c:forEach var="page" begin="${startPage}" end="${endPage}">
			    <a href="${pageContext.request.contextPath}/reviewlist?page=${page}&searchType=${searchType}&keyword=${keyword}"
			       class="${page == pagingno ? 'active' : ''}">
			        ${page}
			    </a>
			</c:forEach>
			
			<!-- 다음 페이지 그룹 -->
			<c:if test="${endPage < totalPage}">
			    <a href="${pageContext.request.contextPath}/reviewlist?page=${endPage + 1}&searchType=${searchType}&keyword=${keyword}">
			        &raquo;
			    </a>
			</c:if>
		
		</div>

    </div> <!-- /.list-main-panel -->

</main>
</body>
</html>