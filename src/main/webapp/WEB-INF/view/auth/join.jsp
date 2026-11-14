<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "auth/join.css"); %>

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 회원가입 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", "auth/join.js"); %>

<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 영역 ===== -->

<main class="container my-5 py-2">

    <div class="row justify-content-center">

        <div class="col-md-10 col-lg-8">

            <!-- 카드 박스 구역 -->
            <div class="card join-card p-4 p-md-5">

                <h2 class="join-title mb-4">🍌 회원가입</h2>

                <form action="${pageContext.request.contextPath}/joinfullcheck" method="post" id="joinForm">

                    <!-- 1. 아이디 -->
                    <div class="mb-3">

                        <div class="d-flex align-items-center flex-wrap gap-2">

                            <label for="userid"
                                   class="form-label-custom mb-0">
                                아이디
                            </label>

                            <input type="text"
                                   class="form-control form-control-custom input-w-sm"
                                   id="userid"
                                   name="userid"
                                   required>

                            <button type="button"
                                    id="idCheckBtn"
                                    class="btn btn-minion-sub">
                                중복검사
                            </button>

                            <span id="idMsg"
                                  class="msg-text ms-2">
                            </span>

                        </div>

                    </div>


                    <!-- 2. 비밀번호 -->
                    <div class="mb-3">

                        <div class="d-flex align-items-center gap-2">

                            <label for="userpw"
                                   class="form-label-custom mb-0">
                                비밀번호
                            </label>

                            <input type="password"
                                   class="form-control form-control-custom input-w-sm"
                                   id="userpw"
                                   name="userpw"
                                   required>

                            <button type="button"
                                    id="viewpw1"
                                    class="btn btn-minion-sub">
                                비밀번호 보기
                            </button>

                        </div>

                        <div class="form-msg-indent">

                            <span id="pwMsg"
                                  class="msg-text mt-1 d-block">
                            </span>

                        </div>

                    </div>


                    <!-- 3. 비밀번호 확인 -->
                    <div class="mb-3">

                        <div class="d-flex align-items-center gap-2">

                            <label for="userpwConfirm"
                                   class="form-label-custom mb-0">
                                비밀번호 확인
                            </label>

                            <input type="password"
                                   class="form-control form-control-custom input-w-sm"
                                   id="userpwConfirm"
                                   required>

                            <button type="button"
                                    id="viewpw2"
                                    class="btn btn-minion-sub">
                                비밀번호 보기
                            </button>

                        </div>

                        <div class="form-msg-indent">

                            <span id="pwConfirmMsg"
                                  class="msg-text mt-1 d-block">
                            </span>

                        </div>

                    </div>


                    <!-- 4. 이메일 -->
                    <div class="mb-3">

                        <div class="d-flex align-items-center flex-wrap gap-2">

                            <label for="emailId"
                                   class="form-label-custom mb-0">
                                이메일
                            </label>

                            <input type="text"
                                   class="form-control form-control-custom input-email"
                                   id="emailid"
                                   name="emailid"
                                   required>

                            <span class="fw-bold text-muted">@</span>

                            <input type="text"
                                   class="form-control form-control-custom input-email"
                                   id="emaildomain"
                                   name="emaildomain"
                                   required>

                            <select class="form-select form-select-custom select-email"
                                    id="emailDomainSelect">

                                <option value="">직접입력</option>
                                <option value="naver.com">naver.com</option>
                                <option value="gmail.com">gmail.com</option>

                            </select>

                            <button type="button"
                                    id="sendEmailCode"
                                    class="btn btn-minion-sub text-nowrap">
                                인증번호 발송
                            </button>
                            <!-- <div id="sendMailMsg"></div> -->

                        </div>

                    </div>


                    <!-- 5. 이메일 인증번호 -->
                    <div class="mb-3">

                        <div class="d-flex align-items-center gap-2">

                            <label for="emailcode"
                                   class="form-label-custom mb-0">
                                이메일 인증번호
                            </label>

                            <input type="text"
                                   class="form-control form-control-custom input-w-sm"
                                   id="emailcode"
                                   name="emailcode">

                            <button type="button"
                                    id="verifyEmailCode"
                                    class="btn btn-minion-sub">
                                인증하기
                            </button>

                        </div>

                    </div>


                    <!-- 6. 닉네임 -->
                    <div class="mb-4">

                        <div class="d-flex align-items-center flex-wrap gap-2">

                            <label for="nick"
                                   class="form-label-custom mb-0">
                                닉네임
                            </label>

                            <input type="text"
                                   class="form-control form-control-custom input-w-sm"
                                   id="nick"
                                   name="nick"
                                   required>
                                   
                            <button type="button"
                                    id="nickCheckBtn"
                                    class="btn btn-minion-sub">
                                중복검사
                            </button>
                            
                            <span id="nickMsg"
                                  class="msg-text ms-2">
                            </span>

                        </div>

                    </div>


                    <!-- 7. 하단 버튼 영역 -->
                    <div class="d-flex justify-content-center gap-3 mt-5 pt-3 border-top">

                        <a id = "cancelBtn"
                           class="btn btn-minion-cancel">
                            취소
                        </a>

                        <button type="submit"
                                class="btn btn-minion-main">

                            <span>가입하기</span>

                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>

</main>

</body>
</html>