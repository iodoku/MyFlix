<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "auth/login.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 로그인 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "auth/login.js"); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 영역 ===== -->
<main class="container my-5 py-3">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-5">
            
            <div class="card login-card p-4 p-md-5">
                
                <h2 class="login-title mb-4">🍌 로그인</h2>

                <div class="d-flex justify-content-center">
                    <div class="login-box text-center">
                        
                        <form action="${pageContext.request.contextPath}/login" method="post" id="logForm">
                            <div class="mb-3">
                                <input type="text" class="form-control form-control-custom text-center py-2.5" id="userid" name="userid" placeholder="아이디를 입력하세요" required>
                            </div>

                            <div class="mb-3">
                                <input type="password" class="form-control form-control-custom text-center py-2.5" id="userpw" name="userpw" placeholder="비밀번호를 입력하세요" required>
                            </div>

                            <div class="mb-3">
                                <button type="submit" class="btn btn-minion-login w-100">
                                    <span>로그인</span>
                                </button>
                            </div>

                            <div class="mb-4">
                                <span id="loginErrorMsg" class="login-error-msg d-block"></span>
                            </div>
                        </form>

                        <div class="pt-2 border-top">
                            <a href="${pageContext.request.contextPath}/findidpw" class="btn btn-minion-sub px-4 py-2 mt-2 w-100">ID / PW 찾기</a>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
</main>

</body>
</html>