<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="ko">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <!-- 공통 CSS 영역 -->

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link rel="preconnect"
          href="https://fonts.gstatic.com"
          crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=Baloo+2:wght@500;600;700;800&family=Nunito:wght@400;600;700;800&display=swap"
          rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/layout/header.css">


    <!-- 개별 CSS 영역 -->

    <%
        String csspath = (String)request.getAttribute("csspath");

        if(csspath != null && !csspath.trim().isEmpty())
        {
    %>

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/<%=csspath%>">

    <%
        }
    %>


    <!-- 공통 JS 영역 -->

    <script src="${pageContext.request.contextPath}/js/jquery-4.0.0.js" defer></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            defer></script>

    <!-- contextPath를 외부 JS에서 사용하기 위한 변수 -->

    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>


    <!-- 개별 JS 영역 -->

    <%
        String scriptpath = (String)request.getAttribute("scriptpath");

        if(scriptpath != null && !scriptpath.trim().isEmpty())
        {
    %>

        <script src="${pageContext.request.contextPath}/js/<%=scriptpath%>" defer></script>

    <%
        }
    %>


    <!-- 개별 Title 영역 -->

    <%
        String titlename = (String)request.getAttribute("titlename");

        if(titlename != null && !titlename.trim().isEmpty())
        {
    %>

        <title><%=titlename%></title>

    <%
        }
    %>

</head>


<body>

    <%
        // 로그인 시 session에 저장해두신 속성명으로 바꿔주세요.
        String nickName = (session != null)
                ? (String) session.getAttribute("nick")
                : null;
    %>


    <!-- ===== 헤더 ===== -->

    <header class="custom-navbar sticky-top">

        <div class="container-fluid d-flex justify-content-between align-items-center px-0">


            <!-- 로고: MyFlix -->

            <div>

                <a class="navbar-brand-logo"
                   href="${pageContext.request.contextPath}/main">

                    <img src="${pageContext.request.contextPath}/img/minions3.webp"
                         alt="MyFlix Logo"
                         class="logo-img">

                    <span>
                        My<span class="logo-highlight">Flix</span>
                    </span>

                </a>

            </div>


            <!-- 중앙: 메인 메뉴 -->

            <div class="d-flex gap-2 gap-md-3 justify-content-center review-nav">

                <a href="${pageContext.request.contextPath}/reviewwrite"
                   class="nav-pill-btn">
                    영화 리뷰 작성
                </a>

                <a href="${pageContext.request.contextPath}/reviewlist"
                   class="nav-pill-btn">
                    영화 리뷰 목록
                </a>

                <a href="${pageContext.request.contextPath}/mp_review"
                   class="nav-pill-btn">
                    &nbsp;&nbsp;마이페이지&nbsp;&nbsp;
                </a>

            </div>


            <!-- 우측 로그인 영역 -->

            <div class="d-flex gap-2">

                <% if (nickName != null) { %>

                    <span class="nav-user">
			            <span class="nav-user-icon">👤</span>
			            <span><%= nickName %> 님</span>
			        </span>

                    <a href="${pageContext.request.contextPath}/logout"
                       class="nav-pill-btn">
                        로그아웃
                    </a>

                    <a href="${pageContext.request.contextPath}/join"
                       class="nav-pill-btn">
                        회원가입
                    </a>

                <% } else { %>

                    <a href="${pageContext.request.contextPath}/login"
                       class="nav-pill-btn">
                        로그인
                    </a>

                    <a href="${pageContext.request.contextPath}/join"
                       class="nav-pill-btn">
                        회원가입
                    </a>

                <% } %>

            </div>

        </div>

    </header>