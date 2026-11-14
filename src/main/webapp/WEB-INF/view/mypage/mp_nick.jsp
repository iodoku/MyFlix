<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "mypage/mypage.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 마이페이지 - 닉네임 변경 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "mypage/mp_nick.js"); %>

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
                    <li class="current"><a href="${pageContext.request.contextPath}/mp_nick">닉네임 변경</a></li>
                    <li><a href="${pageContext.request.contextPath}/mp_pw">비밀번호 변경</a></li>
                    <li><a href="${pageContext.request.contextPath}/mp_email">이메일 변경</a></li>
                </ul>
            </nav>
        </aside>

        <!-- 우측 콘텐츠 영역 (닉네임 변경 폼 카드) -->
        <main class="col-md-9">
            <div class="card border-2 border-dark rounded-4 shadow-sm overflow-hidden">
                <div class="card-body p-4 p-md-5">
                    <h3 class="fs-4 fw-bold mb-4 form-card-title">닉네임 변경</h3>
                    
                    <form id="nickForm" action="${pageContext.request.contextPath}/mp_nick_change" method="post">
                        <!-- 현재 회원 정보 노출 구역 -->
                        <div class="mb-4 p-3 bg-light rounded-3 border-start border-4 border-warning">
                            <span class="text-secondary small d-block fw-bold mb-1">현재 사용 중인 닉네임</span>
                            <span class="fs-5 fw-bold text-dark">${nick}님</span>
                        </div>

                        <!-- 변경 입력 영역 -->
                        <div class="mb-3">
                            <label for="newNick" class="form-label small fw-bold text-dark mb-2">변경하려는 닉네임을 입력하세요</label>
                            
                            <!-- 중복확인 버튼 -->
                            <div class="input-group form-nick-wrap">
                                <input type="text" id="newNick" name="newNick" class="form-control form-control-custom py-2" placeholder="새 닉네임 입력" required>
                                <button id="btnSubmitNick" class="btn btn-myflix-volume px-4" type="button">변경하기</button>
                            </div>
                            
                            <!-- 피드백 메시지 그룹 -->
                            <div id="nickCheckMsg" class="form-text mt-3 small d-flex align-items-center">
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