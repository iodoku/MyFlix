package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AuthDao;
import dto.UserDto;
import util.EmailUtil;
import util.MakeRandomPw;

@WebServlet({"/main", "/logout"})
public class MainController extends HttpServlet
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
				case "/main":
					req.getRequestDispatcher("/WEB-INF/view/main/main.jsp").forward(req, resp);
					break;
					
				case "/logout":
					HttpSession session = req.getSession(false);	// 이미 만들어져 있는 세션이 있는지 확인
										// true → 세션이 없으면 새로 생성
										// false → 세션이 없으면 새로 생성하지 않고 null 반환
					if (session != null) session.invalidate();		// 세션에 값이 있으면 삭제
					
					resp.sendRedirect(req.getContextPath() + "/main");
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
		
		try 
		{
				
			resp.setContentType("application/json; charset=UTF-8");
			
			switch (action) 
			{
				
					
					
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
