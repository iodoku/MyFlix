package dto;

public class ReplyDto {
	private int rp_no;
	private String rp_content;
	private double rp_rating;
	private String rp_dt;
	private int r_no;
	private String userid;
	private String nick;
	private String r_title;
	
	public int getRp_no() {
		return rp_no;
	}
	
	public String getRp_content() {
		return rp_content;
	}

	public void setRp_rating(double rp_rating) {
	    this.rp_rating = rp_rating;
	}
	
	public String getRp_dt() {
		return rp_dt;
	}
	
	public int getR_no() {
		return r_no;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setRp_no(int rp_no) {
		this.rp_no = rp_no;
	}
	
	public void setRp_content(String rp_content) {
		this.rp_content = rp_content;
	}
	
	public double getRp_rating() 
	{
	    return rp_rating;
	}
	
	public void setRp_dt(String rp_dt) {
		this.rp_dt = rp_dt;
	}
	
	public void setR_no(int r_no) {
		this.r_no = r_no;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getR_title() {
		return r_title;
	}

	public void setR_title(String r_title) {
		this.r_title = r_title;
	}
	
}
