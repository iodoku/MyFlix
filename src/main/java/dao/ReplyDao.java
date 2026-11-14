package dao;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.ReplyDto;
import dto.ReviewDto;
import util.OracleConnect;

public class ReplyDao 
{
    // 댓글 목록 가져오는 함수
    public ArrayList<ReplyDto> list(int r_no) throws Exception 
    {
        ArrayList<ReplyDto> list   = new ArrayList<ReplyDto>();
        Connection          conn   = null;
        PreparedStatement   pstmt  = null;
        ResultSet           rs     = null;

        try 
        {
            conn = OracleConnect.getConnection();	// DB 연결

            if (conn == null) {return list;}

            // 해당 리뷰(r_no)에 작성된 댓글 정보 조회
            // MV_REPLY와 MV_USER를 JOIN하여 댓글 작성자의 닉네임도 함께 조회
            String sql =
                    "SELECT R.RP_NO, R.RP_CONTENT, R.RP_RATING, R.RP_DT, R.R_NO, R.USERID, U.NICK "
                  + "FROM MV_REPLY R "
                  + "JOIN MV_USER U ON R.USERID = U.USERID "
                  + "WHERE R.R_NO = ? "
                  + "ORDER BY R.RP_NO DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, r_no);	// 조회할 리뷰 번호 설정

            rs = pstmt.executeQuery();

            while (rs.next()) // 조회된 댓글을 하나씩 DTO에 저장			
            {
                ReplyDto item = new ReplyDto();

                item.setRp_no(rs.getInt("RP_NO"));              	// 댓글 번호
                item.setRp_content(rs.getString("RP_CONTENT")); 	// 댓글 내용
                item.setRp_rating(rs.getDouble("RP_RATING"));   	// 댓글 평점
                item.setRp_dt(rs.getString("RP_DT"));            	// 댓글 작성일
                item.setR_no(rs.getInt("R_NO"));                 	// 리뷰 번호
                item.setUserid(rs.getString("USERID"));          	// 작성자 아이디
                item.setNick(rs.getString("NICK"));              	// 작성자 닉네임

                list.add(item);										// DTO를 댓글 목록에 추가
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            if (rs != null   ) try { rs.close();    } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null ) try { conn.close();  } catch (Exception e) {}
        }
        return list;
    }
    
    // 댓글 작성하기
    public boolean insert(ReplyDto dto) throws Exception 
    {
        Connection        conn  = null;
        PreparedStatement pstmt = null;

        try 
        {
            conn = OracleConnect.getConnection();

         // 댓글 내용, 평점, 작성자 아이디, 리뷰 번호를 MV_REPLY에 저장
            String sql =
                    "INSERT INTO MV_REPLY "
                  + "(RP_CONTENT, RP_RATING, USERID, R_NO) "
                  + "VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            // DTO에 저장된 댓글 정보를 SQL에 설정
            pstmt.setString(1, dto.getRp_content()); // 댓글 내용
            pstmt.setDouble(2, dto.getRp_rating());  // 댓글 평점
            pstmt.setString(3, dto.getUserid());     // 작성자 아이디
            pstmt.setInt(4, dto.getR_no());          // 리뷰 번호

            int result = pstmt.executeUpdate();		// INSERT 실행
            return result > 0;						// 1개 이상의 행이 추가되면 등록 성공
        } 
        finally 
        {
            if (pstmt != null) {try { pstmt.close(); } catch (Exception e) {}}
            if (conn  != null) {try { conn.close();  } catch (Exception e) {}}
        }
    }
    
    public boolean insertcheck(double ic_rp_rating, String ic_rp_content)
    {
    	int contentByte = ic_rp_content.getBytes(StandardCharsets.UTF_8).length;
    	
    	if(contentByte > 4000) 										return false;
    	if(ic_rp_rating <1.f || ic_rp_rating > 5.f) 				return false;
    	if(ic_rp_content == null || ic_rp_content.trim().isEmpty()) return false;
    	
    	return true;
    }
    
