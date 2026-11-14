<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "mypage/mypage.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 마이페이지 - 이메일 변경 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "mypage/mp_email.js"); %>

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
                    <li><a href="${pageContext.request.contextPath}/mp_pw">비밀번호 변경</a></li>
                    <li class="current"><a href="${pageContext.request.contextPath}/mp_email">이메일 변경</a></li>
                </ul>
            </nav>
        </aside>

        <!-- 우측 콘텐츠 영역 (이메일 변경 폼 카드) -->
        <main class="col-md-9">
            <div class="card border-2 border-dark rounded-4 shadow-sm overflow-hidden">
                <div class="card-body p-4 p-md-5">
                    <h3 class="fs-4 fw-bold mb-4 form-card-title">이메일 변경</h3>
                    
                    <form id="emailForm" action="${pageContext.request.contextPath}/mp_email_change" method="post">
                        <!-- 현재 이메일 노출 구역 -->
                        <div class="mb-4 p-3 bg-light rounded-3 border-start border-4 border-warning form-email-wrap">
                            <span class="text-secondary small d-block fw-bold mb-1">현재 연동된 이메일</span>
                            <span class="fs-5 fw-bold text-dark">${email}</span>
                        </div>

                        <!-- 1. 새로운 이메일 입력 및 인증 요청 -->
                        <div class="mb-4 form-email-wrap">
                            <label class="form-label small fw-bold text-dark mb-2">변경하려는 이메일을 입력하세요</label>
                            
                            <div class="row g-2 align-items-center">
                                <!-- 이메일 아이디 -->
                                <div class="col-4">
                                    <input type="text" id="emailid" name="emailid" class="form-control form-control-custom py-2" placeholder="이메일 아이디" required aria-label="이메일 아이디">
                                </div>
                                <!-- 골뱅이 기호 (@) -->
                                <div class="col-auto email-at-sign">@</div>
                                <!-- 이메일 도메인 입력 -->
                                <div class="col-4">
                                    <input type="text" id="emailDomain" name="emaildomain" class="form-control form-control-custom py-2" placeholder="도메인 입력" required aria-label="이메일 도메인">
                                </div>
                                <!-- 도메인 선택 드롭다운 -->
                                <div class="col-3">
                                    <select id="domainSelect" class="form-select form-select-custom py-2 email-select-box">
                                        <option value="direct" selected>직접 입력</option>
                                        <option value="naver.com">naver.com</option>
                                        <option value="gmail.com">gmail.com</option>
                                        <option value="nate.com">nate.com</option>
                                    </select>
                                </div>
                                <!-- 인증번호 발송 버튼 -->
                                <div class="col-12 mt-3">
                                    <button id="sendBtn" class="btn btn-myflix-volume py-2 w-100" type="button">인증번호 발송 ✉️</button>
                                </div>
                            </div>
                        </div>

                        <!-- 2. 인증번호 확인 및 최종 변경 -->
                        <div class="mb-2 form-email-wrap">
                            <label for="emailcodemp" class="form-label small fw-bold text-dark mb-2">인증번호</label>
                            <div class="input-group">
                                <input type="text" id="emailcodemp" name="emailcodemp" class="form-control form-control-custom py-2" placeholder="인증번호 6자리 입력" required>
                                <button id="emailSubmitBtn" class="btn btn-myflix-volume px-4" type="button">변경하기</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>

    </div>
</div>

</body>
</html>