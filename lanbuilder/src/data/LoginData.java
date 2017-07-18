package data;

public class LoginData {

	private String ip;
	private String user;
	private String password;
	private String secret;
	
	public LoginData(){
		ip = "192.168.1.11";
		user = "test";
		password = "test";
		secret = "test";
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}

}