    public boolean update(ReplyDto dto) throws Exception 
    {
        Connection        conn  = null;
        PreparedStatement pstmt = null;

        int result = 0;

        try 
        {
            conn = OracleConnect.getConnection();
            // 댓글 내용과 평점을 수정
            // RP_NO와 USERID가 일치하는 댓글만 수정 가능
            String sql =
                    "UPDATE MV_REPLY "
                  + "SET RP_CONTENT = ?, "
                  + "    RP_RATING = ? "
                  + "WHERE RP_NO = ? "
                  + "AND USERID = ?";

            pstmt = conn.prepareStatement(sql);

            // 수정할 댓글 정보 설정
            pstmt.setString(1, dto.getRp_content()); // 수정된 댓글 내용
            pstmt.setDouble(2, dto.getRp_rating());  // 수정된 댓글 평점

            // 수정할 댓글 번호와 작성자 설정
            pstmt.setInt(3, dto.getRp_no());         // 댓글 번호
            pstmt.setString(4, dto.getUserid());     // 작성자 아이디

            // UPDATE 실행
            result = pstmt.executeUpdate();

        } 
        finally 
        {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn  != null) try { conn.close();  } catch (Exception e) {}
        }
        return result > 0;	// 수정된 행이 1개 이상이면 수정 성공
    }
    
    public boolean delete(int rp_no, String userid) throws Exception 
    {
        Connection        conn  = null;
        PreparedStatement pstmt = null;

        int result = 0;

        try 
        {
            conn = OracleConnect.getConnection();
            // 댓글 번호에 해당하는 댓글 삭제
            String sql =
                    "DELETE FROM MV_REPLY "
                  + "WHERE RP_NO = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, rp_no);			// 삭제할 댓글 번호 설정

            result = pstmt.executeUpdate();	// DELETE 실행
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn  != null) try { conn.close();  } catch (Exception e) {}
        }
        return result > 0;	// 삭제된 행이 1개 이상이면 삭제 성공
    }
    
    public boolean updateReviewRating(int r_no) throws Exception 
    {
        Connection        conn  = null;
        PreparedStatement pstmt = null;

        try 
        {
            conn = OracleConnect.getConnection();
            
            // 해당 리뷰의 댓글 평점 평균을 계산하여 리뷰 평점(R_RATING) 갱신
            // 댓글이 하나도 없으면 NVL을 사용하여 0점으로 설정
            String sql =
                    "UPDATE MV_BOARD "
                  + "SET R_RATING = ( "
                  + "    SELECT NVL(AVG(RP_RATING), 0) "
                  + "    FROM MV_REPLY "
                  + "    WHERE R_NO = ? "
                  + ") "
                  + "WHERE R_NO = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, r_no);	// 평균을 계산할 댓글의 리뷰 번호
            pstmt.setInt(2, r_no);	// 평점을 수정할 리뷰 번호

            int result = pstmt.executeUpdate();	// UPDATE 실행
            return result > 0;					// 수정된 행이 1개 이상이면 갱신 성공

        } 
        finally 
        {
            if (pstmt != null) {try { pstmt.close(); } catch (Exception e) {}}
            if (conn  != null) {try { conn.close();  } catch (Exception e) {}}
        }
    }
    
    public int getReviewNo(int rp_no) throws Exception 
    {
        Connection        conn   = null;
        PreparedStatement pstmt  = null;
        ResultSet         rs     = null;

        try 
        {
            conn = OracleConnect.getConnection();

            // 댓글 번호로 해당 댓글이 작성된 리뷰 번호 조회
            String sql =
                    "SELECT R_NO "
                  + "FROM MV_REPLY "
                  + "WHERE RP_NO = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, rp_no);		// 조회할 댓글 번호 설정

            rs = pstmt.executeQuery();	

            if (rs.next()) {return rs.getInt("R_NO");}	// 조회 결과가 있으면 해당 리뷰 번호 반환

            return 0;	// 해당 댓글이 존재하지 않으면 0 반환

        } 
        finally
        {
            if (rs    != null) {try { rs.close();    } catch (Exception e) {}}
            if (pstmt != null) {try { pstmt.close(); } catch (Exception e) {}}
            if (conn  != null) {try { conn.close();  } catch (Exception e) {}}
        }
    }
    
    // 내 댓글 목록
    public ArrayList<ReplyDto> myreplylist(int pagingno, String userid) throws Exception {

        ArrayList<ReplyDto> myreply = new ArrayList<ReplyDto>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // 한 화면에 보여줄 댓글 수
        int perpage = 10;

        // 시작(DB 컬럼)
        int startrow = (pagingno - 1) * perpage + 1;

        // 끝(DB 컬럼)
        int endrow = pagingno * perpage;

        try {

            conn = OracleConnect.getConnection();

            if (conn == null) {
                // 연결이 안되어있다면 빈 화면
                return myreply;
            }

            String sql = "SELECT * FROM ( "
                    + "    SELECT ROW_NUMBER() OVER (ORDER BY R.RP_NO DESC) AS RNUM, "
                    + "           R.RP_NO, "
                    + "           R.RP_CONTENT, "
                    + "           R.RP_RATING, "
                    + "           R.R_NO, "
                    + "           R.USERID, "
                    + "           U.NICK, "
                    + "           TO_CHAR(R.RP_DT, 'YYYY-MM-DD') AS RP_DT, "
                    + "           B.R_TITLE "
                    + "    FROM MV_REPLY R "
                    + "    JOIN MV_BOARD B ON R.R_NO = B.R_NO "
                    + "    JOIN MV_USER U ON R.USERID = U.USERID "
                    + "    WHERE R.USERID = ? "
                    + ") WHERE RNUM BETWEEN ? AND ? ";

            pstmt = conn.prepareStatement(sql);

            // 화면에 보여줄 댓글만 추출
            pstmt.setString(1, userid);
            pstmt.setInt(2, startrow);
            pstmt.setInt(3, endrow);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                ReplyDto item = new ReplyDto();

                item.setRp_no(rs.getInt("RP_NO"));
                item.setRp_content(rs.getString("RP_CONTENT"));
                item.setRp_dt(rs.getString("RP_DT"));
                item.setR_no(rs.getInt("R_NO"));
                item.setUserid(rs.getString("USERID"));
                item.setNick(rs.getString("NICK"));   // 닉네임 추가
                item.setR_title(rs.getString("R_TITLE"));

                // 리스트에 불러온 내 댓글 목록 저장
                myreply.add(item);
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
            if (conn != null) try { conn.close(); } catch (Exception e) {}
        }

        // 내 댓글 목록 반환
        return myreply;
    }
 	
 	// 모든 댓글 개수
 	public int totalReply() throws Exception {
 		int totalreply = 0;
 		Connection conn = null;
 		PreparedStatement pstmt = null;
 		ResultSet rs = null;
 		
 		try {
 			conn = OracleConnect.getConnection();
 			
 			// 모든 댓글 개수 구하기
 			String sql = "SELECT COUNT(*) FROM MV_REPLY ";
 			
 			pstmt = conn.prepareStatement(sql);
 			rs = pstmt.executeQuery();
 			
 			if (rs.next()) {
 				// 모든 댓글 개수 저장
 				totalreply = rs.getInt(1);
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		} finally {
 			if (rs != null) try { rs.close(); } catch (Exception e) {};
 			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
 			if (conn != null) try { conn.close(); } catch (Exception e) {};
 		}
 		
 		// 댓글 개수 반환
 		return totalreply;
 	}
 	
 	// 내 모든 댓글 개수
  	public int myTotalReply(String userid) throws Exception {
  		int myreply = 0;
  		Connection conn = null;
  		PreparedStatement pstmt = null;
  		ResultSet rs = null;
  		
  		try {
  			conn = OracleConnect.getConnection();
  			
  			// 내 모든 댓글 개수 구하기
  			String sql = "SELECT COUNT(*) FROM MV_REPLY "
  					+ "WHERE USERID = ? ";
  			
  			pstmt = conn.prepareStatement(sql);
  			pstmt.setString(1, userid);
  			rs = pstmt.executeQuery();
  			
  			if (rs.next()) {
  				// 내 모든 댓글 개수 저장
  				myreply = rs.getInt(1);
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			if (rs != null) try { rs.close(); } catch (Exception e) {};
  			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {};
  			if (conn != null) try { conn.close(); } catch (Exception e) {};
  		}
  		
  		// 내 모든 댓글 개수 반환
  		return myreply;
  	}
}