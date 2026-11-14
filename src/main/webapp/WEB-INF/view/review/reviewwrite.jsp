<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "review/reviewwrite.css"); %> 

<%-- 타이틀 이름 (모드에 따라 분기 reviewcontroller doget 에서 update mode, write mode) --%>
<c:set var="pageTitle" value="${mode eq 'update' ? '내플릭스 | 영화 리뷰 수정' : '내플릭스 | 영화 리뷰 작성'}" />
<% request.setAttribute("titlename", pageContext.getAttribute("pageTitle")); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "review/reviewwrite.js"); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 영역 ===== -->
<main class="container my-5 py-2">
    <div class="write-card">
        
        <!-- 상단 헤더 타이틀 -->
        <h2 class="write-title mb-4">
            <c:choose>
                <c:when test="${mode eq 'update'}">🎬 영화 리뷰 수정</c:when>
                <c:otherwise>🎬 영화 리뷰 작성</c:otherwise>
            </c:choose>
        </h2>

        <form action="${pageContext.request.contextPath}/reviewwrite" method="post" id="writeForm" enctype="multipart/form-data">
            
            <!-- 수정 모드일 때 필요한 히든 필드 -->
            <c:if test="${mode eq 'update'}">
                <input type="hidden" name="mode" value="update">
                <input type="hidden" name="r_no" value="${review.r_no}">
                <!-- 기존 파일 유지용 히든 필드 -->
                <input type="hidden" name="existing_file" value="${review.m_filename}">
                <input type="hidden" name="existing_path" value="${review.m_filepath}">
            </c:if>

            <!-- 상단 영역 -->
            <div class="row g-4 mb-4">
                <!-- 왼쪽: 이미지 첨부 -->
                <div class="col-md-4 d-flex flex-column justify-content-start">
                    <label for="imageUpload" class="image-upload-box" id="imagePreview">
                        <!-- 수정모드 + 기존파일 있음 : 포스터/이미지 첨부 문구 표출(x)
                             작성모드 or 파일 없음 : 포스터/이미지 첨부 문구 표출(o) -->
                        <span class="upload-placeholder ${mode eq 'update' and not empty review.m_filename ? 'd-none' : ''}">
                            <span class="image-upload-icon">🖼️</span>
                            <span>포스터/이미지 첨부</span>
                        </span>
                        <!-- 수정모드 + 기존파일 있음 : 포스터 이미지 화면에 표출
                        	 작성모드 : 이미지태그 숨김 -->
                        <img id="previewImg" 
                             class="preview-image ${mode eq 'update' and not empty review.m_filename ? '' : 'd-none'}" 
                             src="${mode eq 'update' and not empty review.m_filename ? pageContext.request.contextPath.concat(review.m_filepath).concat('/').concat(review.m_filename) : ''}" 
                             alt="포스터 미리보기">
                    </label>
                    <input type="file" id="imageUpload" name="m_filename" accept="image/jpeg, image/png, image/webp, image/gif" class="d-none">
                </div>

                <!-- 오른쪽: 제목, 영화제목, 개봉일자, 장르 선택 -->
                <div class="col-md-8 d-flex flex-column justify-content-between gap-3">
                    <div>
                        <input type="text" id="r_title" name="r_title" class="form-control title-input-box" 
                               placeholder="리뷰 제목을 입력하세요(200자 이내)" maxlength="200" value="${review.r_title}" required>
                    </div>

                    <div class="row g-2">
                        <div class="col-6">
                            <input type="text" id="m_title" name="m_title" class="form-control input-pill-custom" 
                                   placeholder="영화제목(200자 이내)" maxlength="200" value="${review.m_title}" required>
                        </div>
                        <div class="col-6">
                            <input type="text" id="m_dt" name="m_dt" class="form-control input-pill-custom" 
                                   placeholder="개봉일자 (예: 20240501)" maxlength="10" value="${review.m_dt}" required>
                        </div>
                    </div>

                    <div class="genre-label-pill">
                        🍿 장르 선택
                    </div>

                    <div class="row g-2">
                        <!-- 장르 1 -->
                        <div class="col-4">
                            <select id="m_genre1" name="m_genre1" class="form-select select-pill-custom genre-select">
                                <option value="선택안함" ${empty review.m_genre1 or review.m_genre1 eq '선택안함' ? 'selected' : ''}>선택안함</option>
                                <option value="액션" ${review.m_genre1 eq '액션' ? 'selected' : ''}>액션</option>
                                <option value="코미디" ${review.m_genre1 eq '코미디' ? 'selected' : ''}>코미디</option>
                                <option value="드라마" ${review.m_genre1 eq '드라마' ? 'selected' : ''}>드라마</option>
                                <option value="로맨스" ${review.m_genre1 eq '로맨스' ? 'selected' : ''}>로맨스</option>
                                <option value="스릴러" ${review.m_genre1 eq '스릴러' ? 'selected' : ''}>스릴러</option>
                                <option value="공포" ${review.m_genre1 eq '공포' ? 'selected' : ''}>공포</option>
                                <option value="SF" ${review.m_genre1 eq 'SF' ? 'selected' : ''}>SF</option>
                                <option value="판타지" ${review.m_genre1 eq '판타지' ? 'selected' : ''}>판타지</option>
                                <option value="애니메이션" ${review.m_genre1 eq '애니메이션' ? 'selected' : ''}>애니메이션</option>
                            </select>
                        </div>
                        <!-- 장르 2 -->
                        <div class="col-4">
                            <select id="m_genre2" name="m_genre2" class="form-select select-pill-custom">
                                <option value="선택안함" ${empty review.m_genre2 or review.m_genre2 eq '선택안함' ? 'selected' : ''}>선택안함</option>
                                <option value="액션" ${review.m_genre2 eq '액션' ? 'selected' : ''}>액션</option>
                                <option value="코미디" ${review.m_genre2 eq '코미디' ? 'selected' : ''}>코미디</option>
                                <option value="드라마" ${review.m_genre2 eq '드라마' ? 'selected' : ''}>드라마</option>
                                <option value="로맨스" ${review.m_genre2 eq '로맨스' ? 'selected' : ''}>로맨스</option>
                                <option value="스릴러" ${review.m_genre2 eq '스릴러' ? 'selected' : ''}>스릴러</option>
                                <option value="공포" ${review.m_genre2 eq '공포' ? 'selected' : ''}>공포</option>
                                <option value="SF" ${review.m_genre2 eq 'SF' ? 'selected' : ''}>SF</option>
                                <option value="판타지" ${review.m_genre2 eq '판타지' ? 'selected' : ''}>판타지</option>
                                <option value="애니메이션" ${review.m_genre2 eq '애니메이션' ? 'selected' : ''}>애니메이션</option>
                            </select>
                        </div>
                        <!-- 장르 3 -->
                        <div class="col-4">
                            <select id="m_genre3" name="m_genre3" class="form-select select-pill-custom">
                                <option value="선택안함" ${empty review.m_genre3 or review.m_genre3 eq '선택안함' ? 'selected' : ''}>선택안함</option>
                                <option value="액션" ${review.m_genre3 eq '액션' ? 'selected' : ''}>액션</option>
                                <option value="코미디" ${review.m_genre3 eq '코미디' ? 'selected' : ''}>코미디</option>
                                <option value="드라마" ${review.m_genre3 eq '드라마' ? 'selected' : ''}>드라마</option>
                                <option value="로맨스" ${review.m_genre3 eq '로맨스' ? 'selected' : ''}>로맨스</option>
                                <option value="스릴러" ${review.m_genre3 eq '스릴러' ? 'selected' : ''}>스릴러</option>
                                <option value="공포" ${review.m_genre3 eq '공포' ? 'selected' : ''}>공포</option>
                                <option value="SF" ${review.m_genre3 eq 'SF' ? 'selected' : ''}>SF</option>
                                <option value="판타지" ${review.m_genre3 eq '판타지' ? 'selected' : ''}>판타지</option>
                                <option value="애니메이션" ${review.m_genre3 eq '애니메이션' ? 'selected' : ''}>애니메이션</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 본문 입력 영역 -->
            <div class="mb-4">
                <textarea id="r_content" name="r_content" class="review-textarea" placeholder="자유롭게 영화 리뷰를 작성해 보세요..." required>${review.r_content}</textarea>
                
                <!-- 실시간 바이트 표시 UI -->
			    <div class="byte-counter-box">
			        <p id="byteWarning" class="byte-warning-text">최대 4000바이트를 초과하여 이후 내용은 입력되지 않습니다.</p>
			        <div class="byte-count-text">
			            <span id="currentByte" class="byte-current">0</span> / 4000 Byte
			        </div>
			    </div>
            </div>

            <!-- 하단 버튼 영역 -->
            <div class="row g-3 pt-3 border-top">
                <div class="col-6">
                    <button type="button" id="backBtn" class="btn-minion-cancel">뒤로가기</button>
                </div>
                <div class="col-6">
                    <button type="submit" class="btn-minion-main">
                        <span>${mode eq 'update' ? '수정하기' : '작성하기'}</span>
                    </button>
                </div>
            </div>

        </form>
        
    </div>
</main>

</body>
</html>