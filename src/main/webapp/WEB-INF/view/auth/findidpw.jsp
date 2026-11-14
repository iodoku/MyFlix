<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "auth/findidpw.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | ID/PW 찾기 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "auth/findidpw.js"); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 영역 ===== -->
<main class="container my-5 py-2">
    <div class="row justify-content-center">
        <div class="col-md-11 col-lg-9">
            
            <div class="card find-card p-4 p-md-5">
                
                <h2 class="find-main-title mb-4">🔍 ID / PW 찾기</h2>

                <!-- ================= 1. ID 찾기 영역 ================= -->
                <section>
                    <h5 class="section-title mb-3">
                        <span>🍌</span> ID 찾기
                    </h5>
                    
                    <form action="아이디찾기" method="post">
                        <!-- 이메일 한 줄 정렬 -->
                        <div class="form-row-custom">
                            <label for="FIemailid" class="form-label-custom">이메일</label>
                            <input type="text" class="form-control form-control-custom input-email-id" id="FIemailid" required>
                            <span class="fw-bold text-muted">@</span>
                            <input type="text" class="form-control form-control-custom input-email-domain" id="FIemaildomain" required>
                            <select class="form-select form-select-custom select-email-domain" id="FIemailDomainSelect">
                                <option value="">직접입력</option>
                                <option value="naver.com">naver.com</option>
                                <option value="gmail.com">gmail.com</option>
                            </select>
                            <button type="button" id="FIsendEmailCode" class="btn btn-minion-sub">인증번호 발송</button>
                        </div>

                        <!-- 인증번호 한 줄 정렬 -->
                        <div class="form-row-custom">
                            <label for="FIemailcode" class="form-label-custom">인증번호</label>
                            <input type="text" class="form-control form-control-custom input-auth-code" id="FIemailcode" required>
                            <button type="button" class="btn btn-minion-main" id="sendIdBtn">ID 발송</button>
                        </div>
                    </form>
                </section>

                <hr class="minion-hr">

                <!-- ================= 2. PW 임시발급 영역 ================= -->
                <section>
                    <h5 class="section-title mb-3">
                        <span>🔑</span> PW 임시발급
                    </h5>

                    <form action="비밀번호찾기" method="post">
                        <!-- 아이디 한 줄 정렬 -->
                        <div class="form-row-custom">
                            <label for="userid" class="form-label-custom">아이디</label>
                            <input type="text" class="form-control form-control-custom input-auth-code" id="userid" required>
                        </div>

                        <!-- 이메일 한 줄 정렬 -->
                        <div class="form-row-custom">
                            <label for="FPemailid" class="form-label-custom">이메일</label>
                            <input type="text" class="form-control form-control-custom input-email-id" id="FPemailid" required>
                            <span class="fw-bold text-muted">@</span>
                            <input type="text" class="form-control form-control-custom input-email-domain" id="FPemaildomain" required>
                            <select class="form-select form-select-custom select-email-domain" id="FPemailDomainSelect">
                                <option value="">직접입력</option>
                                <option value="naver.com">naver.com</option>
                                <option value="gmail.com">gmail.com</option>
                            </select>
                            <button type="button" id="FPsendEmailCode" class="btn btn-minion-sub">인증번호 발송</button>
                        </div>

                        <!-- 인증번호 한 줄 정렬 -->
                        <div class="form-row-custom">
                            <label for="FPemailcode" class="form-label-custom">인증번호</label>
                            <input type="text" class="form-control form-control-custom input-auth-code" id="FPemailcode" required>
                            <button type="button" id="FPsendTmpPwBtn" class="btn btn-minion-main">임시번호 발송</button>
                        </div>
                    </form>
                </section>

                <!-- 하단 버튼 영역 -->
                <div class="d-flex justify-content-center mt-4 pt-3 border-top">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-minion-cancel">로그인으로 돌아가기</a>
                </div>

            </div> <!-- /.card -->
        </div>
    </div>
</main>

</body>
</html>