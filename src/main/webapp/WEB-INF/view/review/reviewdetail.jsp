<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.ReplyDto" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "review/reviewdetail.css"); %>

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", "내플릭스 | 영화 상세 보기"); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "review/reviewdetail.js"); %>


<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- 본문 -->
<section class="page-wrap">
	<div class="page-panel">
    <!-- 리뷰 상세 상단 -->
		<div class="top-area">
			<div class="sidebar">
		        <!-- 영화 이미지 -->
		        <div class="thumb-box">
		          <img
		              src="${pageContext.request.contextPath}${reviewDto.m_filepath}/${reviewDto.m_filename}"
		              alt="${reviewDto.m_title}">
		        </div>
				        
		        <div class="pill-info">${reviewDto.m_title}</div>	<!-- 영화 제목 -->
		        <div class="pill-info">${reviewDto.m_dt}</div>		<!-- 영화 개봉일 -->	        
		        <div class="tag-row">
					<!-- 장르1번 정보 존재하면 실행 -->			
		        	<c:if test="${not empty reviewDto.m_genre1 and reviewDto.m_genre1 ne '선택안함'}">
						<span class="tag-pill">${reviewDto.m_genre1}</span>
					</c:if>	
					<!-- 장르2번 정보 존재하면 실행 -->
					<c:if test="${not empty reviewDto.m_genre2 and reviewDto.m_genre2 ne '선택안함'}">
						<span class="tag-pill">${reviewDto.m_genre2}</span>
					</c:if>	
					<!-- 장르3번 정보 존재하면 실행 -->
					<c:if test="${not empty reviewDto.m_genre3 and reviewDto.m_genre3 ne '선택안함'}">
						<span class="tag-pill">${reviewDto.m_genre3}</span>
					</c:if>
		        </div>

		        <!-- 리뷰 수정 / 삭제 로그인한 사용자와 리뷰 작성자가 같을 때만 표시 -->
				<div class="edit-del-row">			
				    <button type="button"
				            class="btn-edit"
				            onclick="location.href='${pageContext.request.contextPath}/reviewwrite?r_no=${reviewDto.r_no}'"
				            style="${sessionScope.userid eq reviewDto.userid
				                    ? 'visibility: visible;'
				                    : 'visibility: hidden;'}">수정
				    </button>
				    <button type="button"
				            class="btn-del"
				            onclick="deleteReview(${reviewDto.r_no})"
				            style="${sessionScope.userid eq reviewDto.userid
				                    ? 'visibility: visible;'
				                    : 'visibility: hidden;'}">삭제
				    </button>			
				</div>
			     
			    <!-- 작성자 정보 -->    
		        <div class="meta-card">								
		          <div class="meta-label">작성자</div>
		          <div class="meta-value">${reviewDto.nick}</div>
		        </div>
				     
				<!-- 작성글 평점 정보 -->      
		        <div class="meta-card">								
				    <div class="meta-label">작성글 평점</div>			
				    <div class="meta-value">	
				    	<!-- 왼쪽 기준 4번째 별 클릭 -> 평점 4.0 -->
				        <span class="review-rating-stars">			
				            <span class="rating-star ${reviewDto.r_rating >= 1 ? 'selected' : ''}">★</span>
				            <span class="rating-star ${reviewDto.r_rating >= 2 ? 'selected' : ''}">★</span>
				            <span class="rating-star ${reviewDto.r_rating >= 3 ? 'selected' : ''}">★</span>
				            <span class="rating-star ${reviewDto.r_rating >= 4 ? 'selected' : ''}">★</span>
				            <span class="rating-star ${reviewDto.r_rating >= 5 ? 'selected' : ''}">★</span>		
				        </span>
				        <span class="rating-score">${reviewDto.r_rating}</span>
				    </div>
				</div>
		
		        <!-- 작성일자 정보 -->
		        <div class="meta-card">
		          <div class="meta-label">작성일자</div>
		          <div class="meta-value">${reviewDto.r_dt}</div>
		        </div>
		      </div>
		      
		      <!-- Main Content -->
		      
		      <div class="content-area">
		        <div class="title-bar">${reviewDto.r_title}</div>		<!-- 리뷰 제목 --> 		        
		        <div class="review-body">${reviewDto.r_content}</div>	<!-- 리뷰 내용 -->
		      </div>
		    </div>
		
		
		    <!-- 댓글 영역 -->	    
		    <div class="comments-wrap">
				<%
		    	String userid = (String) session.getAttribute("userid");	/* 사용자 아이디 가져옴 */	
		    	String pageParam = request.getParameter("page");
		    	
		    	boolean isLogin = (userid != null); 						/* 로그인 여부 */
		
		    	/* Controller에서 해당 리뷰 댓글 목록을 가져옴 */
		    	ArrayList<ReplyDto> replyList =(ArrayList<ReplyDto>)request.getAttribute("replyList");
		
		    	/* 댓글 목록이 없으면 새로운 댓글 목록을 만들어줌 */
			    if (replyList == null) {replyList =new ArrayList<ReplyDto>();}
					  
			    int perPage = isLogin ? 4 : 5;	/* 페이지당 댓글 개수  로그인 O → 4개 로그인 X → 5개 */
			    int pageNo = 1;					/* 현재 페이지 설정 변수 기본값 : 1 */
			    			
			    if (pageParam != null && !pageParam.isEmpty()) 		// 페이지 정보를 받아왔으면 실행
			    {
			        try {pageNo = Integer.parseInt(pageParam);} 	// 현재 페이지 번호 설정 
			        catch (NumberFormatException e) {pageNo = 1;}	
			    }
			    
			    int totalCount = replyList.size();	/* 전체 댓글 수 */
			    int totalPage  = 0;					/* 전체 페이지 수 */
			
			    if (totalCount > 0) {totalPage =(int) Math.ceil((double) totalCount / perPage);}
		
			    /* 페이지 번호가 잘못된 경우*/
			    if (pageNo < 1) { pageNo = 1; }
			    if (totalPage > 0 && pageNo > totalPage) {pageNo = totalPage;}
		
			    int startIndex = (pageNo - 1) * perPage; 					/* 댓글 시작 위치 */
		    	int endIndex = Math.min(startIndex + perPage,totalCount); 	/* 댓글 마지막 위치 */
				%>
		
		
		      	<!--  댓글 목록 -->
				<%
			    for (int i = startIndex;i < endIndex; i++) 
			    {
			        ReplyDto reply = replyList.get(i);	// 댓글 목록에서 해당 페이지의 댓글 목록을 가져옴 
			        
			        /* 현재 로그인 사용자가 댓글 작성자인지 확인 */
			        boolean isWriter = userid != null && userid.equals(reply.getUserid());
					%>
					<!-- 댓글 영역 --> 
			      	<div class="comment-item" id="comment-<%= reply.getRp_no() %>">
		          		<div class="comment-card">										
		            		<div class="comment-head">										
		             		<!-- 댓글 평점 : 평점 수치만큼 별 색상 활성화 3.0 -> 별 3개 노란색 -->
						    <span class="display-stars">
							    <span class="display-star <%= reply.getRp_rating() >= 1 ? "active" : "" %>">★</span>
							    <span class="display-star <%= reply.getRp_rating() >= 2 ? "active" : "" %>">★</span>
							    <span class="display-star <%= reply.getRp_rating() >= 3 ? "active" : "" %>">★</span>
							    <span class="display-star <%= reply.getRp_rating() >= 4 ? "active" : "" %>">★</span>
							    <span class="display-star <%= reply.getRp_rating() >= 5 ? "active" : "" %>">★</span>							
							</span>
					
							<span class="reply-score"><%= reply.getRp_rating() %></span>	<!-- 평점 정보 -->					
							<span class="c-author"><%= reply.getNick() %></span>			<!-- 평점 작성자 정보 -->	
								
				            <!-- 댓글 작성자 본인에게만 수정 / 삭제 버튼 표시 -->
				            <button type="button" 
				            		class="btn-comment-edit"
				                	onclick="openCommentEdit(<%= reply.getRp_no() %>)"
				                	<%= isWriter ? "" : "style='visibility:hidden;'" %>>수정하기
				            </button>
		            
				            <button	type="button"
				                	class="btn-comment-delete"
				                	onclick="deleteComment(<%= reply.getRp_no() %>)"
				                	<%= isWriter ? "" : "style='visibility:hidden;'" %>>삭제하기
				           	</button>
		
		            		<span class="c-date"><%= reply.getRp_dt() %></span>	<!-- 작성일 정보 -->
						</div>
						
						<!-- 댓글 내용 정보 -->
				        <p class="comment-text" id="comment-text-<%= reply.getRp_no() %>"><%= reply.getRp_content() %></p>
					</div>


			        <!-- 댓글 수정 영역 작성자 본인에게만 표시 -->
			
					<%
			        if (isWriter) 
			        {
						%>
						<div class="comment-edit">
					        <!-- 수정용 별점 -->
					        <div class="rating">
							    <span class="edit-stars"
							          data-rp-no="<%= reply.getRp_no() %>"
							          data-rating="<%= reply.getRp_rating() %>">
							
							        <span class="edit-star" data-rating="1">★</span>
							        <span class="edit-star" data-rating="2">★</span>
							        <span class="edit-star" data-rating="3">★</span>							
							        <span class="edit-star" data-rating="4">★</span>							
							        <span class="edit-star" data-rating="5">★</span>							
							    </span>
							
							    <span class="score"
							          id="edit-rating-<%= reply.getRp_no() %>">
							        <%= reply.getRp_rating() %>
							    </span>						
							</div>
		         		
							<!-- 🎯 textarea와 바이트 카운터를 묶는 래퍼 -->
					        <div class="comment-input-wrap">
					            <!-- 수정 내용 -->
					            <textarea id="comment-edit-textarea-<%= reply.getRp_no() %>"><%= reply.getRp_content() %></textarea>
					
					            <!-- 수정용 바이트 카운터 -->
					            <div class="byte-counter-box">
					                <p id="byteWarning-<%= reply.getRp_no() %>" class="byte-warning-text" style="display: none; color: #dc3545; font-size: 0.8rem; margin: 0; font-weight: bold;">
					                    4000바이트 초과
					                </p>
					                <div class="byte-count-text" style="font-size: 0.8rem; color: #6c757d; margin-left: auto;">
					                    <span id="currentByte-<%= reply.getRp_no() %>" class="byte-current">0</span> / 4000 Byte
					                </div>
					            </div>
					        </div>
					        <!-- 수정 버튼 -->
					        <div class="edit-actions">
					            <button	type="button"
					                	class="btn-edit-save"
					                	onclick="saveCommentEdit(<%= reply.getRp_no() %>)">작성완료
								</button>
				
					            <button	type="button"
					                	class="btn-edit-cancel"
					                	onclick="cancelCommentEdit(<%= reply.getRp_no() %>)">취소하기
					            </button>
				          	</div>
		        		</div>
						<%
			        }
					%>
				</div>
				<%
			}
			%>
		
			<!-- 댓글이 없는 경우  -->
			<%
			if (totalCount == 0) 
			{
			%>
				<div class="no-comment">등록된 댓글이 없습니다.</div>
			<%
		    }
			%>
			<!-- 댓글 작성 로그인한 경우에만 표시 -->
			<%
			if (isLogin) 
			{
			%>
				<div class="comment-new">

				    <!-- 댓글 작성 별점 -->
				    <div class="rating">
				
				        <span class="reply-stars">		
				            <span class="reply-star selected" data-rating="1">★</span>
							<span class="reply-star selected" data-rating="2">★</span>
							<span class="reply-star selected" data-rating="3">★</span>
				            <span class="reply-star" data-rating="4">★</span>
				            <span class="reply-star" data-rating="5">★</span>				
				        </span>
				
				        <span class="score" id="reply-rating">3.0</span>			
				    </div>
				
					<!-- 🎯 textarea와 바이트 수를 세로로 묶는 컨테이너 -->
			        <div class="comment-input-wrap">
			            <!-- 댓글 입력 -->
			            <textarea id="reply-content"
			                      placeholder="영화 리뷰가 제 맘에 무척 와닿았습니다..."></textarea>
			                  
			            <div class="byte-counter-box">
			                <!-- 평소에는 숨겨져 있다가 4000바이트를 넘는 순간 나타날 알림 문구 -->
			                <p id="byteWarning" class="byte-warning-text" style="display: none; color: #dc3545; font-size: 0.8rem; margin: 0; font-weight: bold;">
			                    4000바이트 초과
			                </p>
			                <div class="byte-count-text" style="font-size: 0.8rem; color: #6c757d; margin-left: auto;">
			                    <span id="currentByte" class="byte-current">0</span> / 4000 Byte
			                </div>
			            </div>
			        </div>
				
				    <!-- 작성 버튼 -->
				    <button type="button"
				            class="btn-comment-submit"
				            onclick="insertComment()">
				        작성하기
				    </button>
				</div>
			<%
			}
			%>
			
			<!-- 댓글 페이징 -->
			<%
			if (totalPage > 1)
			{
			%>
			    <div class="pagination-minion">
			
			        <!-- 이전 버튼 페이지 번호 -1 -->
			        <%
			        if (pageNo > 1)
			        {
			        	%>
			            <a href="${pageContext.request.contextPath}/reviewdetail?r_no=${reviewDto.r_no}&page=<%= pageNo - 1 %>">
			                &laquo;
			            </a>
			        	<%
			        }
			        %>
			
			        <!-- 페이지 번호 선택 버튼 -->
			        <%
			        for (int p = 1; p <= totalPage; p++)
			        {
			        	%>
			            <a href="${pageContext.request.contextPath}/reviewdetail?r_no=${reviewDto.r_no}&page=<%= p %>"
			               class="<%= (p == pageNo) ? "active" : "" %>">
			                <%= p %>
			            </a>
			        	<%
			        }
			        %>
			
			        <!-- 다음 버튼 페이지 번호 +1 -->
			        <%
			        if (pageNo < totalPage)
			        {
			        	%>
			            <a href="${pageContext.request.contextPath}/reviewdetail?r_no=${reviewDto.r_no}&page=<%= pageNo + 1 %>">
			                &raquo;
			            </a>
			        	<%
			        }
			        %>
			    </div>
				<%
			}
			%>
    	</div>
	</div>
</section>


</body>
</html>