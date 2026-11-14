package dao;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import javax.servlet.http.Part;

import dto.ReviewDto;
import util.OracleConnect;

public class ReviewDao {
	// 리뷰 작성하기
	public boolean reviewWrite(ReviewDto reviewdto) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// r_no와 r_dt는 오라클DB에서 자동처리하므로 제외
			String sql = "INSERT INTO MV_BOARD "
					+ "(R_TITLE, M_TITLE, M_DT, M_GENRE1, M_GENRE2, M_GENRE3, "
					+ "M_FILENAME, M_FILEPATH, R_CONTENT, R_RATING, USERID) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reviewdto.getR_title());
			pstmt.setString(2, reviewdto.getM_title());
			pstmt.setString(3, reviewdto.getM_dt());
			pstmt.setString(4, reviewdto.getM_genre1());
			pstmt.setString(5, reviewdto.getM_genre2());
			pstmt.setString(6, reviewdto.getM_genre3());
			pstmt.setString(7, reviewdto.getM_filename());
			pstmt.setString(8, reviewdto.getM_filepath());
			pstmt.setString(9, reviewdto.getR_content());
			// DB구조상 not null이기 때문에 기본값 0.0 넣기
			pstmt.setDouble(10, 0.0);
			
			pstmt.setString(11, reviewdto.getUserid());
					
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
			if (conn != null) try { conn.close(); } catch (Exception e) {};
		}
		
		if (result == 1) {
			
			// 등록 성공 시 true 반환
			return true;
		} else {
			
			// 등록 실패 시 false 반환
			return false;
		}
	}
	
	// 리뷰 목록
	public ArrayList<ReviewDto> list(int pagingno) throws Exception{
		ArrayList<ReviewDto> list = new ArrayList<ReviewDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 한 화면에 보여줄 리뷰 수
		int perpage = 6;
		
		// 시작(DB 컬럼)
		int startrow = (pagingno - 1) * perpage + 1;
		
		// 끝(DB 컬럼)
		int endrow = pagingno * perpage;
		
		try {
			conn = OracleConnect.getConnection();
			if (conn == null) {
				// 연결이 안되어있다면 빈 화면
				return list;
			}
			
			String sql = "SELECT * FROM ( "
			        + " SELECT ROW_NUMBER() OVER (ORDER BY B.R_NO DESC) AS RNUM, "
			        + "        B.R_NO, "
			        + "        B.USERID, "
			        + "        U.NICK, "
			        + "        B.R_TITLE, "
			        + "        B.M_TITLE, "
			        + "        B.M_DT, "
			        + "        B.M_GENRE1, "
			        + "        B.M_GENRE2, "
			        + "        B.M_GENRE3, "
			        + "        B.M_FILENAME, "
			        + "        B.M_FILEPATH, "
			        + "        B.R_CONTENT, "
			        + "        B.R_DT, "
			        + "        B.R_RATING "
			        + " FROM MV_BOARD B "
			        + " JOIN MV_USER U ON B.USERID = U.USERID "
			        + ") WHERE RNUM BETWEEN ? AND ?";
			
			pstmt = conn.prepareStatement(sql);
			
			// 화면에 보여줄 리뷰만 추출
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ReviewDto item = new ReviewDto();
				item.setR_no(rs.getInt("R_NO"));
				item.setUserid(rs.getString("USERID"));
			    item.setNick(rs.getString("NICK"));
				item.setR_title(rs.getString("R_TITLE"));
				item.setM_title(rs.getString("M_TITLE"));
				item.setM_dt(rs.getString("M_DT"));
				item.setM_genre1(rs.getString("M_GENRE1"));
				item.setM_genre2(rs.getString("M_GENRE2"));
				item.setM_genre3(rs.getString("M_GENRE3"));
				item.setM_filename(rs.getString("M_FILENAME"));
				item.setM_filepath(rs.getString("M_FILEPATH"));
				item.setR_content(rs.getString("R_CONTENT"));
				item.setR_dt(rs.getString("R_DT"));	
				item.setR_rating(rs.getDouble("R_RATING"));	
				
				// 리스트에 불러온 리뷰 목록 저장
				list.add(item);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {};
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
			if (conn != null) try { conn.close(); } catch (Exception e) {};
		}
		
		// 리뷰 목록 반환
		return list;
	}
	
	// 내 리뷰 목록
	public ArrayList<ReviewDto> mylist(int pagingno, String userid) throws Exception {

	    ArrayList<ReviewDto> list = new ArrayList<ReviewDto>();

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    // 한 화면에 보여줄 리뷰 수
	    int perpage = 10;

	    // 시작(DB 컬럼)
	    int startrow = (pagingno - 1) * perpage + 1;

	    // 끝(DB 컬럼)
	    int endrow = pagingno * perpage;

	    try {

	        conn = OracleConnect.getConnection();

	        if (conn == null) {
	            // 연결이 안되어있다면 빈 화면
	            return list;
	        }

	        String sql = "SELECT * FROM ( "
	                + "    SELECT ROW_NUMBER() OVER (ORDER BY B.R_NO DESC) AS RNUM, "
	                + "           B.R_NO, "
	                + "           B.USERID, "
	                + "           U.NICK, "
	                + "           B.R_TITLE, "
	                + "           B.R_RATING, "
	                + "           TO_CHAR(B.R_DT, 'YYYY-MM-DD') AS R_DT "
	                + "    FROM MV_BOARD B "
	                + "    JOIN MV_USER U ON B.USERID = U.USERID "
	                + "    WHERE B.USERID = ? "
	                + ") WHERE RNUM BETWEEN ? AND ? ";

	        pstmt = conn.prepareStatement(sql);

	        // 화면에 보여줄 리뷰만 추출
	        pstmt.setString(1, userid);
	        pstmt.setInt(2, startrow);
	        pstmt.setInt(3, endrow);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {

	            ReviewDto item = new ReviewDto();

	            item.setR_no(rs.getInt("R_NO"));
	            item.setUserid(rs.getString("USERID"));
	            item.setNick(rs.getString("NICK"));       // 닉네임 추가
	            item.setR_title(rs.getString("R_TITLE"));
	            item.setR_rating(rs.getDouble("R_RATING"));
	            item.setR_dt(rs.getString("R_DT"));

	            // 리스트에 불러온 리뷰 목록 저장
	            list.add(item);
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        if (rs != null) try { rs.close(); } catch (Exception e) {}
	        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
	        if (conn != null) try { conn.close(); } catch (Exception e) {}
	    }

	    // 리뷰 목록 반환
	    return list;
	}
	
	// 모든 리뷰 개수
	public int totalReview() throws Exception 
	{
		int totalreview = 0;	// 전체 리뷰 개수
		
		Connection        conn  = null;
		PreparedStatement pstmt = null;
		ResultSet         rs    = null;
		
		try 
		{
			conn = OracleConnect.getConnection();
			
			// 모든 리뷰 개수 구하기
			String sql = "SELECT COUNT(*) FROM MV_BOARD ";
			
			pstmt = conn.prepareStatement(sql);
			rs    = pstmt.executeQuery();
			
			if (rs.next()) {totalreview = rs.getInt(1);}	// 모든 리뷰 개수 저장
		} 
		catch (Exception e) {e.printStackTrace();} 
		finally 
		{
			if (rs    != null) try { rs.close();    } catch (Exception e) {};
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
			if (conn  != null) try { conn.close();  } catch (Exception e) {};
		}
		
		return totalreview;	// 리뷰 개수 반환
	}
	
	// 내 모든 리뷰 개수
	public int myTotalReview(String userid) throws Exception {
		int mytotalreview = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 내 모든 리뷰 개수 구하기
			String sql = "SELECT COUNT(*) FROM MV_BOARD WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				// 내 모든 리뷰 개수 저장
				mytotalreview = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e) {};
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
			if (conn != null) try { conn.close(); } catch (Exception e) {};
		}
		
		// 리뷰 개수 반환
		return mytotalreview;
	}
	
	// 리뷰 하나 상세보기
	public ReviewDto reviewDetail(int r_no) throws Exception {

	    ReviewDto item = null;

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {

	        conn = OracleConnect.getConnection();

	        // r_no를 이용해서 리뷰 정보 + 작성자 닉네임 조회
	        String sql = "SELECT B.*, U.NICK "
	                   + "FROM MV_BOARD B "
	                   + "JOIN MV_USER U ON B.USERID = U.USERID "
	                   + "WHERE B.R_NO = ? ";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, r_no);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {

	            item = new ReviewDto();

	            item.setR_no(rs.getInt("R_NO"));
	            item.setUserid(rs.getString("USERID"));
	            item.setNick(rs.getString("NICK"));       // 닉네임 추가

	            item.setR_title(rs.getString("R_TITLE"));

	            item.setM_title(rs.getString("M_TITLE"));
	            item.setM_dt(rs.getString("M_DT"));
	            item.setM_genre1(rs.getString("M_GENRE1"));
	            item.setM_genre2(rs.getString("M_GENRE2"));
	            item.setM_genre3(rs.getString("M_GENRE3"));

	            item.setM_filename(rs.getString("M_FILENAME"));
	            item.setM_filepath(rs.getString("M_FILEPATH"));

	            item.setR_content(rs.getString("R_CONTENT"));
	            item.setR_dt(rs.getString("R_DT"));
	            item.setR_rating(rs.getDouble("R_RATING"));
	        }

	    } catch (Exception e) {

	        e.printStackTrace();

	    } finally {

	        if (rs != null) try { rs.close(); } catch (Exception e) {}
	        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
	        if (conn != null) try { conn.close(); } catch (Exception e) {}
	    }

	    return item;
	}
	
	// 리뷰 수정
	public boolean reviewUpdate(ReviewDto reviewdto) throws Exception {
		int result = 0;	// UPDATE 실행 결과 저장
		
		Connection        conn  = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 리뷰 번호와 작성자 ID가 일치하는 경우에만 리뷰 수정
			String sql = "UPDATE MV_BOARD SET "
					   + "R_TITLE = ?, M_TITLE = ?, M_DT = ?, "
					   + "M_GENRE1 = ?, M_GENRE2 = ?, M_GENRE3 = ?, "
					   + "M_FILENAME = ?, M_FILEPATH= ?, "
					   + "R_CONTENT = ? "
					   + "WHERE R_NO = ? AND USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reviewdto.getR_title());
	        pstmt.setString(2, reviewdto.getM_title());
	        pstmt.setString(3, reviewdto.getM_dt());
	        pstmt.setString(4, reviewdto.getM_genre1());
	        pstmt.setString(5, reviewdto.getM_genre2());
	        pstmt.setString(6, reviewdto.getM_genre3());
	        pstmt.setString(7, reviewdto.getM_filename());
	        pstmt.setString(8, reviewdto.getM_filepath());
	        pstmt.setString(9, reviewdto.getR_content());
	        pstmt.setInt(10, reviewdto.getR_no());
	        pstmt.setString(11, reviewdto.getUserid());
			result = pstmt.executeUpdate();
			
		} 
		catch (Exception e) {e.printStackTrace();} 
		finally 
		{
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
			if (conn  != null) try { conn.close();  } catch (Exception e) {};
		}
		
		// 수정된 행이 1개이면 수정 성공
		if (result == 1) {return true; } 
		else 			 {return false;}
	}
	
	// 리뷰 삭제
	public boolean reviewDelete(int r_no) 
	{
	    Connection        conn       = null;
	    PreparedStatement replyPstmt = null;
	    PreparedStatement boardPstmt = null;

	    try 
	    {
	        conn = OracleConnect.getConnection();

	        // 자동 커밋 끄기
	        conn.setAutoCommit(false);

	        // 1. 해당 리뷰의 댓글 먼저 삭제
	        String replySql =
	                "DELETE FROM MV_REPLY "
	              + "WHERE R_NO = ?";

	        replyPstmt = conn.prepareStatement(replySql);
	        replyPstmt.setInt(1, r_no);

	        replyPstmt.executeUpdate();

	        // 2. 리뷰 삭제
	        String boardSql =
	                "DELETE FROM MV_BOARD "
	              + "WHERE R_NO = ?";

	        boardPstmt = conn.prepareStatement(boardSql);
	        boardPstmt.setInt(1, r_no);

	        int result = boardPstmt.executeUpdate();

	        // 리뷰가 실제로 삭제된 경우에만 커밋
	        if (result == 1) 
	        {
	            conn.commit();
	            return true;
	        } 
	        else 
	        {
	            // 리뷰 삭제 실패 → 댓글 삭제도 취소
	            conn.rollback();
	            return false;
	        }
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	        // 오류 발생 시 전체 취소
	        if (conn != null) 
	        {
	            try 
	            {
	                conn.rollback();
	            } 
	            catch (Exception rollbackException) {rollbackException.printStackTrace();}
	        }
	        return false;
	    } 
	    finally 
	    {
	        if (replyPstmt != null) 
	        {
	            try 
	            {
	                replyPstmt.close();
	            } 
	            catch (Exception e) {}
	        }
	        if (boardPstmt != null) 
	        {
	            try 
	            {
	                boardPstmt.close();
	            } 
	            catch (Exception e) {}
	        }
	        if (conn != null) 
	        {
	            try {conn.setAutoCommit(true);} 
	            catch (Exception e) {}
	            try {conn.close();} 
	            catch (Exception e) {}
	        }
	    }
	}
	
	// 전체 리뷰 수 조회
	public int count() throws Exception 
	{
	    int count = 0;

	    Connection        conn  = null;
	    PreparedStatement pstmt = null;
	    ResultSet         rs    = null;

	    try 
	    {
	        conn = OracleConnect.getConnection();

	        if (conn == null) {return count;}

	        String sql = "SELECT COUNT(*) FROM MV_BOARD"; 	// MV_BOARD의 전체 리뷰 수 조회

	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {count = rs.getInt(1);}			// 조회된 리뷰 수 저장
	    }
	    catch (Exception e) {e.printStackTrace();} 
	    finally 
	    {
	        if (rs    != null) try { rs.close();    } catch (Exception e) {}
	        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
	        if (conn  != null) try { conn.close();  } catch (Exception e) {}
	    }

	    return count;	// 전체 리뷰 수 반환
	}
	
	// 리뷰 목록 검색
	public ArrayList<ReviewDto> searchList(int pagingno, String searchType, String keyword) throws Exception
	{
	    ArrayList<ReviewDto> list = new ArrayList<ReviewDto>();

	    Connection        conn  = null;
	    PreparedStatement pstmt = null;
	    ResultSet         rs    = null;

	    int perpage  = 6;                              // 한 페이지에 보여줄 리뷰 수
	    int startrow = (pagingno - 1) * perpage + 1;  // 조회할 리뷰의 시작 행 번호
	    int endrow   = pagingno * perpage;             // 조회할 리뷰의 마지막 행 번호

	    try
	    {
	        conn = OracleConnect.getConnection();

	        String column = "";

	        // 검색 조건에 따라 조회할 컬럼 설정
	        if ("title".equals(searchType))
	        {
	            column = "B.R_TITLE";                  // 리뷰 제목
	        }
	        else if ("movieTitle".equals(searchType))
	        {
	            column = "B.M_TITLE";                  // 영화 제목
	        }
	        else if ("writer".equals(searchType))
	        {
	            column = "U.NICK";                     // 작성자 닉네임
	        }
	        else
	        {
	            column = "B.R_TITLE";                 // 기본값
	        }

	        // 검색 조건에 맞는 리뷰를 최신순으로 조회
	        String sql =
	            "SELECT * FROM ( " +
	            "    SELECT ROW_NUMBER() OVER (ORDER BY B.R_NO DESC) AS RNUM, " +
	            "           B.R_NO, " +
	            "           B.USERID, " +
	            "           U.NICK, " +
	            "           B.R_TITLE, " +
	            "           B.M_TITLE, " +
	            "           B.M_DT, " +
	            "           B.M_GENRE1, " +
	            "           B.M_GENRE2, " +
	            "           B.M_GENRE3, " +
	            "           B.M_FILENAME, " +
	            "           B.M_FILEPATH, " +
	            "           B.R_CONTENT, " +
	            "           B.R_DT, " +
	            "           B.R_RATING " +
	            "    FROM MV_BOARD B " +
	            "    LEFT JOIN MV_USER U ON B.USERID = U.USERID " +
	            "    WHERE " + column + " LIKE ? " +
	            ") " +
	            "WHERE RNUM BETWEEN ? AND ?";

	        pstmt = conn.prepareStatement(sql);

	        // 검색어
	        pstmt.setString(1, "%" + keyword + "%");

	        // 페이징 시작 번호
	        pstmt.setInt(2, startrow);

	        // 페이징 마지막 번호
	        pstmt.setInt(3, endrow);

	        rs = pstmt.executeQuery();

	        while (rs.next())
	        {
	            ReviewDto item = new ReviewDto();

	            item.setR_no(rs.getInt("R_NO"));
	            item.setUserid(rs.getString("USERID"));
	            item.setNick(rs.getString("NICK"));             // 닉네임 추가

	            item.setR_title(rs.getString("R_TITLE"));
	            item.setM_title(rs.getString("M_TITLE"));
	            item.setM_dt(rs.getString("M_DT"));

	            item.setM_genre1(rs.getString("M_GENRE1"));
	            item.setM_genre2(rs.getString("M_GENRE2"));
	            item.setM_genre3(rs.getString("M_GENRE3"));

	            item.setM_filename(rs.getString("M_FILENAME"));
	            item.setM_filepath(rs.getString("M_FILEPATH"));

	            item.setR_content(rs.getString("R_CONTENT"));
	            item.setR_dt(rs.getString("R_DT"));
	            item.setR_rating(rs.getDouble("R_RATING"));

	            list.add(item);
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        if (rs != null)
	            try { rs.close(); } catch (Exception e) {}

	        if (pstmt != null)
	            try { pstmt.close(); } catch (Exception e) {}

	        if (conn != null)
	            try { conn.close(); } catch (Exception e) {}
	    }

	    return list;
	}
	
	// 검색 조건에 맞는 리뷰 수 조회
	public int searchCount(String searchType, String keyword) throws Exception 
	{
	    int count = 0;

	    Connection        conn  = null;
	    PreparedStatement pstmt = null;
	    ResultSet         rs    = null;

	    try 
	    {
	        conn = OracleConnect.getConnection();

	        String column = "";

	        if ("title".equals(searchType))           {column = "R_TITLE";}     // 리뷰제목
	        else if ("movieTitle".equals(searchType)) {column = "M_TITLE";}  	// 영화제목
	        else if ("writer".equals(searchType)) 	  {column = "USERID"; } 	// 작성자
	        else 									  {column = "R_TITLE";}		// 기본값: 리뷰제목	

	        // 검색 조건에 해당하는 리뷰 개수 조회
	        String sql =
	            "SELECT COUNT(*) " +
	            "FROM MV_BOARD " +
	            "WHERE " + column + " LIKE ?";

	        pstmt = conn.prepareStatement(sql);

	        pstmt.setString(1, "%" + keyword + "%");	 // 검색어 설정

	        rs = pstmt.executeQuery();

	        if (rs.next()) {count = rs.getInt(1);}		// 검색된 리뷰 수 저장
	    } 
	    catch (Exception e) {e.printStackTrace();} 
	    finally 
	    {
	        if (rs    != null) try { rs.close();    } catch (Exception e) {}
	        if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
	        if (conn  != null) try { conn.close();  } catch (Exception e) {}
	    }

	    return count;	// 검색된 리뷰 개수 반환
	}
	
	//리뷰 작성 및 submit 클릭시 입력값 유효성 검사 로직
	public boolean reviewwriteVal(String r_title, String m_title, String m_dt, String m_genre1,
								  String m_genre2, String m_genre3, String r_content, String originalFileName)
	{
		// 1. 필수 텍스트 필드 검사 (제목, 영화제목, 개봉일자, 내용)
	    if (r_title == null || r_title.trim().isEmpty() ||
	        m_title == null || m_title.trim().isEmpty() ||
	        m_dt == null || m_dt.trim().isEmpty() ||
	        r_content == null || r_content.trim().isEmpty()) 
	    {
	        return false;
	    }
	    
	    int contentByte = r_content.getBytes(StandardCharsets.UTF_8).length;
	    if(r_title.length() > 200 || m_title.length() > 200 || m_dt.length() > 10 || contentByte > 4000 )
	    {
	    	return false;
	    }
	    // 2. 개봉일자(yyyy.mm.dd) 형식 및 실제 달력상 유효한 일자인지 검사
	    // 1) 앞뒤 불필요한 공백 제거
	    String cleanDate = m_dt.trim();

	    // 2) 날짜 포맷 규칙 설정 ("연도4자리.월2자리.일2자리")
	    // uuuu는 엄격 검증 모드에서 연도를 숫자로 안전하게 인식하기 위해 사용합니다.
	    String datePattern = "uuuu.MM.dd";

	    // 3) 기본 포맷터 생성
	    DateTimeFormatter baseFormatter = DateTimeFormatter.ofPattern(datePattern);

	    // 4) 엄격 모드(STRICT) 적용
	    // 기본 모드(SMART)는 2월 30일을 2월 29일 등으로 자동 보정해버리므로,
	    // 없는 날짜를 칼같이 걸러내기 위해 엄격 검증 모드로 변경합니다.
	    DateTimeFormatter strictFormatter = baseFormatter.withResolverStyle(ResolverStyle.STRICT);

	    // 5) 문자열을 실제 날짜(LocalDate) 객체로 변환 시도
	    try {
	        // 포맷 규칙에 맞지 않거나 달력에 없는 날짜(예: 2024.02.30, 2024.13.01)이면 예외가 발생합니다.
	        LocalDate parsedDate = LocalDate.parse(cleanDate, strictFormatter);
	    } 
	    catch (DateTimeParseException e) {
	        // 날짜 형식이 틀렸거나 존재하지 않는 날짜일 때 false 반환
	        return false;
	    }
	    
	    
	    // 3. 장르 3개가 전부 '선택안함'이거나 비어있는지 검사 (NPE 방지)
	    boolean g1Empty = (m_genre1 == null || m_genre1.trim().isEmpty() || "선택안함".equals(m_genre1));
	    boolean g2Empty = (m_genre2 == null || m_genre2.trim().isEmpty() || "선택안함".equals(m_genre2));
	    boolean g3Empty = (m_genre3 == null || m_genre3.trim().isEmpty() || "선택안함".equals(m_genre3));

	    if (g1Empty && g2Empty && g3Empty) 
	    {
	        return false; // 최소 1개 이상 선택해야 함
	    }
	    
	    if (!g1Empty && m_genre1.getBytes(StandardCharsets.UTF_8).length > 50) return false;
	    if (!g2Empty && m_genre2.getBytes(StandardCharsets.UTF_8).length > 50) return false;
	    if (!g3Empty && m_genre3.getBytes(StandardCharsets.UTF_8).length > 50) return false;	    
	    
	    // 4. 파일 확장자 검사 (첨부 파일이 있을 때만 검증)
	    if (originalFileName != null && !originalFileName.trim().isEmpty())
	    {
	        // 점(.)이 없거나(dotIndex == -1), 파일명이 '.'으로 끝나는 비정상 파일 차단
	        int dotIndex = originalFileName.lastIndexOf(".");
	        if (dotIndex == -1 || dotIndex == originalFileName.length() - 1) 
	        {
	            return false;
	        }

	        // 점 다음 글자부터 잘라내고, 대문자(JPG, PNG 등)를 소문자로 통일
	        String ext = originalFileName.substring(dotIndex + 1).toLowerCase();
	        
	        // 허용 확장자 화이트리스트 검사
	        switch (ext)
	        {
		        case "jpg":
		        case "jpeg":
		        case "png":
		        case "webp":
		        case "gif":
		            // 허용된 확장자이므로 계속 진행
		            break;
		        default:
		            // 허용되지 않은 확장자는 바로 유효성 검증 실패
		            return false;
	        }
	    }
		return true;
	}
}
