<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "mypage/mypage.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 마이페이지 - 비밀번호 변경 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "mypage/mp_pw.js"); %>

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
                <a class="nav-link" href="${pageContext.request.contextPath}/mp_reply">내 댓글</a>
                <a class="nav-link active" aria-current="page">내 정보수정</a>
                
                <!-- 하위 소메뉴 리스트 -->
                <ul class="submenu-list">
                    <li><a href="${pageContext.request.contextPath}/mp_nick">닉네임 변경</a></li>
                    <li class="current"><a href="${pageContext.request.contextPath}/mp_pw">비밀번호 변경</a></li>
                    <li><a href="${pageContext.request.contextPath}/mp_email">이메일 변경</a></li>
                </ul>
            </nav>
        </aside>

        <!-- 우측 콘텐츠 영역 (비밀번호 변경 폼 카드) -->
        <main class="col-md-9">
            <div class="card border-2 border-dark rounded-4 shadow-sm overflow-hidden">
                <div class="card-body p-4 p-md-5">
                    <h3 class="fs-4 fw-bold mb-4 form-card-title">비밀번호 변경</h3>
                    
                    <form id="pwForm" action="${pageContext.request.contextPath}/mp_pw_change" method="post" class="form-pw-wrap">
                        
                        <!-- 1. 현재 비밀번호 입력 -->
                        <div class="mb-4">
                            <label for="currentPw" class="form-label small fw-bold text-dark mb-2">현재 비밀번호를 입력하세요</label>
                            <div class="input-group">
                                <input type="password" id="currentPw" name="currentPw" class="form-control form-control-custom py-2" placeholder="현재 비밀번호 입력" required>
                                <button id="btnViewCurrent" class="btn btn-myflix-volume px-4" type="button" onclick="togglePasswordVisibility(this)">보기</button>
                            </div>
                        </div>

                        <!-- 2. 변경할 새 비밀번호 입력 -->
                        <div class="mb-4">
                            <label for="newPw" class="form-label small fw-bold text-dark mb-2">변경하려는 비밀번호를 입력하세요</label>
                            <div class="input-group">
                                <input type="password" id="newPw" name="newPw" class="form-control form-control-custom py-2" placeholder="새 비밀번호 입력" required>
                                <button id="btnViewNew" class="btn btn-myflix-volume px-4" type="button" onclick="togglePasswordVisibility(this)">보기</button>
                            </div>
                            <div id="newPwCheckMsg" class="form-text mt-2 small">
                            </div>
                        </div>

                        <!-- 3. 새 비밀번호 확인 입력 -->
                        <div class="mb-4">
                            <label for="newPwConfirm" class="form-label small fw-bold text-dark mb-2">비밀번호 확인</label>
                            <div class="input-group">
                                <input type="password" id="newPwConfirm" class="form-control form-control-custom py-2" placeholder="새 비밀번호 재입력" required>
                                <button id="btnViewConfirm" class="btn btn-myflix-volume px-4" type="button" onclick="togglePasswordVisibility(this)">보기</button>
                            </div>
                            <div id="pwConfirmMsg" class="form-text mt-2 small">
                            </div>
                        </div>

                        <!-- 최종 변경 버튼 -->
                        <div class="text-center pt-3">
                            <button id="pwSubmitBtn" type="button" class="btn btn-myflix-volume px-4">변경하기</button>
                        </div>

                    </form>
                </div>
            </div>
        </main>

    </div>
</div>

</body>
</html>