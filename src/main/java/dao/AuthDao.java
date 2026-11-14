package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dto.UserDto;
import util.OracleConnect;

public class AuthDao {
	// 회원가입
	public int join(UserDto userdto) throws Exception {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			conn = OracleConnect.getConnection();
			
			
			
			// 유저 테이블에 값 집어넣기
			String sql = "INSERT INTO MV_USER "
					   + "(USERID, USERPW, NICK, EMAIL) "
					   + "VALUES (?, ?, ?, ?) ";
			
			// BCrypt로 해싱하기
			String hashpw = BCrypt.hashpw(userdto.getUserpw(), BCrypt.gensalt());
						
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userdto.getUserid());
			pstmt.setString(2, hashpw);
			pstmt.setString(3, userdto.getNick());
			pstmt.setString(4, userdto.getEmail());
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return result;
	}
	
	// 아이디 중복검사
	public boolean idCheck(String userid) throws Exception {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 유저 테이블에 아이디가 있으면 카운트
			String sql = "SELECT COUNT(*) AS CNT "
					+ "FROM MV_USER "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				cnt = rs.getInt("CNT");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return cnt <= 0;
	}
	
	// 닉네임 중복검사
	public boolean nickCheck(String nick) throws Exception {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 유저 테이블에 닉네임이 있으면 카운트
			String sql = "SELECT COUNT(*) AS CNT "
					+ "FROM MV_USER "
					+ "WHERE NICK = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cnt = rs.getInt("CNT");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return cnt <= 0;
	}
	
	// 닉네임 변경
	public int nickChange(String userid, String nick) throws Exception {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 유저 테이블의 유저 닉네임 변경
			String sql = "UPDATE MV_USER "
					+ "SET NICK = ? "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nick);
			pstmt.setString(2, userid);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return result;
	}
	
	// 이메일 변경
	public int emailChange(String userid, String email) throws Exception {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 유저 테이블의 유저 이메일 변경
			String sql = "UPDATE MV_USER "
					+ "SET EMAIL = ? "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, userid);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return result;
	}
	
	// 로그인
	public boolean login(String userid, String userpw) throws Exception {
		// 로그인 여부
		boolean islogin = false;
		
		Connection conn = null;		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			// 만약 연결이 안되어있다면 false 반환
			if (conn == null) {
				return islogin;
			}
			
			// userid 이용해서 해싱된 비밀번호 값 가져오기
			String sql = "SELECT USERPW "
					+ "FROM MV_USER "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			// 해싱된 비밀번호 값 가져오고 입력한 비밀번호와 비교하기
			if (rs.next()) {
				String hashpw = rs.getString("USERPW");
				
				// 비밀번호가 일치한다면 로그인 성공
				if (BCrypt.checkpw(userpw, hashpw)) {
					islogin = true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return islogin;
	}
	
	// 아이디 찾기
	public List<String> findid(String email) throws Exception 
	{
		List<String> useridList = new ArrayList<>();
		
		Connection        conn  = null;
		PreparedStatement pstmt = null;
		ResultSet         rs    = null;
		
		try 
		{ 
			conn = OracleConnect.getConnection();
			
			// 입력한 이메일과 일치하는 아이디 가져오기
			String sql = "SELECT USERID "
					+ "FROM MV_USER "
					+ "WHERE EMAIL = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			// 아이디 가져오기
			while(rs.next()) 
			{
				useridList.add(rs.getString("USERID"));
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			 if (rs    != null) { try { rs.close();    } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn  != null) { try { conn.close();  } catch (Exception e) {} }		
		}	
		return useridList;
	}
	
	// 임시 비밀번호 발급
	public boolean changePw(String userid, String email, String tmppw) throws Exception {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String hashpw = BCrypt.hashpw(tmppw, BCrypt.gensalt());
		
		try {
			conn = OracleConnect.getConnection();
			
			// 입력한 아이디와 이메일을 이용해 일치하는 비밀번호 값 변경
			String sql = "UPDATE MV_USER "
					+ "SET USERPW = ? "
					+ "WHERE USERID = ? AND EMAIL = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hashpw);
			pstmt.setString(2, userid);
			pstmt.setString(3, email);
			
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }		
		}
		
		return result > 0;
	}
	
	// 이메일 중복검사
	public boolean emailCheck(String email) throws Exception {
		int cnt = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			// 유저 테이블에 이메일이 있으면 카운트
			String sql = "SELECT COUNT(*) AS CNT "
					+ "FROM MV_USER "
					+ "WHERE EMAIL = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cnt = rs.getInt("CNT");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }
		}
		
		return cnt <= 0;
	}
	
	public boolean joinFullCheck(String userid, boolean isidcheck, String userpw, 
							String email, String nick, boolean isnickcheck)
	{
		/*** 정규식 표현 ***/
		String idRegex = "^[a-z0-9]{5,10}$";
		// 영어 소문자 + 숫자, 5~10자

		String pwRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!?#&*])[a-zA-Z\\d!?#&*]{6,15}$";
		// 영문자 + 숫자 + 특수문자(! ? # & *), 각각 1개 이상, 6~15자

		String nickRegex = "^[a-zA-Z0-9가-힣_]{3,20}$";
		// 영문 + 숫자 + 한글 + _, 3~20자      
        
        if(userid == null || !userid.matches(idRegex) || !isidcheck) return false;
		if(userpw == null || !userpw.matches(pwRegex)) return false;
		if(email == null ) return false;
		if(nick == null || !nick.matches(nickRegex) || !isnickcheck) return false;
		
		return true;
	}
	
	// 로그인 시 닉네임 같이 보내기
	public String getnick(String userid) throws Exception {
		String nick = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try { 
			conn = OracleConnect.getConnection();
			
			// 입력한 아이디와 일치하는 닉네임 가져오기
			String sql = "SELECT NICK "
					+ "FROM MV_USER "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			// 닉네임 가져오기
			if (rs.next()) {
				nick = rs.getString("NICK");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }		
		}
		
		return nick;
	}
	
	// 로그인 시 이메일 같이 보내기
	public String getemail(String userid) throws Exception {
		String email = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try { 
			conn = OracleConnect.getConnection();
			
			// 입력한 아이디와 일치하는 이메일 가져오기
			String sql = "SELECT EMAIL "
					+ "FROM MV_USER "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			// 이메일 가져오기
			if (rs.next()) {
				email = rs.getString("EMAIL");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }		
		}
		
		return email;
	}
	
	// 비밀번호 일치 여부
	public boolean pwCheck(String userid, String currentpw) {
		String oldpw = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			String sql = "SELECT USERPW "
					+ "FROM MV_USER "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				oldpw = rs.getString("USERPW");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (rs != null) { try { rs.close(); } catch (Exception e) {} }
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }		
		}
		
		if (oldpw == null) return false;
		
		return BCrypt.checkpw(currentpw, oldpw);
	}
	
	// 새 비밀번호로 변경
	public int newPw(String userid, String newpw) {
		int result = 0;
		
		String hashpw = BCrypt.hashpw(newpw, BCrypt.gensalt());
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = OracleConnect.getConnection();
			
			String sql = "UPDATE MV_USER "
					+ "SET USERPW = ? "
					+ "WHERE USERID = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hashpw);
			pstmt.setString(2, userid);
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     if (pstmt != null) { try { pstmt.close(); } catch (Exception e) {} }
		     if (conn != null) { try { conn.close(); } catch (Exception e) {} }		
		}
		
		return result;
	}
}
