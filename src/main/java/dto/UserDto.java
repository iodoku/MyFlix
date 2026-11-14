package dto;

public class UserDto {
	private String userid;
	private String userpw;
	private String nick;
	private String email;
	private String join_dt;
	
	public String getUserid() {
		return userid;
	}
	
	public String getUserpw() {
		return userpw;
	}
	
	public String getNick() {
		return nick;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getJoin_dt() {
		return join_dt;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setJoin_dt(String join_dt) {
		this.join_dt = join_dt;
	}

}
