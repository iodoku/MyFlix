<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- CSS 파일 경로 --%>
<% request.setAttribute("csspath", "main/main.css"); %> 

<%-- 타이틀 이름 --%>
<% request.setAttribute("titlename", " 내플릭스 | 메인 "); %>

<%-- 스크립트 이름 --%>
<% request.setAttribute("scriptpath", null); %>


<%@ include file="/WEB-INF/view/layout/header.jsp" %>

<!-- ===== 본문 ===== -->
<main class="hero">
  <img src="${pageContext.request.contextPath}/img/minions4.jpg">

  <div class="hero-content">
    <h1 class="hero-title">자유롭게 토론하는<br><em>영화</em> 커뮤니티</h1>
  </div>

  <div class="hero-caption">Biografen · 저녁의 작은 영화관</div>
</main>

</body>
</html>