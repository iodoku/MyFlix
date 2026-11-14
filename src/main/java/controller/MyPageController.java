package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AuthDao;
import dao.ReplyDao;
import dao.ReviewDao;
import dto.ReplyDto;
import dto.ReviewDto;

@WebServlet({"/mp_email", "/mp_nick", "/mp_pw", "/mp_reply", "/mp_review",
	"/mp_nick_change", "/mp_pw_change", "/mp_email_change", "/mp_nick_check"})
public class MyPageController extends HttpServlet
{
	protected void doGet(HttpServletRequest req,
			 HttpServletResponse resp
	      ) throws ServletException, IOException
	{
		//글자셋
		req.setCharacterEncoding("UTF-8");
		
		//접속 URI 확인. 추출
		String uri     = req.getRequestURI();
		String context = req.getContextPath();
		String action  = uri.substring(context.length());
		
		try 
		{
			switch (action) 
			{
				case "/mp_email":
					/*** 마이페이지 이메일 변경 (다른 곳과 구분하기 위해 변수명에 _email 를 붙임) ***/
					// 1. 로그인 검증
					HttpSession session_email = req.getSession();
					String userid_email = null;
					
					// 세션 null 체크 후 속성 추출
                    if (session_email != null)
                    {
                        userid_email = (String) session_email.getAttribute("userid");
                    }
                    
					// 로그인 여부 판별(비로그인시)
					if(userid_email == null || userid_email.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					req.getRequestDispatcher("/WEB-INF/view/mypage/mp_email.jsp").forward(req, resp);
					break;
		
				case "/mp_nick":
					/*** 마이페이지 닉네임 변경 (다른 곳과 구분하기 위해 변수명에 _nick 를 붙임) ***/
					// 1. 로그인 검증
					HttpSession session_nick = req.getSession();
					String userid_nick = null;
					
					// 세션 null 체크 후 속성 추출
                    if (session_nick != null)
                    {
                        userid_nick = (String) session_nick.getAttribute("userid");
                    }
                    
					// 로그인 여부 판별(비로그인시)
					if(userid_nick == null || userid_nick.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					req.getRequestDispatcher("/WEB-INF/view/mypage/mp_nick.jsp").forward(req, resp);
					break;
				
				case "/mp_nick_check":
					
					String checkNick = req.getParameter("nick");
					
					boolean isAvailable = false;
					
					try {
						AuthDao dao = new AuthDao();
						isAvailable = dao.nickCheck(checkNick);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					resp.setContentType("text/plain; charset=utf-8");
					resp.getWriter().print(isAvailable);
					
					return;
			
				case "/mp_pw":
					/*** 마이페이지 비밀번호 변경 (다른 곳과 구분하기 위해 변수명에 _pw 를 붙임) ***/
					// 1. 로그인 검증
					HttpSession session_pw = req.getSession();
					String userid_pw = null;
					
					// 세션 null 체크 후 속성 추출
                    if (session_pw != null)
                    {
                        userid_pw = (String) session_pw.getAttribute("userid");
                    }
                    
					// 로그인 여부 판별(비로그인시)
					if(userid_pw == null || userid_pw.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					req.getRequestDispatcher("/WEB-INF/view/mypage//mp_pw.jsp").forward(req, resp);
					break;
		
				case "/mp_reply":
					/*** 내가 쓴 댓글 출력 (다른 곳과 구분하기 위해 변수명에 _reply 를 붙임) ***/
					// 1. 로그인 검증
					HttpSession session_reply = req.getSession();
					String userid_reply = null;
					
					// 세션 null 체크 후 속성 추출
                    if (session_reply != null)
                    {
                        userid_reply = (String) session_reply.getAttribute("userid");
                    }
                    
					// 로그인 여부 판별(비로그인시)
					if(userid_reply == null || userid_reply.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					int pagingno_reply = 1; // 현재 페이지 번호, 기본값 1페이지
					
					// 현재 페이지 번호 받아옴 myreply?page=3 이때는 3페이지
				    String page_reply = req.getParameter("page");
				    
				    // dao 선언
				    ReplyDao replyDao_reply = new ReplyDao();
	
				    // 페이지 정보가 있으면 정수로 변환하여 현재 페이지 번호 설정
				    if (page_reply != null && !page_reply.isEmpty()) {pagingno_reply = Integer.parseInt(page_reply);}
	
				    int perpage_reply = 10;                  // 한 페이지에 보여줄 리뷰 수
				    int totalCount_reply = replyDao_reply.myTotalReply(userid_reply);                   // 내 전체 리뷰 수
				    
				    List<ReplyDto> myreply = replyDao_reply.myreplylist(pagingno_reply, userid_reply);       // 현재 페이지의 내 리뷰 목록
				    
				    int totalPage_reply = (int) Math.ceil((double) totalCount_reply / perpage_reply);	// 전체 페이지 수 계산
				    int pageGroup_reply = 5;												// 페이지 번호 5개 단위
				    int startPage_reply = ((pagingno_reply - 1) / pageGroup_reply) * pageGroup_reply + 1;	// 현재 페이지 그룹의 시작 페이지 번호
				    int endPage_reply   = startPage_reply + pageGroup_reply - 1;						// 현재 페이지 그룹의 마지막 페이지 번호
	
				    // 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수를 마지막 페이지로 설정
				    if (endPage_reply > totalPage_reply) {endPage_reply = totalPage_reply;}
	
				    req.setAttribute("myReply", myreply);
				    req.setAttribute("pagingno"  , pagingno_reply);
				    req.setAttribute("totalPage" , totalPage_reply);
				    req.setAttribute("startPage" , startPage_reply);
				    req.setAttribute("endPage"   , endPage_reply);
					
					req.getRequestDispatcher("/WEB-INF/view/mypage/mp_reply.jsp").forward(req, resp);
					break;
			
				case "/mp_review":
					/*** 내가 쓴 리뷰 출력 ***/
					// 1. 로그인 검증
					HttpSession session = req.getSession();
					String userid = null;
					
					// 세션 null 체크 후 속성 추출
                    if (session != null)
                    {
                        userid = (String) session.getAttribute("userid");
                    }
                    
					// 로그인 여부 판별(비로그인시)
					if(userid == null || userid.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					int pagingno = 1; // 현재 페이지 번호, 기본값 1페이지
					
					// 현재 페이지 번호 받아옴 mylist?page=3 이때는 3페이지
				    String page = req.getParameter("page");
				    
				    // dao 선언
				    ReviewDao reviewDao = new ReviewDao();
	
				    // 페이지 정보가 있으면 정수로 변환하여 현재 페이지 번호 설정
				    if (page != null && !page.isEmpty()) {pagingno = Integer.parseInt(page);}
	
				    int perpage = 10;                  // 한 페이지에 보여줄 리뷰 수
				    int totalCount = reviewDao.myTotalReview(userid);                   // 내 전체 리뷰 수
				    
				    List<ReviewDto> mylist = reviewDao.mylist(pagingno, userid);       // 현재 페이지의 내 리뷰 목록
	
				    int totalPage = (int) Math.ceil((double) totalCount / perpage);	// 전체 페이지 수 계산
				    int pageGroup = 5;												// 페이지 번호 5개 단위
				    int startPage = ((pagingno - 1) / pageGroup) * pageGroup + 1;	// 현재 페이지 그룹의 시작 페이지 번호
				    int endPage   = startPage + pageGroup - 1;						// 현재 페이지 그룹의 마지막 페이지 번호
	
				    // 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수를 마지막 페이지로 설정
				    if (endPage > totalPage) {endPage = totalPage;}
	
				    req.setAttribute("myList", mylist);
				    req.setAttribute("pagingno"  , pagingno);
				    req.setAttribute("totalPage" , totalPage);
				    req.setAttribute("startPage" , startPage);
				    req.setAttribute("endPage"   , endPage);
					
					req.getRequestDispatcher("/WEB-INF/view/mypage/mp_review.jsp").forward(req, resp);
					break;					
			}
		
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글자셋 설정
		req.setCharacterEncoding("UTF-8");
		
		//접속 URI 확인. 추출
		String uri     = req.getRequestURI();
		String context = req.getContextPath();
		String action  = uri.substring(context.length());
		
		try {
			switch (action) {
			case "/mp_nick_change":
				// 닉네임 변경
				// 1. 로그인 검증
				HttpSession session = req.getSession();
				String userid = null;
				
				// 세션 null 체크 후 속성 추출
                if (session != null)
                {
                    userid = (String) session.getAttribute("userid");
                }
                
				// 로그인 여부 판별(비로그인시)
				if(userid == null || userid.trim().isEmpty())
				{
					resp.sendRedirect(context + "/login?msg=need_login");
					return;
				}
				
				String newnick = req.getParameter("newNick");
				int result = 0;
				
				try {
					AuthDao dao = new AuthDao();
					
					result = dao.nickChange(userid, newnick);
					
					if (result > 0) {
						session.setAttribute("nick", newnick);
					} else {
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (result > 0) {
					resp.sendRedirect(context + "/mp_nick?status=success");
				} else {
					resp.sendRedirect(context + "/mp_nick");
				}
				
				break;
				
			case "/mp_pw_change":
				// 비밀번호 변경 (구분을 위해 _pw 붙임)
				// 1. 로그인 검증
				HttpSession session_pw = req.getSession();
				String userid_pw = null;
				
				// 세션 null 체크 후 속성 추출
                if (session_pw != null)
                {
                	userid_pw = (String) session_pw.getAttribute("userid");
                }
                
				// 로그인 여부 판별(비로그인시)
				if(userid_pw == null || userid_pw.trim().isEmpty())
				{
					resp.sendRedirect(context + "/login?msg=need_login");
					return;
				}
				
				String currentpw = req.getParameter("currentPw");
				String newpw = req.getParameter("newPw");
				
				int result_pw = 0;
				
				try {
					AuthDao dao = new AuthDao();
					
					boolean isrightpw = dao.pwCheck(userid_pw, currentpw);
					
					if (isrightpw) {
						result_pw  = dao.newPw(userid_pw, newpw);
					} else {
						resp.sendRedirect(context + "/mp_pw?error=wrong_pw");
						return;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (result_pw > 0) {
					resp.sendRedirect(context + "/mp_pw?status=success");
				} else {
					resp.sendRedirect(context + "/mp_pw");
				}
				
				
				break;
				
			case "/mp_email_change":
				// 이메일 변경 (구분을 위해 _email 붙임)
				// 1. 로그인 검증
				HttpSession session_email = req.getSession();
				String userid_email = null;
				
				// 세션 null 체크 후 속성 추출
                if (session_email != null)
                {
                	userid_email = (String) session_email.getAttribute("userid");
                }
                
				// 로그인 여부 판별(비로그인시)
				if(userid_email == null || userid_email.trim().isEmpty())
				{
					resp.sendRedirect(context + "/login?msg=need_login");
					return;
				}
				
				String emailid = req.getParameter("emailid");
				String emaildomain = req.getParameter("emaildomain");
				String fullemail = emailid + "@" + emaildomain;
				
				String redirectStatus = "fail";
				
				try {
					AuthDao dao = new AuthDao();
					int result_email = dao.emailChange(userid_email, fullemail);
					
					if (result_email > 0) {
						session_email.removeAttribute("checkemailcodemp");
						
						session_email.setAttribute("email", fullemail);
						
						redirectStatus = "success";
					}
				} catch (Exception e) {
					e.printStackTrace();
					
					redirectStatus = "duplicate";
				}
				
				resp.sendRedirect(context + "/mp_email?status=" + redirectStatus);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
