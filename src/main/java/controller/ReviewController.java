package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ReplyDao;
import dao.ReviewDao;
import dto.ReplyDto;
import dto.ReviewDto;
import javax.servlet.http.Part;


@WebServlet({
				"/reviewlist", "/reviewwrite", "/reviewdetail",
				
				"/insertcomment", "/updatecomment", "/deletecomment", "/deletereview"
			})
@MultipartConfig(
	    maxFileSize = 1024 * 1024 * 5,        // 개별 파일 최대 5MB
	    maxRequestSize = 1024 * 1024 * 20     // 전체 폼 데이터 최대 20MB
	)
public class ReviewController extends HttpServlet
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
		
		ReviewDao reviewDao = new ReviewDao();
	    ReplyDao replyDao = new ReplyDao();
	    ReviewDto reviewDto;
		
	    HttpSession session = req.getSession();
	      
		try 
		{
			switch (action) 
			{
				case "/reviewlist":		// 리뷰 목록으로 이동			
				    int pagingno = 1;	// 현재 페이지 번호, 기본값 1페이지
	
					// 현재 페이지 번호 받아옴 reviewlist?page=3 이때는 3페이지
				    String page = req.getParameter("page");				
	
				    // 페이지 정보가 있으면 정수로 변환하여 현재 페이지 번호 설정
				    if (page != null && !page.isEmpty()) {pagingno = Integer.parseInt(page);}
	
				    String searchType = req.getParameter("searchType");	// 검색 조건
				    String keyword    = req.getParameter("keyword");	// 검색어
	
				    if (searchType == null) {searchType = "";}
				    if (keyword == null)    {keyword = "";}
	
				    int perpage = 6;                  	// 한 페이지에 보여줄 리뷰 수
				    int totalCount;                   	// 검색 조건에 따른 전체 리뷰 수
				    
				    List<ReviewDto> reviewList;       	// 현재 페이지의 리뷰 목록
	
				    if (!keyword.trim().isEmpty()) 		// 검색어가 있으면 검색
				    {
				        reviewList = reviewDao.searchList(pagingno,searchType,keyword);	// 검색 조건에 맞는 리뷰 목록 조회
				        totalCount = reviewDao.searchCount(searchType,keyword);			// 검색 조건에 맞는 전체 리뷰 수 조회
				    } 
				    else
				    {
				        reviewList = reviewDao.list(pagingno);	// 검색어가 없으면 전체 리뷰 목록 조회
				        totalCount = reviewDao.totalReview();	// 전체 리뷰 수 조회	
				    }
	
				    int totalPage = (int) Math.ceil((double) totalCount / perpage);	// 전체 페이지 수 계산
				    int pageGroup = 5;												// 페이지 번호 5개 단위
				    int startPage = ((pagingno - 1) / pageGroup) * pageGroup + 1;	// 현재 페이지 그룹의 시작 페이지 번호
				    int endPage   = startPage + pageGroup - 1;						// 현재 페이지 그룹의 마지막 페이지 번호
	
				    // 마지막 페이지가 전체 페이지 수보다 크면 전체 페이지 수를 마지막 페이지로 설정
				    if (endPage > totalPage) {endPage = totalPage;}
	
				    req.setAttribute("reviewList", reviewList);	// 리뷰 목록 리스트
				    req.setAttribute("pagingno"  , pagingno);	// 현제 페이지 번호
				    req.setAttribute("totalPage" , totalPage);	// 전체 페이지 번호
				    req.setAttribute("startPage" , startPage);	// 시작 페이지 번호
				    req.setAttribute("endPage"   , endPage);	// 마지막 페이지 번호		    
				    req.setAttribute("searchType", searchType);	// 검색 타입(리뷰제목, 영화제목, 작성자)
				    req.setAttribute("keyword", keyword);		// 검색 값
	
				    req.getRequestDispatcher("/WEB-INF/view/review/reviewlist.jsp").forward(req, resp);
				    break;
				
				case "/reviewwrite":
					/*** 작성화면 로드 + 수정화면 로드 ***/
					// 1. 로그인 검증
					session = req.getSession(false);
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
					// 2. 수정화면 로드 , 글번호(r_no) 파라미터 확인(작성화면, 수정화면 판별)
					String r_no_param = req.getParameter("r_no");
					if(r_no_param != null && !r_no_param.trim().isEmpty())
					{
						// [수정모드] 기존 리뷰 데이터 조회
						int r_no = Integer.parseInt(r_no_param);
						ReviewDao dao = new ReviewDao();
						ReviewDto dto = dao.reviewDetail(r_no);
						
						// url 강제 접근한 경우 + 본인글 아닌 경우방어 : reviewlist로 돌려보냄
						if(dto == null || !userid.equals(dto.getUserid()))
						{
							resp.sendRedirect(context + "/reviewlist");
							return;
						}
						
						//화면단(reviewwrite.jsp)으로 기존 데이터 전달 및 수정모드 or 작성모드 인지 구분
						req.setAttribute("review", dto);
						req.setAttribute("mode", "update");
					}else {
						req.setAttribute("mode", "write");
					}
					
					req.getRequestDispatcher("/WEB-INF/view/review/reviewwrite.jsp").forward(req, resp);
					break;
				
				case "/reviewdetail":						// 리뷰 상세 보기 화면으로 이동
				    String r_no = req.getParameter("r_no");	// 리뷰 번호 받기

				    if (r_no == null || r_no.isEmpty()) 	// r_no 리뷰 번호가 없다면 다시 리뷰 목록으로 이동
				    {
				        resp.sendRedirect(req.getContextPath() + "/reviewlist");
				        return;
				    }

				    // 리뷰 번호에 해당하는 리뷰 상세 정보를 받아옴
				    reviewDto = reviewDao.reviewDetail(Integer.parseInt(r_no));
				    
				    // 조회된 리뷰가 없으면 목록으로 이동
				    if (reviewDto == null) 
				    {
				        resp.sendRedirect(req.getContextPath() + "/reviewlist");
				        return;
				    }

				    // 댓글 목록 조회
				    ArrayList<ReplyDto> replyList = replyDao.list(Integer.parseInt(r_no));
				    req.setAttribute("reviewDto", reviewDto);	// JSP로 리뷰 정보 전달
				    req.setAttribute("replyList", replyList);	// JSP로 댓글 목록 전달

				    req.getRequestDispatcher("/WEB-INF/view/review/reviewdetail.jsp").forward(req, resp);
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
			
		HttpSession session = req.getSession(false);	// 세션 선언
	    
		String userid = null;
		String nick   = null;
		if (session != null) { userid = (String) session.getAttribute("userid");}			// 사용자 아이디
		if (session != null) { nick   = (String) session.getAttribute("nick");  }			// 사용자 닉네임	
					    
	    boolean result; 		// 결과 판단 결과 저장 변수
		
		ReplyDto  replyDto;
		ReviewDto reviewDto;
		
		ReplyDao  replyDao  = new ReplyDao();
		ReviewDao reviewDao = new ReviewDao();
		
		try 
		{
			switch (action) 
			{
				case "/reviewwrite":
					/*** 작성하기 로직, 수정하기 로직***/
					// 1. 로그인 여부 판별
					session = req.getSession(false);
					userid = null;
					nick = null;
                    if (session != null) 
                    {
                        userid = (String) session.getAttribute("userid");
                        nick = (String) session.getAttribute("nick");
                    }				
					// 비로그인시 로그인화면으로 돌려보내기
					if(userid == null || userid.trim().isEmpty())
					{
						resp.sendRedirect(context + "/login?msg=need_login");
						return;
					}
					
					//로그인시 작성하기 로직 진행
					
					// 2. 일반 텍스트 파라미터 수신 + 수정모드 or 작성모드 파라미터 수신
					String mode = req.getParameter("mode"); // update 값 null or 빈값(write)
					String r_no = req.getParameter("r_no"); // update 시 받아야할 r_no
					
					String r_title = req.getParameter("r_title");
					String m_title = req.getParameter("m_title");
					String m_dt = req.getParameter("m_dt");
					String m_genre1 = req.getParameter("m_genre1");
					String m_genre2 = req.getParameter("m_genre2");
					String m_genre3 = req.getParameter("m_genre3");
					String r_content = req.getParameter("r_content");
					
					// 수정시 기존 파일 유지를 위한 파라미터 수신
					String existFile = req.getParameter("existing_file");
					String existPath = req.getParameter("existing_path");
					
					// 3. 첨부파일 정보 추출
					Part filePart = req.getPart("m_filename");
					String originalFileName = (filePart != null) ? filePart.getSubmittedFileName() : null ;
					
					// 4. 입력값 2차 유효성 검사 (dao 메서드 호출)
					ReviewDao dao = new ReviewDao();
					boolean isValid = dao.reviewwriteVal(r_title, m_title, m_dt, m_genre1, m_genre2, m_genre3, r_content, originalFileName);
					if(!isValid)
					{
						//수정모드였다면 r_no를 붙여서 , 작성모드였다면 ""으로
						resp.sendRedirect(context + "/reviewwrite" + ("update".equals(mode) ? "?r_no=" + r_no : ""));
						return;
					}
				
					// 5. 파일 업로드 처리
					// [1단계] 클라이언트가 보낸 파일 껍데기 꺼내기
					String savedFileName = null; //DB에 저장할 고유 파일명 m_filename 에 들어갈값
					String webPath = null;		//DB에 저장할 고유 경로 m_filepath 에 들어갈 값
					if (originalFileName != null && !originalFileName.trim().isEmpty())
					{	
						// [2단계] 서버 하드디스크의 실제 물리 경로(realPath) 찾기 및 폴더 생성
						// 새파일이 첨부된 경우 -> 새 파일 저장
                        String realPath = req.getServletContext().getRealPath("/upload");
                        File uploadDir = new File(realPath);
                        if (!uploadDir.exists()) 
                        {
                            uploadDir.mkdirs(); // upload 폴더가 없으면 자동 생성
                        }
                        // [3단계] 파일명 난수화 (UUID) UUID를 붙여 고유한 파일명 생성
                        // 원본이 'ironman.png'라면 확장자(.png)만 잘라내어 '3f9b2...4a.png'로 변환
                        String ext = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                        savedFileName = UUID.randomUUID().toString() + ext;
                        
                        // [4단계] 실제 하드디스크에 바이너리 파일 저장
                        // 물리 디스크에 파일 저장
                        filePart.write(realPath + File.separator + savedFileName);
                        
                        // DB에 남겨둘 웹 접근용 경로 세팅
                        // 화면(JSP)에서 불러올때 사용할 웹 경로
                        webPath = "/upload";
                        
                        // 수정 모드이면서 기존파일이 존재했다면 하드디스크에서 기존 파일 삭제
                        if ("update".equals(mode) && existFile != null && !existFile.trim().isEmpty()) 
                        {
                            File oldFile = new File(realPath + File.separator + existFile);
                            if (oldFile.exists()) 
                            {
                                oldFile.delete();
                            }
                        }
					} else {
                        // [새 파일이 첨부되지 않은 경우]
                        if ("update".equals(mode)) 
                        {
                            // 수정 모드: 기존 파일 정보 유지
                            savedFileName = existFile;
                            webPath = existPath;
                        }
                        // 신규 등록 모드라면 savedFileName, webPath는 null로 유지됨                        
                    }
                    
                    // 5. Dto setting
					ReviewDto dto = new ReviewDto();
					dto.setR_title(r_title);
					dto.setM_title(m_title);
					dto.setM_dt(m_dt);
					dto.setM_genre1(m_genre1);
					dto.setM_genre2(m_genre2);
					dto.setM_genre3(m_genre3);
					dto.setR_content(r_content);
					dto.setM_filename(savedFileName);
					dto.setM_filepath(webPath);
					dto.setUserid(userid);
					dto.setNick(nick);
					
                    
                    // 6. Mode에 따른 DAO 호출 분기
                    if ("update".equals(mode)) 
                    {
                        int reviewNo = Integer.parseInt(r_no);
                        dto.setR_no(reviewNo);

                        boolean updateSuccess = dao.reviewUpdate(dto); // DB에 수정 메서드 호출
                        if (updateSuccess) 
                        {
                            resp.sendRedirect(context + "/reviewdetail?r_no=" + reviewNo);
                        } else {
                            resp.sendRedirect(context + "/reviewwrite?r_no=" + reviewNo + "&msg=fail");
                        }
                    } else {
                        boolean writeSuccess = dao.reviewWrite(dto); // DB에 등록 메서드 호출
                        if (writeSuccess) 
                        {
                            resp.sendRedirect(context + "/reviewlist");
                        } else {
                            resp.sendRedirect(context + "/reviewwrite?msg=fail");
                        }
                    }
                    
                    break;
                    
				case "/insertcomment":   // 댓글 작성 하기 
					if (userid == null) 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
			            return;
			        }
					
					int ic_tr_no         = Integer.parseInt(req.getParameter("r_no"));			// 조회 리뷰 번호	
					double ic_rp_rating  = Double.parseDouble(req.getParameter("rp_rating"));	// 조회 리뷰 댓글 평점
					String ic_rp_content = req.getParameter("rp_content");						// 조회 리뷰 댓글 내용
			        
					if (!replyDao.insertcheck(ic_rp_rating, ic_rp_content)) 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"입력값이 유효하지 않습니다.\"}");
			            return;
			        }
						
			        replyDto = new ReplyDto();      	// 댓글 정보 받아올 dto 설정
			        replyDto.setR_no(ic_tr_no);			// 리뷰 번호 설정
			        replyDto.setRp_content(ic_rp_content);	// 댓글 내용 설정
			        replyDto.setRp_rating(ic_rp_rating);	// 댓글 평점 설정 
			        replyDto.setUserid(userid);			// 댓글 작성자 아이디 설정

			        result = replyDao.insert(replyDto);	// DB작성 및 결과 반환

			        if (result) 	// 댓글 작성 성공하면 실행
			        {
			            replyDao.updateReviewRating(ic_tr_no);		// 댓글 작성 후 리뷰 평점 갱신				            
			            resp.getWriter().write("{\"success\":true,\"message\":\"댓글이 작성되었습니다.\"}");
			        } 
			        else 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"댓글 작성에 실패했습니다.\"}");
			        }
				    break;
					
				case "/updatecomment":	// 댓글 수정 하기

					if (userid == null) 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
			            return;
			        }
					
					int uc_tr_no;																// 조회 리뷰 번호				
					int uc_rp_no         = Integer.parseInt(req.getParameter("rp_no"));			// 조회 리뷰 댓글 번호
					double uc_rp_rating  = Double.parseDouble(req.getParameter("rp_rating"));	// 조회 리뷰 댓글 평점
					String uc_rp_content = req.getParameter("rp_content");						// 조회 리뷰 댓글 내용
					
				    uc_tr_no  = replyDao.getReviewNo(uc_rp_no);	// 수정하기 전에 해당 댓글의 리뷰 번호 가져오기	
				    
				    replyDto = new ReplyDto();				    
				    replyDto.setRp_no(uc_rp_no);				// 리뷰 번호 설정
				    replyDto.setRp_content(uc_rp_content);		// 댓글 내용 설정
				    replyDto.setRp_rating(uc_rp_rating);		// 댓글 평점 설정 
				    replyDto.setUserid(userid);					// 댓글 작성자 아이디 설정

				    result = replyDao.update(replyDto);		// DB수정 및 결과 반환
				    
				    if (result) {replyDao.updateReviewRating(uc_tr_no);}	// 댓글 작성 후 리뷰 평점 갱신	

				    resp.setContentType("application/json; charset=UTF-8");
				    resp.getWriter().print("{\"result\":" + result + "}");
				    break;	
				    
				case "/deletecomment":	// 댓글 삭제 하기
					int dc_tr_no;													// 조회 리뷰 번호				
					int dc_rp_no = Integer.parseInt(req.getParameter("rp_no"));		// 조회 리뷰 댓글 번호
			    	
					if (userid == null) 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
			            return;
			        }
			        
			        dc_tr_no  = replyDao.getReviewNo(dc_rp_no);	// 삭제하기 전에 리뷰 번호 확인

			        result = replyDao.delete(dc_rp_no, userid);	// DB 컬럼 삭제 및 결과 반환
			        resp.setContentType("text/plain; charset=UTF-8");

			        if (result) 
			        {
			        	replyDao.updateReviewRating(dc_tr_no);	// 댓글 삭제 후 리뷰 평점 다시 계산
			            resp.getWriter().write("success");
			        } 
			        else 
			        {
			            resp.getWriter().write("fail");
			        }
				    break;
				    
				case "/deletereview":

					if (userid == null) 
			        {
			            resp.getWriter().write("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
			            return;
			        }
					
				    int dr_r_no = Integer.parseInt(req.getParameter("r_no"));	// 리뷰 번호 받아옴

				    reviewDto = reviewDao.reviewDetail(dr_r_no);	// 삭제할 리뷰의 상세 정보 조회
				    
				    // 리뷰가 존재하지 않거나 작성자가 본인이 아니면 삭제하지 않음
				    if (reviewDto == null || !userid.equals(reviewDto.getUserid())) 
				    {
				        resp.setContentType("application/json; charset=UTF-8");
				        resp.getWriter().print("{\"result\":0}");
				        return;
				    }
				    
				    result    = reviewDao.reviewDelete(dr_r_no);	// 해당 리뷰 삭제 및 삭제 결과 반환

				    resp.setContentType("application/json; charset=UTF-8");

				    if (result) 
				    {
				        resp.getWriter().print("{\"result\":1}");
				    } 
				    else 
				    {
				        resp.getWriter().print("{\"result\":0}");
				    }
				    break;
			}			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}