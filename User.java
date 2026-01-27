package dev;

public class User {
	String email;
	String name;
	String password;
	int user_id;
	public User(String email, String name, String password, int user_id) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", password=" + password + ", user_id=" + user_id + "]";
	}
	
}