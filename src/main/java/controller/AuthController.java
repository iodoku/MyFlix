package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AuthDao;
import dto.EmailDto;
import dto.UserDto;
import util.AuthCodeGenerator;
import util.EmailUtil;
import util.MakeRandomPw;

@WebServlet(
	
	// GET
	{"/findidpw", "/join", "/login",
	
	// POST
	"/joinidcheck", "/sendemail", "/codecheck", "/joinnickcheck", "/joinfullcheck",
	})

public class AuthController extends HttpServlet
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
				case "/findidpw":
					req.getRequestDispatcher("/WEB-INF/view/auth/findidpw.jsp").forward(req, resp);
					break;
					
				case "/join":
					req.getRequestDispatcher("/WEB-INF/view/auth/join.jsp").forward(req, resp);
					break;	
					
				case "/login":
					HttpSession session = req.getSession();

					String referer = req.getHeader("Referer");
					
					if(referer != null && 
					(referer.contains("/reviewlist")||referer.contains("/reviewdetail")))
					{
						session.setAttribute("loginTarget", referer);
					}
					else
					{
					    session.setAttribute("loginTarget", req.getContextPath() + "/main");
					}
				    
					req.getRequestDispatcher("/WEB-INF/view/auth/login.jsp").forward(req, resp);
					break;	
				
			}
		
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest req,
			 HttpServletResponse resp
	      ) throws ServletException, IOException
	{
		//글자셋
		req.setCharacterEncoding("UTF-8");
		
		//접속 URI 확인. 추출
		String uri     = req.getRequestURI();
		String context = req.getContextPath();
		String action  = uri.substring(context.length());
		
		String userid = req.getParameter("userid");
		String userpw = req.getParameter("userpw");
		String emailtype = req.getParameter("emailtype");
		String email = req.getParameter("emailid") + "@" + req.getParameter("emaildomain");
		String emailcode = req.getParameter("emailcode");
		String emailcodeid = req.getParameter("emailcodeid");
		String emailcodepw = req.getParameter("emailcodepw");
		String emailcodemp = req.getParameter("emailcodemp");
		String nick = req.getParameter("nick");
		
		try 
		{
			AuthDao authDAO = new AuthDao();
			UserDto userDTO = new UserDto();
			
			userDTO.setUserid(userid);
			userDTO.setUserpw(userpw);
			userDTO.setEmail(email);
			userDTO.setNick(nick);
				
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
					
			boolean result; // 결과 반환 값 저장 변수
			
			switch (action) 
			{

				/////////////////////////////////////////////////////////////////////////					
				//////////////////////////// 회원 가입 영역 시작 ////////////////////////////
				/////////////////////////////////////////////////////////////////////////
					
				case "/joinidcheck": // 아이디 중복 확인
					
					result = authDAO.idCheck(userid); // id 중복여부 확인 함수
					
					if(result) 
					{
						out.print("{\"msg\":\"OK\"}");
					}
					else 
					{
						out.print("{\"msg\":\"FAIL\"}");
					}
					
					out.flush();
					out.close();
					
					break;
					
				case "/sendemail": // 이메일 인증번호 전송
					
					req.setCharacterEncoding("UTF-8");

					AuthCodeGenerator codegenerator = new AuthCodeGenerator(); 
					
			        emailcode = codegenerator.generateAuthCode(); 	// 6자리 인증번호 생성	
			        
			        try 
			        {
			            // 이메일 발송
			            EmailUtil.sendEmail(email, emailcode);
			            

			            if("회원가입".equals(emailtype))
			            {
			            	HttpSession session = req.getSession();
				            session.setAttribute("checkemailcode",emailcode);
				            
				            System.out.println("받은 이메일 : " + email);
					        System.out.println("인증번호 : " + emailcode);
			            }

			            else if("id찾기".equals(emailtype))
			            {
			            	HttpSession session = req.getSession();
				            session.setAttribute("checkemailcodeid",emailcode);
				            
				            System.out.println("받은 이메일 : " + email);
					        System.out.println("인증번호 : " + emailcode);
			            }
			            else if("pw찾기".equals(emailtype))
			            {
			            	HttpSession session = req.getSession();
				            session.setAttribute("checkemailcodepw",emailcode);
				            
				            System.out.println("받은 이메일 : " + email);
					        System.out.println("인증번호 : " + emailcode);
			            }
			            else if ("이메일변경".equals(emailtype))
			            {
			            	HttpSession session = req.getSession();
			            	session.setAttribute("checkemailcodemp", emailcode);
			            	
			            	System.out.println("받은 이메일 : " + email);
			            	System.out.println("인증번호 : " + emailcode);
			            }
			            
			            resp.setContentType
			            (
			                "application/json; charset=UTF-8"
			            );

			            resp.getWriter().write(
			                "{\"msg\":\"OK\"}"
			            );

			        } 
			        catch (Exception e) 
			        {

			            e.printStackTrace();

			            resp.setContentType(
			                "application/json; charset=UTF-8"
			            );

			            resp.getWriter().write(
			                "{\"msg\":\"FAIL\"}"
			            );
			        }
					
					break;
					
				case "/codecheck": // 이메일 인증번호 확인
					
					req.setCharacterEncoding("UTF-8");
					
			        try 
			        {
			        	resp.setContentType
			            (
			                "application/json; charset=UTF-8"
			            );
			        	
			        	HttpSession session = req.getSession(false);	        	
			            
			            String checkemailcode = "";
			            
			            if(emailtype.equals("회원가입"))
			            {
			            	
			            	if (session != null)
			            	{
			            		checkemailcode = (String)session.getAttribute("checkemailcode");
			            	}
				            if((checkemailcode!=null&&emailcode!=null&&!emailcode.equals(""))
				            		&&checkemailcode.equals(emailcode))
				            {
				            	session.removeAttribute("checkemailcode");
				            	
				            	resp.getWriter().write("{\"msg\":\"OK\"}");
				            }
				            else{resp.getWriter().write("{\"msg\":\"FAIL\"}");}
			            }
			            else if(emailtype.equals("id찾기"))
			            {
			            	if (session != null)
			            	{
			            		checkemailcode = (String)session.getAttribute("checkemailcodeid");
			            	}
			            	
			            	if((checkemailcode!=null&&emailcodeid!=null&&!emailcodeid.equals(""))
				            		&&checkemailcode.equals(emailcodeid))
				            {
			            		List<String> useridlist = authDAO.findid(email);	// 받아올 아이디 리스트 정보 
			            		String       senduserid = "";						// 보내줄 아이디 목록 문자열

			            		if(email!=null && useridlist != null)
			            		{
			            			for(int i = 0; i < useridlist.size(); i++)
			            			{
			            				senduserid += useridlist.get(i);
			            				if(i != useridlist.size() - 1) {senduserid += ", ";}
			            			}
			            			
			            			EmailUtil.sendId(email, senduserid);

			            			session.removeAttribute("checkemailcodeid");
			            			
			            			resp.getWriter().write("{\"msg\":\"OK\"}");
			            		}
			            		else{resp.getWriter().write("{\"msg\":\"FAIL\"}");}
				            }
				            else
				            {
				            	resp.getWriter().write(
					                "{\"msg\":\"FAIL\"}"
					            );
				            }
			            }
			            else if(emailtype.equals("pw찾기"))
			            {
			            	if (session != null)
			            	{
			            		checkemailcode = (String)session.getAttribute("checkemailcodepw");
			            	}
		       	
			            	if((checkemailcode!=null&&emailcodepw!=null&&!emailcodepw.equals(""))
				            		&&checkemailcode.equals(emailcodepw))
				            {
			            		
			            		String tmppw = MakeRandomPw.MakeRandom();
	            				
			            		if(userid!=null && email!=null && tmppw != null)
			            		{
			            			boolean pwcheck = authDAO.changePw(userid, email, tmppw);
			            			if(pwcheck)
			            			{
			            				EmailUtil.sendPw(email, tmppw);
			            				
			            				session.removeAttribute("checkemailcodepw");
			            				
			            				resp.getWriter().write("{\"msg\":\"OK\"}");
			            			}
			            			else{resp.getWriter().write("{\"msg\":\"FAIL\"}");}
			            		}
			            		else{resp.getWriter().write("{\"msg\":\"FAIL\"}");}
				            }
				            else{resp.getWriter().write("{\"msg\":\"FAIL\"}");}
			            }
			            else if (emailtype.equals("이메일변경"))
			            {
			            	if (session != null) {
			            		checkemailcode = (String)session.getAttribute("checkemailcodemp");
			            	}
			            	
			            	if ((checkemailcode != null && emailcodemp != null && !emailcodemp.equals(""))
			            			&& checkemailcode.equals(emailcodemp)) {
			            		
			            		session.removeAttribute("checkemailcodemp");
			            		
			            		resp.getWriter().write("{\"msg\":\"OK\"}");
			            	}
			            	else {resp.getWriter().write("{\"msg\":\"FAIL\"}");}
			            }
			        } 
			        catch (Exception e) 
			        {
			            e.printStackTrace();
			            resp.setContentType("application/json; charset=UTF-8");
			            resp.getWriter().write("{\"msg\":\"FAIL\"}");
			        }
					break;
					
				case "/joinnickcheck": // 닉네임 중복 체크
					
					req.setCharacterEncoding("UTF-8");
					
					result = authDAO.nickCheck(nick); // id 중복여부 확인 함수
					
					if(result) {out.print("{\"msg\":\"OK\"}");}
					else {out.print("{\"msg\":\"FAIL\"}");}
					
					out.flush();
					out.close();
					
					break;
					
				case "/joinfullcheck": // 회원가입 완료 체크
					
					req.setCharacterEncoding("UTF-8");
					
					result = authDAO.joinFullCheck(
							userid, 
							authDAO.idCheck(userid),
							userpw,
							email,
							nick,
							authDAO.nickCheck(nick)
							);
					
					//DB저장
					
					
					if(result) 
					{
						authDAO.join(userDTO);
						resp.sendRedirect(req.getContextPath() + "/login");
					}
					else 
					{
						resp.sendRedirect(req.getContextPath() + "/join");
					}
					
					out.flush();
					out.close();
					
					break;
				/////////////////////////////////////////////////////////////////////////
				//////////////////////////// 회원 가입 영역 종료 ////////////////////////////
				/////////////////////////////////////////////////////////////////////////	
					
				/////////////////////////////////////////////////////////////////////////
				////////////////////////////// 로그인 영역 시작 /////////////////////////////
				/////////////////////////////////////////////////////////////////////////
				
				case "/login": // 로그인 체크
				
					req.setCharacterEncoding("UTF-8");
					
					result = authDAO.login(userid,userpw); // 로그인 성공 여부 판단
					
					HttpSession session = req.getSession();
					
					if(result) 
					{
						String viewnick = authDAO.getnick(userid);
						String viewemail = authDAO.getemail(userid);
						
						session.setAttribute("userid",userid);
						session.setAttribute("userpw",userpw);
						session.setAttribute("nick",viewnick);
						session.setAttribute("email",viewemail);
						
						String loginTarget = (String)session.getAttribute("loginTarget");
						
						resp.sendRedirect(loginTarget);
					}
					else 
					{
						resp.sendRedirect(req.getContextPath() + "/login?msg=fail");
					}
					
					break;
				
				/////////////////////////////////////////////////////////////////////////
				////////////////////////////// 로그인 영역 종료 /////////////////////////////
				/////////////////////////////////////////////////////////////////////////		
							
			}
		
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
